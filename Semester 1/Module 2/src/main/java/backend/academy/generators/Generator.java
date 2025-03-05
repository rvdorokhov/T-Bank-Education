package backend.academy.generators;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import backend.academy.maze.Type;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("MagicNumber")
public abstract class Generator {
    protected Cell[][] grid;
    protected final SecureRandom random;
    protected List<Type> typesWithoutWalls;
    protected final Properties descriptions;
    protected final Properties settings;
    protected final Logger logger;
    protected int startY;
    protected int startX;
    protected Maze maze;

    // Выбор случайного типа (не включая стену) для клетки
    protected void chooseType(Coordinate cell) {
        int y = cell.row();
        int x = cell.col();
        int randomTypeInd = random.nextInt(typesWithoutWalls.size());

        grid[y][x].type(typesWithoutWalls.get(randomTypeInd));
    }

    // Выбор начальной точки, из которой будет начинаться генерация проходов
    protected void chooseStartPos(int height, int width) {
        // Пересоздаем startX и startY, пока они оба не будут четными
        // поскольку генераторы не выдают аномалий только в случае стартовой позиции с четными координатами
        do {
            startX = random.nextInt(width);
            startY = random.nextInt(height);
        } while ((startX % 2 == 1) || (startY % 2 == 1));
    }

    public Generator() {
        descriptions = new Properties();
        settings = new Properties();

        logger = LogManager.getLogger(Type.class);

        try (Reader input = new InputStreamReader(
            getClass().getResourceAsStream("/configs/typeofpassages.description"),
            StandardCharsets.UTF_8);
            Reader settingsInput = new InputStreamReader(
            getClass().getResourceAsStream("/configs/settings"),
            StandardCharsets.UTF_8);) {
            descriptions.load(input);
            settings.load(settingsInput);
        } catch (NullPointerException e) {
            logger.fatal("Не найден файл конфигурации", e);
        } catch (IOException e) {
            logger.fatal("Ошибка чтения файла конфигурации", e);
        }

        random = new SecureRandom();

        typesWithoutWalls = new ArrayList<>(List.of(Type.values()));
        typesWithoutWalls.remove(Type.WALL);
    }

    // Сделаем лабиринт не идеальным
    abstract protected void makeMazeNotIdeal();

    abstract public Maze generate(int height, int width);
}
