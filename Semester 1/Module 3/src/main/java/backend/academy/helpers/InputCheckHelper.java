package backend.academy.helpers;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Статический класс-хэлпер, безопасно (т.е. без выброса ошибок) преобразующий
 * переданную в формате {@code String} строку в объект класса {@code OffsetDateTime} или {@code Integer}
 */
@UtilityClass
public class InputCheckHelper {
    private static final Logger LOGGER = LogManager.getLogger(InputCheckHelper.class);

    /**
     * Конвертирует переданную в параметрах строку в объект класса {@code OffsetDateTime}
     * или {@code OffsetDateTime.of(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)}
     * в случае если переданную строку нельзя преобразовать в {@code OffsetDateTime}
     * @param str  {@code String}
     * @return объект класса {@code OffsetDateTime} переданной в параметрах строки
     * или {@code OffsetDateTime.of(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)}
     * в случае если переданную строку нельзя преобразовать в {@code OffsetDateTime}
     */
    public static OffsetDateTime checkDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        OffsetDateTime result;
        try {
            result = OffsetDateTime.parse(str, formatter);
        } catch (DateTimeParseException e) {
            LOGGER.warn("Некорректный формат даты и времени", e);
            result = OffsetDateTime.of(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        }

        return result;
    }

    /**
     * Конвертирует переданную в параметрах строку в объект класса {@code Integer}
     * или {@code 0} в случае если переданную строку нельзя преобразовать в {@code Integer}
     * @param str  {@code String}
     * @return объект класса {@code Integer} переданной в параметрах строки
     * или {@code 0} в случае если переданную строку нельзя преобразовать в {@code Integer}
     */
    public static int checkInteger(String str) {
        int result;
        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            LOGGER.warn("Некорректный ввод числового значения", e);
            result = 0;
        }

        return result;
    }
}
