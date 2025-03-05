package solvers;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import backend.academy.maze.Type;
import backend.academy.solvers.DijkstraSolver;
import backend.academy.solvers.Path;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DijkstraSolverTest {
    private static Logger logger;
    private static Properties settings;

    private static Maze maze;
    private static DijkstraSolver solver;

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
        solver = new DijkstraSolver();
    }


    @Test
    void solveTest1() {
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(7, 7);

        Path result = solver.solve(maze, start, end);

        assertEquals(58, result.time()); // >= 0 значит, что он нашел такой путь
    }

    @Test
    void solveTest2() {
        Coordinate start = new Coordinate(3, 3);
        Coordinate end = new Coordinate(3, 5);

        Path result = solver.solve(maze, start, end);

        assertEquals(53, result.time()); // >= 0 значит, что он нашел такой путь
    }

    @Test
    void solveTestNoWay() {
        Coordinate start = new Coordinate(7, 7);
        Coordinate end = new Coordinate(2, 2);

        Path result = solver.solve(maze, start, end);

        assertEquals(0, result.time());
    }
}
