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
 * Вспомогательный класс, предоставляющий данные из конфигурационных файлов
 */
@UtilityClass
public class ConfigOpen {
    private static final Logger LOGGER = LogManager.getLogger(ConfigOpen.class);

    /**
     * <p>Предоставляет данные из конфига prints, в котором содержатся сообщения,
     * используемые в интерфейсе для взаимодействия с пользователем</p>
     * <p>Конфиг находится по адресу: src/main/resources/prints</p>
     * @return объект {@code Properties} с данными конфига prints
     */
    public static Properties getPrints() {
        Properties properties = new Properties();

        try (Reader input = new InputStreamReader(
            ConfigOpen.class.getResourceAsStream("/prints"),
            StandardCharsets.UTF_8)) {
            properties.load(input);
        } catch (NullPointerException e) {
            LOGGER.error("Configuration file \"prints\" not found", e);
        } catch (IOException e) {
            LOGGER.error("Error reading configuration file \"prints\"", e);
        }

        return properties;
    }
}
