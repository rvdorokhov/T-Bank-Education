package backend.academy.helpers;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Статический класс-хэлпер, безопасно (т.е. без выброса ошибок) преобразующий
 * переданную в формате {@code String} строку в объект класса {@code Path} или {@code URI}
 */
@UtilityClass
public class ResourceCheckHelper {
    private static final Logger LOGGER = LogManager.getLogger(ResourceCheckHelper.class);

    /**
     * Конвертирует переданную в параметрах строку в объект класса {@code URI}
     * или {@code null} в случае если переданную строку нельзя преобразовать в {@code URI}
     * @param str  {@code String}
     * @return объект класса {@code URI} переданной в параметрах строки
     * или {@code null} в случае если переданную строку нельзя преобразовать в {@code URI}
     */
    public static URI checkURI(String str) {
        try {
            URI uri = new URI(str);
            return uri.getScheme() != null ? uri : null;
        } catch (URISyntaxException e) {
            LOGGER.info("Ресурс {} не является URI-ресурсом", str);
            return null;
        }
    }

    /**
     * Конвертирует переданную в параметрах строку в объект класса {@code Path}
     * или {@code null} в случае если переданную строку нельзя преобразовать в {@code Path}
     * @param str  {@code String}
     * @return объект класса {@code Path} переданной в параметрах строки
     * или {@code null} в случае если переданную строку нельзя преобразовать в {@code Path}
     */
    public static Path checkPath(String str) {
        try {
            if (checkURI(str) == null) {
                return Paths.get(str);
            }
            LOGGER.info("Ресурс {} является удаляенным ресурсом", str);
            return null;
        } catch (InvalidPathException e) {
            LOGGER.info("Ресурс {} не является локальным ресурсом", str);
            return null;
        }
    }
}
