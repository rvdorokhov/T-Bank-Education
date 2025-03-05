package backend.academy.game;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import backend.academy.maze.Type;
import backend.academy.solvers.Path;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Printer implements Printable {
    protected final Properties prints;
    protected final Properties descriptions;
    protected final Logger logger;
    protected final PrintStream printConsole;

    private final static String PATH_CONFIG = "path.visualization";
    private final static String SPEND_MINUTES_CONFIG = "spends.minutes";
    private final static String NO_WAY_CONFIG = "no.way";

    Printer(PrintStream printConsole) {
        this.printConsole = printConsole;

        prints = new Properties();
        descriptions = new Properties();

        logger = LogManager.getLogger(Type.class);

        try (Reader inputPrints = new InputStreamReader(
            getClass().getResourceAsStream("/configs/prints"),
            StandardCharsets.UTF_8);
            Reader inputDescriptions = new InputStreamReader(
            getClass().getResourceAsStream("/configs/typeofpassages.description"),
            StandardCharsets.UTF_8)) {
            prints.load(inputPrints);
            descriptions.load(inputDescriptions);
        } catch (NullPointerException e) {
            logger.fatal("Не найден файл конфигурации пользовательского вывода", e);
        } catch (IOException e) {
            logger.fatal("Ошибка чтения файла конфигурации пользовательского вывода", e);
        }
    }

    // Вывод лабиринта таким, какой он есть
    public void print(Maze maze) {
        Cell[][] grid = maze.grid();

        for (int row = 0; row < maze.height(); ++row) {
            for (int col = 0; col < maze.width(); ++col) {
                printConsole.print(grid[row][col].visualization());
            }
            printConsole.println();
        }
    }

    // Вывод лабиринта вместе с путем от точки до точки
    public void print(Maze maze, Path path) {
        Cell[][] grid = new Cell[maze.height()][maze.width()];
        for (int row = 0; row < maze.height(); ++row) {
            for (int col = 0; col < maze.width(); ++col) {
                grid[row][col] = new Cell(maze.grid()[row][col].type());
            }
        }

        if (path.time() == 0) {
            String output = prints.getProperty(NO_WAY_CONFIG);
            printConsole.println(output);
        } else {
            for (Coordinate step : path.path()) {
                grid[step.row()][step.col()].visualization(
                    descriptions.getProperty(PATH_CONFIG).charAt(0));
            }

            print(new Maze(maze.width(), maze.height(), grid));
            String output = String.format(prints.getProperty(SPEND_MINUTES_CONFIG), path.time());
            printConsole.println(output);
        }
    }
}
