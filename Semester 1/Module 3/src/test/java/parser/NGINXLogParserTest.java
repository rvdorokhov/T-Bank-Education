package parser;

import backend.academy.parser.NGINXLogParser;
import backend.academy.parser.LogRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NGINXLogParserTest {
    @Test
    @DisplayName("Все поля заполнены")
    void parseTest1() {
        String log = "93.180.71.3 - 93.180.72.3 [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" " +
            "304 0 \"https://test.com/\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"";

        LogRecord logRecord = NGINXLogParser.parse(log);

        assertAll(
            () -> assertEquals("93.180.71.3", logRecord.remoteAddr()),
            () -> assertEquals("93.180.72.3", logRecord.remoteUser()),
            () -> assertEquals(OffsetDateTime.of(2015, 5, 17, 8, 5, 32, 0, ZoneOffset.UTC), logRecord.timeLocal()),
            () -> assertEquals("GET /downloads/product_1 HTTP/1.1", logRecord.request()),
            () -> assertEquals(304, logRecord.status()),
            () -> assertEquals(0, logRecord.bodyBytesSent()),
            () -> assertEquals("https://test.com/", logRecord.httpReferer()),
            () -> assertEquals("Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)", logRecord.httpUserAgent())
        );
    }

    @Test
    @DisplayName("Ни одно поле не заполнено")
    void parseTest2() {
        String log = "- - - - - " +
            "- - - -\n";

        LogRecord logRecord = NGINXLogParser.parse(log);

        assertAll(
            () -> assertEquals("-", logRecord.remoteAddr()),
            () -> assertEquals("-", logRecord.remoteUser()),
            () -> assertEquals(OffsetDateTime.of(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC), logRecord.timeLocal()),
            () -> assertEquals("-", logRecord.request()),
            () -> assertEquals(0, logRecord.status()),
            () -> assertEquals(0, logRecord.bodyBytesSent()),
            () -> assertEquals("-", logRecord.httpReferer()),
            () -> assertEquals("-", logRecord.httpUserAgent())
        );
    }

    @Test
    @DisplayName("Некоторые поля заполнены")
    void parseTest3() {
        String log = "217.168.17.5 - - - " +
            "\"GET /downloads/product_2 HTTP/1.1\" 200 3316 \"-\" \"-\"";

        LogRecord logRecord = NGINXLogParser.parse(log);

        assertAll(
            () -> assertEquals("217.168.17.5", logRecord.remoteAddr()),
            () -> assertEquals("-", logRecord.remoteUser()),
            () -> assertEquals(OffsetDateTime.of(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC), logRecord.timeLocal()),
            () -> assertEquals("GET /downloads/product_2 HTTP/1.1", logRecord.request()),
            () -> assertEquals(200, logRecord.status()),
            () -> assertEquals(3316, logRecord.bodyBytesSent()),
            () -> assertEquals("-", logRecord.httpReferer()),
            () -> assertEquals("-", logRecord.httpUserAgent())
        );
    }
}
