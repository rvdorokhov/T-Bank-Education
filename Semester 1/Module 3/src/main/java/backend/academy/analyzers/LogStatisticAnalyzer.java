package backend.academy.analyzers;

import backend.academy.analyzers.interfaces.LogAnalyzer;
import backend.academy.helpers.MapHandlerHelper;
import backend.academy.parser.LogRecord;
import backend.academy.parser.NGINXLogParser;
import com.google.common.math.Quantiles;
import java.io.BufferedReader;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@code LogStatisticAnalyzer} - анализатор потока логов -
 * предоставляет API для рассчета статистики потока логов в формате {@code String}
 * <p>Пример использования:
 * <blockquote><pre>
 * LogAnalyzer&lt;LogAnalysisResult&gt; logAnalyzer =
 *             new LogStatisticAnalyzer("GET", OffsetDateTime.MIN, OffsetDateTime.MAX);
 * LogAnalysisResult result = logAnalyzer.analyzeLogs(bufferedReader);
 * </pre></blockquote>
 * @see LogAnalyzer
 * @see LogAnalysisResult
 */
public class LogStatisticAnalyzer implements LogAnalyzer<LogAnalysisResult> {
    protected static final Logger LOGGER = LogManager.getLogger(LogStatisticAnalyzer.class);

    protected int numberOfRequests;
    protected Map<String, Integer> mostFrequentResources;
    protected Map<Integer, Integer> mostFrequentResponseStatuses;
    protected Map<String, Integer> lessFrequentResources;
    protected Map<Integer, Integer> lessFrequentResponseStatuses;
    protected double averageResponseSize;
    protected double percentile;

    protected String filter;
    protected OffsetDateTime from;
    protected OffsetDateTime to;

    protected Map<String, Integer> resourcesCounter;
    protected Map<Integer, Integer> responseStatusesCounter;
    protected BigInteger sumOfBytesSent;
    protected List<Integer> listOfBytesSent;

    @Getter protected LogAnalysisResult result;

    protected static final int PERCENTILE_PER_CENT = 95;

    /**
     * Рассчитывает статистику потока логов в формате {@code String} и возвращает результат анализа
     * в виде записи {@code LogAnalysisResult}
     * <p>Рассчитываемая статистика:
     * <ul>
     *     <li>Количество запросов (количество логов)</li>
     *     <li>Самые часто запрашиваемые ресурсы</li>
     *     <li>Самые частые коды ответа</li>
     *     <li>Самые редко запрашиваемые ресурсы</li>
     *     <li>Самые редкие коды ответа</li>
     *     <li>Средний размер ответа</li>
     *     <li>Перцентиль</li>
     *     <li>Сколько всего байт было отправлено</li>
     * </ul>
     * <p>При каждом новом вызове метода {@code analyzeLogs(BufferedReader)}
     * обнуления полей не происходит,
     * поэтому при последовательном вызове объекта класса {@code LogAnalysisResult}
     * для разных {@code BufferedReader}
     * статистика будет считаться для всех {@code BufferedReader} в совокупности:
     * <blockquote><pre>
     * LogAnalyzer&lt;LogAnalysisResult&gt; logAnalyzer =
     *             new LogStatisticAnalyzer("GET", OffsetDateTime.MIN, OffsetDateTime.MAX);
     * // проанализировали первый поток
     * LogAnalysisResult result = logAnalyzer.analyzeLogs(bufferedReader1);
     * // проанализировали второй поток, в result хранится результат анализа двух потоков как для одного потока
     * LogAnalysisResult result = logAnalyzer.analyzeLogs(bufferedReader2);
     * </pre></blockquote>
     * <p>Для повторного использования объекта класса {@code LogAnalysisResult}
     * (т.е. в случаях, когдане требуется считать общую статистику для разных {@code BufferedReader})
     * перед каждым вызовом метода {@code analyzeLogs(BufferedReader)}
     * необходимо заново инициализировать объект с помощью метода
     * {@code init(String, OffsetDateTime, OffsetDateTime)}:
     * <blockquote><pre>
     * LogAnalyzer&lt;LogAnalysisResult&gt; logAnalyzer =
     *             new LogStatisticAnalyzer("GET", OffsetDateTime.MIN, OffsetDateTime.MAX);
     * // проанализировали первый поток
     * LogAnalysisResult result1 = logAnalyzer.analyzeLogs(bufferedReader1);
     * // заново инициализируем
     * logAnalyzer.init("GET", OffsetDateTime.MIN, OffsetDateTime.MAX);
     * // проанализировали второй поток
     * LogAnalysisResult result2 = logAnalyzer.analyzeLogs(bufferedReader2);
     * </pre></blockquote>
     * @param reader  {@code BufferedReader} - поток логов
     * @return запись {@code LogAnalysisResult} с результатом анализа потока логов
     * @see LogStatisticAnalyzer#calculateDynamicValues(String, BufferedReader)
     * @see backend.academy.analyzers.LogAnalysisResult
     * @see MapHandlerHelper#findMaxes(Map)
     * @see MapHandlerHelper#findMins(Map)
     */
    public LogAnalysisResult analyzeLogs(BufferedReader reader) {
        calculateDynamicValues(filter, reader);

        mostFrequentResources = MapHandlerHelper.findMaxes(resourcesCounter);
        mostFrequentResponseStatuses = MapHandlerHelper.findMaxes(responseStatusesCounter);
        lessFrequentResources = MapHandlerHelper.findMins(resourcesCounter);
        lessFrequentResponseStatuses = MapHandlerHelper.findMins(responseStatusesCounter);
        averageResponseSize = numberOfRequests == 0
            ? 0 : sumOfBytesSent.divide(BigInteger.valueOf(numberOfRequests)).intValue();
        percentile = listOfBytesSent.isEmpty()
            ? 0 : Quantiles.percentiles().index(PERCENTILE_PER_CENT).compute(listOfBytesSent);

        result = new LogAnalysisResult(
            numberOfRequests,
            mostFrequentResources,
            mostFrequentResponseStatuses,
            lessFrequentResources,
            lessFrequentResponseStatuses,
            averageResponseSize,
            percentile,
            sumOfBytesSent);

        return result;
    }

    /**
     * <p>Динамически рассчитывает величины, необходимые для подсчета статистики
     * <p>Рассчитываемые величины:
     * <ul>
     *     <li>{@code numberOfRequests} - Количество запросов (количество логов)</li>
     *     <li>{@code resourcesCounter} - Количество запросов к определенным ресурсам</li>
     *     <li>{@code responseStatusesCounter} - Количество кодов ответа</li>
     *     <li>{@code sumOfBytesSent} - Сумма отправленных байт</li>
     *     <li>{@code listOfBytesSent} - Список размеров ответов в байтах (для подсчета перцентиля)</li>
     * </ul>
     * @param filter  {@code String} - фильтр - ключевое слово, которое должно содержаться в логе
     * @see LogStatisticAnalyzer#checkTime(OffsetDateTime)
     * @see NGINXLogParser#parse(String)
     */
    protected void calculateDynamicValues(String filter, BufferedReader reader) {
        try {
            String log;
            while ((log = reader.readLine()) != null) {
                LogRecord logRecord = NGINXLogParser.parse(log);

                if (checkTime(logRecord.timeLocal()) && log.contains(filter)) {
                    numberOfRequests += 1;

                    // для расчета наиболее часто запрашиваемых ресурсов
                    String resource = logRecord.resource();
                    resourcesCounter.merge(
                        resource,
                        1,
                        Integer::sum);

                    // для расчета наиболее часто встречающихся кодов ответа
                    int status = logRecord.status();
                    responseStatusesCounter.merge(
                        status,
                        1,
                        Integer::sum);

                    // для расчета среднего размера ответа сервера
                    sumOfBytesSent = sumOfBytesSent.add(BigInteger.valueOf(logRecord.bodyBytesSent()));

                    // для расчета 95% перцентиля размера ответа сервера
                    listOfBytesSent.add(logRecord.bodyBytesSent());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Ошибка при чтении файла", e);
        }
    }

    /**
     * Задает фильтр по ключевому слову, минимальное допустимое время, максимально допустимое время
     * и обнуляет все поля
     * @param filter {@code String} - фильтр по ключевому слову
     * @param from {@code OffsetDateTime} - минимальное время (не включительно)
     * @param to {@code OffsetDateTime} - максмальное время (не включительно)
     */
    public void init(String filter, OffsetDateTime from, OffsetDateTime to) {
        this.filter = filter;
        this.from = from;
        this.to = to;

        numberOfRequests = 0;
        mostFrequentResources = new HashMap<>();
        mostFrequentResponseStatuses = new HashMap<>();
        lessFrequentResources = new HashMap<>();
        lessFrequentResponseStatuses = new HashMap<>();
        averageResponseSize = 0;
        percentile = 0;

        resourcesCounter = new HashMap<>();
        responseStatusesCounter = new HashMap<>();
        sumOfBytesSent = BigInteger.ZERO;
        listOfBytesSent = new ArrayList<>();
    }

    /**
     * Проверяет, что переданное в параметрах время находится
     * в заданном при инициализации временном диапазоне
     * @param time {@code OffsetDateTime}
     * @return {@code true} если переданное в параметрах время находится
     * в заданном при инициализации временном диапазоне
     * {@code false} если не находится
     */
    protected boolean checkTime(OffsetDateTime time) {
        return (time.isAfter(from) && (time.isBefore(to)));
    }

    /**
     * <p>Инициализирует новый {@code LogStatisticAnalyzer} с фильтрами по ключевому слову и по времени
     * @param filter {@code String} - фильтр по ключевому слову
     * @param from {@code OffsetDateTime} - минимальное время (не включительно)
     * @param to {@code OffsetDateTime} - максмальное время (не включительно)
     * @see LogStatisticAnalyzer#init(String, OffsetDateTime, OffsetDateTime)
     */
    public LogStatisticAnalyzer(String filter, OffsetDateTime from, OffsetDateTime to) {
        init(filter, from, to);
    }
}
