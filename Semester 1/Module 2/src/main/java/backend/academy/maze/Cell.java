package backend.academy.maze;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cell {
    @Getter protected Type type;
    @Getter @Setter protected char visualization;

    Properties descriptions;

    public Cell(Type type) {
        descriptions = new Properties();

        Logger logger = LogManager.getLogger(Cell.class);

        try (Reader input = new InputStreamReader(
            getClass().getResourceAsStream("/configs/typeofpassages.description"),
            StandardCharsets.UTF_8)) {
            descriptions.load(input);
        } catch (NullPointerException e) {
            logger.fatal("Не найден файл конфигурации с темами, словами и подсказками к ним", e);
        } catch (IOException e) {
            logger.fatal("Ошибка чтения файла конфигурации", e);
        }

        type(type);
    }

    public void type(Type type) {
        this.type = type;
        this.visualization = descriptions.getProperty(
            type.name().toLowerCase() + ".visualization").charAt(0);
    }
}
