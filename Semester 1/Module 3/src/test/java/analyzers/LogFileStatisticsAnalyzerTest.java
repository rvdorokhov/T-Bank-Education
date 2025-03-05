package analyzers;

import backend.academy.analyzers.LogAnalysisResult;
import backend.academy.analyzers.LogFileStatisticsAnalyzer;
import backend.academy.analyzers.LogStatisticAnalyzer;
import backend.academy.analyzers.interfaces.LogAnalyzer;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogFileStatisticsAnalyzerTest {
    @Test
    @DisplayName("happy-path - все задано и все есть")
    void analyzeTest1() {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        String path = "src/main/resources/test/test1.txt";
        LogAnalysisResult result = fileAnalyzer.analyze(path);

        LogAnalysisResult waitFor = new LogAnalysisResult(
            16,
            new HashMap<>(Map.of(
                "/downloads/product_1", 9,
                "/downloads/product_2", 4,
                "/downloads/product_4", 2)),
            new HashMap<>(Map.of(
                304, 6,
                200, 3,
                298, 3)),
            new HashMap<>(Map.of(
                "/downloads/product_5", 1,
                "/downloads/product_2", 4,
                "/downloads/product_4", 2)),
            new HashMap<>(Map.of(
                299, 1,
                404, 2,
                405, 1)),
            973,
            3316,
            BigInteger.valueOf(15582));

        assertEquals(waitFor, result);
    }

    @Test
    @DisplayName("Случай когда всего ресурсов и статусов меньше 3")
    void analyzeTest2() {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        String path = "src/main/resources/test/test2.txt";
        LogAnalysisResult result = fileAnalyzer.analyze(path);

        LogAnalysisResult waitFor = new LogAnalysisResult(
            16,
            new HashMap<>(Map.of(
                "/downloads/product_1", 11,
                "/downloads/product_2", 5)),
            new HashMap<>(Map.of(
                304, 10,
                200, 6)),
            new HashMap<>(Map.of(
                "/downloads/product_1", 11,
                "/downloads/product_2", 5)),
            new HashMap<>(Map.of(
                304, 10,
                200, 6)),
            973,
            3316,
            BigInteger.valueOf(15582));

        assertEquals(waitFor, result);
    }

    @Test
    @DisplayName("Анализ пустого файла")
    void analyzeTest3() {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        String path = "src/main/resources/test/test3.txt";
        LogAnalysisResult result = fileAnalyzer.analyze(path);

        LogAnalysisResult waitFor = new LogAnalysisResult(
            0,
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            0,
            0,
            BigInteger.valueOf(0));

        assertEquals(waitFor, result);
    }

    @Test
    @DisplayName("Чтение удаленного ресурса")
    void analyzeTest4() {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        String uri = "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs";
        LogAnalysisResult result = fileAnalyzer.analyze(uri);

        LogAnalysisResult waitFor = new LogAnalysisResult(
            51462,
            new HashMap<>(Map.of(
                "/downloads/product_2", 21104,
                "/downloads/product_1", 30285,
                "/downloads/product_3", 73)),
            new HashMap<>(Map.of(
                304, 13330,
                404, 33876,
                200, 4028)),
            new HashMap<>(Map.of(
                "/downloads/product_3", 73,
                "/downloads/product_1", 30285,
                "/downloads/product_2", 21104)),
            new HashMap<>(Map.of(
                416, 4,
                403, 38,
                206, 186)),
            659509,
            1768,
            new BigInteger("33939678630"));

        assertEquals(waitFor, result);
    }


    @Test
    @DisplayName("Тестирование фильтра по ключевому слову")
    void analyzeTest5() {
        String filter = "product_2";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        String path = "src/main/resources/test/test2.txt";
        LogAnalysisResult result = fileAnalyzer.analyze(path);

        LogAnalysisResult waitFor = new LogAnalysisResult(
            5,
            new HashMap<>(Map.of(
                "/downloads/product_2", 5)),
            new HashMap<>(Map.of(
                304, 2,
                200, 3)),
            new HashMap<>(Map.of(
                "/downloads/product_2", 5)),
            new HashMap<>(Map.of(
                304, 2,
                200, 3)),
            1559,
            3316,
            BigInteger.valueOf(7796));

        assertEquals(waitFor, result);
    }

    @Test
    @DisplayName("Тестирование фильтра по времени")
    void analyzeTest6() {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.of(2015, 5, 17, 8, 5, 22, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2015, 5, 17, 8, 5, 25, 0, ZoneOffset.UTC);
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        String path = "src/main/resources/test/test1.txt";
        LogAnalysisResult result = fileAnalyzer.analyze(path);

        LogAnalysisResult waitFor = new LogAnalysisResult(
            2,
            new HashMap<>(Map.of(
                "/downloads/product_1", 2)),
            new HashMap<>(Map.of(
                304, 2)),
            new HashMap<>(Map.of(
                "/downloads/product_1", 2)),
            new HashMap<>(Map.of(
                304, 2)),
            0,
            0,
            BigInteger.valueOf(0));

        assertEquals(waitFor, result);
    }
}
