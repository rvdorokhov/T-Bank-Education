package backend.academy.helpers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Статический класс-хэлпер, отвечающий за чтение конфигурационных файлов
 */
@UtilityClass
public class ConfigOpenHelper {
    private static final Logger LOGGER = LogManager.getLogger(ConfigOpenHelper.class);

    /**
     * <p> Открывает конфиг-файл с выводом для формата {@code format} на языке {@code language}
     * и возвращает объект класса {@code Properties}, хранящий результат чтения конфига-файла
     * <p> Поддерживаемые форматы: markdown, adoc
     * <p> Поддерживаемые языки: ru
     * <p>Конфигурационные файлы, описывающие поддерживаемые форматы и языки, хранятся в <i>main/resources</i>
     * @param format {@code String} - формат вывода
     * @param language {@code String} - язык вывода
     * @return объект класса {@code Properties}, хранящий результат чтения конфига-файла
     * указанного в параметрах формата и языка
     */
    public static Properties getLocale(String format, String language) {
        Properties properties = new Properties();
        String path = format + "." + language;

        try (Reader input = new InputStreamReader(
            ConfigOpenHelper.class.getResourceAsStream("/locale." + path),
            StandardCharsets.UTF_8)) {
            properties.load(input);
        } catch (NullPointerException e) {
            LOGGER.error("Не найден файл конфигурации locale.{}", path, e);
        } catch (IOException e) {
            LOGGER.error("Ошибка при чтении файла конфигурации locale.{}", path, e);
        }

        return properties;
    }
}
