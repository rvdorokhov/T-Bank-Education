package backend.academy.parser;

import java.time.OffsetDateTime;

/**
 * Представление лога в качестве записи
 * <p> Схема лога:
 * <blockquote><pre>
 * '$remote_addr - $remote_user [$time_local] ' '"$request"
 * $status $body_bytes_sent ' '"$http_referer" "$http_user_agent"'
 * </pre></blockquote>
 * <p> Пример лога:
 * <blockquote><pre>
 * 93.180.71.3 - - [17/May/2015:08:05:32 +0000] "GET /downloads/product_1 HTTP/1.1"
 * 304 0 "-" "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)"
 * </pre></blockquote>
 * @param remoteAddr {@code String}
 * @param remoteUser {@code String}
 * @param timeLocal {@code OffsetDateTime} - Время записи
 * @param request {@code String} - Текст запроса
 * @param httpMethod {@code String} - http-метод
 * @param resource {@code String} - Запрашиваемый ресурс
 * @param protocol {@code String} - Используемый протокол передачи
 * @param status {@code int} - Статус ответа
 * @param bodyBytesSent {@code int} - Сколько байт было отправлено
 * @param httpReferer {@code String}
 * @param httpUserAgent {@code String}
 */
@SuppressWarnings("RecordComponentNumber")
public record LogRecord(
    String remoteAddr,
    String remoteUser,
    OffsetDateTime timeLocal,
    String request,
    String httpMethod,
    String resource,
    String protocol,
    int status,
    int bodyBytesSent,
    String httpReferer,
    String httpUserAgent
) {}
