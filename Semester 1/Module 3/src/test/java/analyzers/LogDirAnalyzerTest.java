package analyzers;

import backend.academy.analyzers.interfaces.LogAnalyzer;
import backend.academy.analyzers.LogAnalysisResult;
import backend.academy.analyzers.LogDirAnalyzer;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import backend.academy.analyzers.LogFileStatisticsAnalyzer;
import backend.academy.analyzers.LogStatisticAnalyzer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LogDirAnalyzerTest {
    @Test
    @DisplayName("Анализ пустой директории")
    void analyzeTest1() throws IOException {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        Path path = Path.of("src", "main", "resources", "test", "EmptyDir");
        String glob = "glob:";
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        LogAnalysisResult result = LogDirAnalyzer.analyze(fileAnalyzer, glob, path);

        assertNull(result);
    }

    @Test
    @DisplayName("Анализ директории с файлами, не соответсвующими glob-выражению")
    void analyzeTest2() throws IOException {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        Path path = Path.of("src", "main", "java");
        String glob = "glob:**/*.txt";
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        LogAnalysisResult result = LogDirAnalyzer.analyze(fileAnalyzer, glob, path);

        assertNull(result);
    }

    @Test
    @DisplayName("happy-path - файлы для на анализа есть в поддиректориях")
    void analyzeTest3() throws IOException {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        Path path = Path.of("src", "main", "resources", "test");
        String glob = "glob:**/test*.txt";
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        LogAnalysisResult result = LogDirAnalyzer.analyze(fileAnalyzer, glob, path);

        LogAnalysisResult waitFor = new LogAnalysisResult(
            48,
            new HashMap<>(Map.of(
                "/downloads/product_1", 29,
                "/downloads/product_2", 13,
                "/downloads/product_4", 4)),
            new HashMap<>(Map.of(
                304, 22,
                200, 12,
                298, 6)),
            new HashMap<>(Map.of(
                "/downloads/product_5", 2,
                "/downloads/product_2", 13,
                "/downloads/product_4", 4)),
            new HashMap<>(Map.of(
                299, 2,
                404, 4,
                405, 2)),
            973,
            3316,
            BigInteger.valueOf(46746));

        assertEquals(waitFor, result);
    }

    @Test
    @DisplayName("Тестирование фильтра по ключевому слову")
    void analyzeTest4() throws IOException {
        String filter = "product_2";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        Path path = Path.of("src", "main", "resources", "test", "test2.txt");
        String glob = "glob:**/test*.txt";
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        LogAnalysisResult result = LogDirAnalyzer.analyze(fileAnalyzer, glob, path);

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
    void analyzeTest5() throws IOException {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.of(2015, 5, 17, 8, 5, 22, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2015, 5, 17, 8, 5, 25, 0, ZoneOffset.UTC);
        Path path = Path.of("src", "main", "resources", "test", "test2.txt");
        String glob = "glob:**/test*.txt";
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        LogFileStatisticsAnalyzer<LogAnalysisResult> fileAnalyzer = new LogFileStatisticsAnalyzer<>(logAnalyzer);
        LogAnalysisResult result = LogDirAnalyzer.analyze(fileAnalyzer, glob, path);

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
