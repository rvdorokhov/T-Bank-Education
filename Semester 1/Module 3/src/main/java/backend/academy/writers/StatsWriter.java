package backend.academy.writers;

import backend.academy.analyzers.LogAnalysisResult;
import backend.academy.helpers.ConfigOpenHelper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>Класс {@code StatsWriter} предназначен для вывода записей {@code LogAnalysisResult}
 * в поток {@code OutputStreamWriter}
 * <p>Поддерживаемые форматы вывода: markdown, adoc
 * @see LogAnalysisResult
 */
public class StatsWriter {
    private static final Logger LOGGER = LogManager.getLogger(StatsWriter.class);

    protected OutputStreamWriter outputStream;

    private final Properties locale;
    private final String format;

    private final String header;
    private final String numberOfRequestsText;
    private final String averageResponseSizeText;
    private final String percentileText;
    private final String totalDataSentText;

    private final String mostFrequentResources;
    private final String mostFrequentStatuses;
    private final String lessFrequentResources;
    private final String lessFrequentStatuses;

    /**
     * <p>Осуществляет вывод переданного в параметрах {@code LogAnalysisResult}
     * в поток {@code OutputStreamWriter}</p>
     * @param   logRecord  {@code LogAnalysisResult}
     * @param   outputStream  {@code OutputStreamWriter}
     * @see LogAnalysisResult
     * @see StatsWriter#writeData(String)
     * @see StatsWriter#writeMap(Map)
     */
    public void write(LogAnalysisResult logRecord, OutputStreamWriter outputStream) {
        this.outputStream = outputStream;
        String numberOfRequests = String.valueOf(logRecord.numberOfRequests());
        String averageResponseSize = String.valueOf(logRecord.averageResponseSize());
        String percentile = String.valueOf(logRecord.percentile());
        String totalDataSent = String.valueOf(logRecord.totalDataSent());

        try {
            writeData(header);
            outputStream.write(numberOfRequestsText);
            writeData(numberOfRequests);
            outputStream.write(averageResponseSizeText);
            writeData(averageResponseSize);
            outputStream.write(percentileText);
            writeData(percentile);
            outputStream.write(totalDataSentText);
            writeData(totalDataSent);

            writeData(mostFrequentResources);
            writeMap(logRecord.mostFrequentResources());
            writeData(mostFrequentStatuses);
            writeMap(logRecord.mostFrequentResponseStatuses());
            writeData(lessFrequentResources);
            writeMap(logRecord.lessFrequentResources());
            writeData(lessFrequentStatuses);
            writeMap(logRecord.lessFrequentResponseStatuses());
        } catch (IOException e) {
            LOGGER.error("Ошибка при записи в файл", e);
        }
    }

    /**
     * <p>Осуществляет вывод переданного в параметрах {@code Map<K, V>}
     * в поток {@code OutputStreamWriter}</p>
     * @param   map  {@code Map<K, V>}
     * @see StatsWriter#writeData(String)
     * @throws IOException при возникновении IO-ошибок
     */
    protected <K, V> void writeMap(Map<K, V> map) throws IOException {
        for (K key : map.keySet()) {
            String dataKey = String.valueOf(key);
            String dataValue = String.valueOf(map.get(key));
            outputStream.write("| ");
            outputStream.write(dataKey);
            outputStream.write(" | ");
            writeData(dataValue);
        }
    }

    /**
     * <p>Осуществляет вывод переданного в параметрах {@code String}
     * в поток {@code OutputStreamWriter}</p>
     * @param   data {@code String}
     * @throws IOException при возникновении IO-ошибок
     */
    protected void writeData(String data) throws IOException {
        outputStream.write(data);
        switch (format) {
            case ("markdown"):
                outputStream.write(" |\n");
                break;
            case ("adoc"):
                outputStream.write("\n");
                break;
            default:
                outputStream.write("\n");
                LOGGER.warn("Неподдерживаемый формат {}", format);
        }
    }

    /**
     * <p>Инициализирует новый {@code StatsWriter} с форматом и языком, переданным в параметрах</p>
     * <p>Поддерживаемые форматы вывода: <i>markdown, adoc</i></p>
     * <p>Конфигурационные файлы, описывающие поддерживаемые форматы, хранятся в <i>main/resources</i></p>
     * @param   format {@code String}
     * @param   language {@code String}
     * @see ConfigOpenHelper#getLocale(String, String)
     */
    public StatsWriter(String format, String language) {
        locale = ConfigOpenHelper.getLocale(format, language);

        header = locale.getProperty("header");
        numberOfRequestsText = locale.getProperty("number.of.requests");
        averageResponseSizeText = locale.getProperty("average.response.size");
        percentileText = locale.getProperty("percentile");
        totalDataSentText = locale.getProperty("total.data.sent");

        mostFrequentResources = locale.getProperty("most.frequent.resources");
        mostFrequentStatuses = locale.getProperty("most.frequent.response.statuses");
        lessFrequentResources = locale.getProperty("less.frequent.resources");
        lessFrequentStatuses = locale.getProperty("less.frequent.response.statuses");

        this.format = format;
    }
}
