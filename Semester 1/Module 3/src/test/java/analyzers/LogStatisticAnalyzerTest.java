package analyzers;

import backend.academy.analyzers.interfaces.LogAnalyzer;
import backend.academy.analyzers.LogAnalysisResult;
import backend.academy.analyzers.LogStatisticAnalyzer;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogStatisticAnalyzerTest {
    @Test
    @DisplayName("happy-path - все задано и все есть")
    void analyzeTest1() throws IOException {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        Path path = Path.of("src", "main", "resources", "test", "test1.txt");
        BufferedReader reader = Files.newBufferedReader(path);
        LogAnalysisResult result = logAnalyzer.analyzeLogs(reader);

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
    void analyzeTest2() throws IOException {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        Path path = Path.of("src", "main", "resources", "test", "test2.txt");
        BufferedReader reader = Files.newBufferedReader(path);
        LogAnalysisResult result = logAnalyzer.analyzeLogs(reader);

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
    void analyzeTest3() throws IOException {
        String filter = "";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        Path path = Path.of("src", "main", "resources", "test", "test3.txt");
        BufferedReader reader = Files.newBufferedReader(path);
        LogAnalysisResult result = logAnalyzer.analyzeLogs(reader);

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
    @DisplayName("Тестирование фильтра по ключевому слову")
    void analyzeTest4() throws IOException {
        String filter = "product_2";
        OffsetDateTime from = OffsetDateTime.MIN;
        OffsetDateTime to = OffsetDateTime.MAX;
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        Path path = Path.of("src", "main", "resources", "test", "test2.txt");
        BufferedReader reader = Files.newBufferedReader(path);
        LogAnalysisResult result = logAnalyzer.analyzeLogs(reader);

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
        LogAnalyzer<LogAnalysisResult> logAnalyzer = new LogStatisticAnalyzer(filter, from, to);
        Path path = Path.of("src", "main", "resources", "test", "test1.txt");
        BufferedReader reader = Files.newBufferedReader(path);
        LogAnalysisResult result = logAnalyzer.analyzeLogs(reader);

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
