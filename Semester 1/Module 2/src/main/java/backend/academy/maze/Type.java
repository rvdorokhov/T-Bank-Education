package backend.academy.maze;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public enum Type {
    WALL(),
    PASSAGE(),
    COIN(),
    GUARDIAN();

    private final String localeName;
    private final String description;
    private final int cost;

    Type() {
        Properties descriptions = new Properties();

        Logger logger = LogManager.getLogger(Type.class);

        try (Reader input = new InputStreamReader(
            getClass().getResourceAsStream("/configs/typeofpassages.description"),
            StandardCharsets.UTF_8)) {
            descriptions.load(input);
        } catch (NullPointerException e) {
            logger.fatal("Не найден файл конфигурации с темами, словами и подсказками к ним", e);
        } catch (IOException e) {
            logger.fatal("Ошибка чтения файла конфигурации", e);
        }

        this.localeName = descriptions.getProperty(this.name().toLowerCase() + ".name");
        this.description = descriptions.getProperty(this.name().toLowerCase() + ".description");
        this.cost = Integer.parseInt(descriptions.getProperty(this.name().toLowerCase() + ".cost"));
    }
}
