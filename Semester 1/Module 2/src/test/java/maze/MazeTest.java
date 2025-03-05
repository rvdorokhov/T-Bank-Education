package maze;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import backend.academy.maze.Type;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MazeTest {
    private static Logger logger;
    private static Properties settings;

    private static Maze maze;

    private static int WIDTH;
    private static int HEIGHT;
    private static String[] GRID;
    private static Cell[][] grid;

    private static final String WIDTH_CONFIG = "width.test";
    private static final String HEIGHT_CONFIG = "height.test";
    private static final String GRID_CONFIG = "grid.test";

    @BeforeAll // Октрытие файла конфигурации
    static void openConfig() throws Exception {
        logger = LogManager.getLogger(Type.class);
        settings = new Properties();

        try (Reader input = new InputStreamReader(
            Maze.class.getClassLoader()
                .getResourceAsStream("configs/settings"),
            StandardCharsets.UTF_8)) {
            settings.load(input);
        } catch (NullPointerException e) {
            logger.fatal("Не найден файл конфигурации", e);
        } catch (IOException e) {
            logger.fatal("Ошибка чтения файла конфигурации", e);
        }

        WIDTH = Integer.parseInt(settings.getProperty(WIDTH_CONFIG));
        HEIGHT = Integer.parseInt(settings.getProperty(HEIGHT_CONFIG));

        GRID = new String[HEIGHT * WIDTH];
        GRID = settings.getProperty(GRID_CONFIG).split(",").clone();
        Cell[][] grid = new Cell[HEIGHT][WIDTH];
        for (int row = 0; row < HEIGHT; ++row) {
            for (int col = 0; col < WIDTH; ++col) {
                Type type = Type.valueOf(GRID[WIDTH * row + col]);
                grid[row][col] = new Cell(type);
            }
        }

        maze = new Maze(WIDTH, HEIGHT, grid);
    }

    @Test
    void checkCellTestCorrect1() {
        Coordinate check = new Coordinate(1, 1);

        boolean result = maze.checkCell(check);

        assertTrue(result);
    }

    @Test
    void checkCellTestCorrect2() {
        Coordinate check = new Coordinate(3, 3);

        boolean result = maze.checkCell(check);

        assertTrue(result);
    }

    @Test
    void checkCellTestCorrect3() {
        Coordinate check = new Coordinate(1, 7);

        boolean result = maze.checkCell(check);

        assertTrue(result);
    }

    @Test
    void checkCellTestIncorrect1() {
        Coordinate check = new Coordinate(-1, 7);

        boolean result = maze.checkCell(check);

        assertFalse(result);
    }

    @Test
    void checkCellTestIncorrect2() {
        Coordinate check = new Coordinate(1, -7);

        boolean result = maze.checkCell(check);

        assertFalse(result);
    }

    @Test
    void checkCellTestIncorrect4() {
        Coordinate check = new Coordinate(1, 10);

        boolean result = maze.checkCell(check);

        assertFalse(result);
    }

    @Test
    void checkCellTestIncorrect5() {
        Coordinate check = new Coordinate(10, 1);

        boolean result = maze.checkCell(check);

        assertFalse(result);
    }

    @Test
    void checkCellTestIncorrect6() {
        Coordinate check = new Coordinate(0, 1);

        boolean result = maze.checkCell(check);

        assertFalse(result);
    }

    @Test
    void checkCellTestIncorrect7() {
        Coordinate check = new Coordinate(1, 0);

        boolean result = maze.checkCell(check);

        assertFalse(result);
    }

    @Test
    void checkCellTestIncorrect8() {
        Coordinate check = new Coordinate(1, 8);

        boolean result = maze.checkCell(check);

        assertFalse(result);
    }

    @Test
    void checkCellTestIncorrect9() {
        Coordinate check = new Coordinate(8, 1);

        boolean result = maze.checkCell(check);

        assertFalse(result);
    }
}
