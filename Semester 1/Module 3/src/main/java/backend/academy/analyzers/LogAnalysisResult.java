package backend.academy.analyzers;

import backend.academy.parser.LogRecord;
import java.math.BigInteger;
import java.util.Map;

/**
 * Результат анализа лога в качестве записи
 * @param numberOfRequests  {@code long} - Количество запросов (количество логов)
 * @param mostFrequentResources  {@code Map<String, Integer>} - Самые часто запрашиваемые ресурсы
 * @param mostFrequentResponseStatuses  {@code Map<Integer, Integer>} - Самые частые коды ответа
 * @param lessFrequentResources  {@code Map<String, Integer>} - Самые редко запрашиваемые ресурсы
 * @param lessFrequentResponseStatuses  {@code Map<Integer, Integer>} - Самые редкие коды ответа
 * @param averageResponseSize  {@code double} - Средний размер ответа
 * @param percentile  {@code double} - Перцентиль
 * @param totalDataSent  {@code BigInteger} - Сколько всего байт было отправлено
 * @see LogRecord
 */
public record LogAnalysisResult(
    long numberOfRequests,
    Map<String, Integer> mostFrequentResources,
    Map<Integer, Integer> mostFrequentResponseStatuses,
    Map<String, Integer> lessFrequentResources,
    Map<Integer, Integer> lessFrequentResponseStatuses,
    double averageResponseSize,
    double percentile,
    BigInteger totalDataSent
) {}
