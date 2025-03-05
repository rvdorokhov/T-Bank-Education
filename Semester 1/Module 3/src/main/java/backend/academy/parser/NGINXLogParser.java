package backend.academy.parser;

import backend.academy.helpers.InputCheckHelper;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

/**
 * <p>Класс {@code NGINXLogParser} предназначен для парсинга
 * переданных в качестве {@code String} логов в запись {@code LogRecord}.
 * <p> Схема лога:
 * <blockquote><pre>
 * '$remote_addr - $remote_user [$time_local] ' '"$request" $status
 * $body_bytes_sent ' '"$http_referer" "$http_user_agent"'
 * </pre></blockquote>
 * <p> Пример лога:
 * <blockquote><pre>
 * 93.180.71.3 - - [17/May/2015:08:05:32 +0000] "GET /downloads/product_1 HTTP/1.1"
 * 304 0 "-" "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)"
 * </pre></blockquote>
 * @see LogRecord
 */
@UtilityClass
public class NGINXLogParser {
    private final static int REMOTE_ADDR_INDEX = 0;
    private final static int REMOTE_USER_INDEX = 2;
    private final static int TIME_LOCAL_INDEX = 3;
    private final static int REQUEST_INDEX = 4;
    private final static int STATUS_INDEX = 5;
    private final static int BODY_BYTES_SENT_INDEX = 6;
    private final static int HTTP_REFERER_INDEX = 7;
    private final static int HTTP_USER_AGENT_INDEX = 8;

    private final static int HTTP_METHOD_INDEX = 0;
    private final static int RESOURCE_INDEX = 1;
    private final static int PROTOCOL_INDEX = 2;

    /**
     * <p> Каждое поле лога разделяется разными символами - например
     * время заключается в квадратные скобки []:
     * <blockquote><pre>
     * [17/ May/ 2015:08:05:32 +0000]
     * </pre></blockquote>
     * а запрос в двойные кавычки "":
     * <blockquote><pre>
     * "GET / downloads/ product_1 HTTP/ 1.1"
     * </pre></blockquote>
     * <p> {@code SPLITTERS_MAP} хранит пары <i>открывающий символ - закрывающий символ</i>
     * для таких символов-разделителей
     */
    private static final Map<Character, Character> SPLITTERS_MAP = new HashMap<>(Map.of(
        '[', ']',
        '\"', '\"',
        ' ', ' '
    ));

    /**
     * Проверяет, что если символ {@code chr} является разделителем, то он не будет в строке {@code str} первым
     * @param chr  {@code char}
     * @param str  {@code str}
     * @see NGINXLogParser#SPLITTERS_MAP
     */
    private static boolean checkBelongsSplitters(char chr, StringBuilder str) {
        return !(str.isEmpty() && SPLITTERS_MAP.containsKey(chr));
    }

    /**
     * <p> Проверяет, что символ {@code chr} является закрывающим символом {@code splitter} или концом строки
     * @param chr  {@code char}
     * @param splitter  {@code str} - закрывающий символ
     * @see NGINXLogParser#SPLITTERS_MAP
     */
    private static boolean isSplitter(char chr, char splitter) {
        return (chr == splitter) || (chr == '\n');
    }

    /**
     * Собирает {@code LogRecord} из переданного в параметрах {@code List<String>}
     * @param log {@code List<String>}
     * @see LogRecord
     * @see InputCheckHelper#checkDateTime(String)
     * @see InputCheckHelper#checkInteger(String)
     * @return {@code LogRecord}, построенный из параметра {@code log}
     */
    private static LogRecord logRecordBuilder(List<String> log) {
        String remoteAddr = log.get(REMOTE_ADDR_INDEX);
        String remoteUser = log.get(REMOTE_USER_INDEX);
        OffsetDateTime timeLocal = InputCheckHelper.checkDateTime(log.get(TIME_LOCAL_INDEX));

        String request = log.get(REQUEST_INDEX);
        String httpMethod = "-";
        String resource = "-";
        String protocol = "-";
        String[] splittedRequest = request.split(" ");
        if (!request.equals("-")) { // проверка на то что request был задан в логе
            httpMethod = splittedRequest[HTTP_METHOD_INDEX];
            resource = splittedRequest[RESOURCE_INDEX];
            protocol = splittedRequest[PROTOCOL_INDEX];
        }

        int status = InputCheckHelper.checkInteger(log.get(STATUS_INDEX));
        int bodyBytesSent = InputCheckHelper.checkInteger(log.get(BODY_BYTES_SENT_INDEX));
        String httpReferer = log.get(HTTP_REFERER_INDEX);
        String httpUserAgent = log.get(HTTP_USER_AGENT_INDEX);

        return new LogRecord(
            remoteAddr,
            remoteUser,
            timeLocal,
            request,
            httpMethod,
            resource,
            protocol,
            status,
            bodyBytesSent,
            httpReferer,
            httpUserAgent
        );
    }

    /**
     * Разделяет переданный в параметрах {@code log} по его полям. Например
     *  <blockquote><pre>
     *  93.180.71.3 - - [17/May/2015:08:05:32 +0000] "GET /downloads/product_1 HTTP/1.1"
     *  304 0 "-" "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)"
     *  </pre></blockquote>
     *  преобразуется в
     *  <blockquote><pre>
     *  ["93.180.71.3", "-", "-", "17/May/2015:08:05:32 +0000", "GET /downloads/product_1 HTTP/1.1",
     *  "304", "0", "-", "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)"]
     *  </pre></blockquote>
     * @param log  {@code String}
     * @see NGINXLogParser#SPLITTERS_MAP
     * @see NGINXLogParser#isSplitter(char, char)
     * @see NGINXLogParser#checkBelongsSplitters(char, StringBuilder)
     * @return {@code List<String>} - результат разделение лога по его полям
     */
    private static List<String> splitLog(String log) {
        List<String> splittingLog = new ArrayList<>();

        char splitter = SPLITTERS_MAP.get(' ');
        StringBuilder str = new StringBuilder();
        for (int index = 0; index < log.length(); ++index) {
            char curChar = log.charAt(index);
            if (!isSplitter(curChar, splitter) && checkBelongsSplitters(curChar, str)) {
                str.append(curChar);
            } else {
                if (!str.isEmpty()) {
                    splittingLog.add(str.toString());
                    str.delete(0, str.length());
                }

                char nextChar = index + 1 == log.length() ? ' ' : log.charAt(index + 1);
                if (SPLITTERS_MAP.containsKey(nextChar)) {
                    splitter = SPLITTERS_MAP.get(nextChar);
                }
            }
        }

        return splittingLog;
    }

    /**
     * API - возвращает {@code LogRecord}, полученный из переданного в параметрах лога класса {@code String}
     * @param log {@code String}
     * @return {@code Logrecord}, полученный из переданного в параметрах лога класса {@code String}
     */
    public static LogRecord parse(String log) {
        List<String> splittedLog = splitLog(log);
        return logRecordBuilder(splittedLog);
    }
}
