package backend.academy.game;

import backend.academy.generators.Generator;
import backend.academy.maze.Maze;
import backend.academy.maze.Type;
import backend.academy.solvers.Solver;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Game {
    protected Maze maze;
    protected List<Solver> solvers;
    protected List<Generator> generators;
    protected Solver curSolver;
    protected Generator curGenerator;
    protected final Printer printer;
    protected int height;
    protected int width;

    protected final PrintStream printConsole;
    protected final InputStream readConsole;
    protected final Scanner scanner;
    protected final Properties prints;
    protected final Properties settings;
    protected final Properties descriptions;
    protected final Logger logger;

    protected final static int CHOOSE_GENERATOR = 1;
    protected final static int CHOOSE_SOLVER = 2;
    protected final static int CHOOSE_PROPERTIES = 3;
    protected final static int CHOOSE_MAZE = 4;
    protected final static int CHOOSE_END = 5;
    protected final static String CHOOSE_EXIT = "no";
    protected final static List<Integer> CHOOSES = new ArrayList<>(List.of(
        CHOOSE_GENERATOR, CHOOSE_SOLVER, CHOOSE_PROPERTIES, CHOOSE_MAZE, CHOOSE_END));

    protected final static String START_MENU = "start.menu";
    protected final static String GENERATE_MENU = "generate.menu";
    protected final static String SOLVER_MENU = "solver.menu";
    protected final static String DEFAULT_HEIGHT = "default.height";
    protected final static String DEFAULT_WIDTH = "default.width";
    protected final static String CHOOSE_WIDTH_HEIGHT = "choose.properties";
    protected final static String CHOOSE_START_POINT = "maze.choose.start";
    protected final static String CHOOSE_END_POINT = "maze.choose.end";
    protected final static String QUIT_WARNING = "maze.quit.warning";
    protected final static String ERROR_MESSAGE = "invalid.input";
    protected final static String MAX_Y = "max.width";
    protected final static String MAX_X = "max.height";
    protected final static String COST = "local.cost";

    protected int makeChoose() {
        String choose = "0";
        int intChoose = -1;

        boolean validInput = true;
        do {
            try {
                choose = scanner.next().toLowerCase();
                intChoose = Integer.parseInt(choose);
                validInput = true;
            } catch (Exception e) {
                validInput = false;
                scanner.nextLine(); // "съедаем" всю введенную строку
                printConsole.println(prints.getProperty(ERROR_MESSAGE));
            }
        } while (!validInput);

        return intChoose;
    }

    protected boolean isHeightWidthCorrect(int y, int x) {
        int maxY = Integer.parseInt(settings.getProperty(MAX_Y));
        int maxX = Integer.parseInt(settings.getProperty(MAX_X));
        return !((y <= 0) || (x <= 0) || (y > maxY) || (x > maxX));
    }

    protected boolean isCoordinatesCorrect(int y, int x) {
        return !(!isHeightWidthCorrect(y, x) || y > height || x > width
            || maze.grid()[y][x].type() == Type.WALL);
    }

    protected void printDescriptions() {
        for (Type type : Type.values()) {
            String typeName = type.toString().toLowerCase();
            printConsole.print(
                descriptions.getProperty(typeName + ".visualization") + " ");
            printConsole.print(
                descriptions.getProperty(typeName + ".name") + " - ");
            printConsole.print(
                descriptions.getProperty(typeName + ".description") + " ");
            if (type != Type.WALL) {
                String localCost = prints.getProperty(COST);
                printConsole.print(
                    descriptions.getProperty(typeName + ".cost") + " " + localCost);
            }
            printConsole.println();
        }
    }

    public abstract void startGame();

    public Game(
        PrintStream printConsole,
        InputStream readConsole,
        List<Generator> generators,
        List<Solver> solvers) {
        this.printConsole = printConsole;
        this.readConsole = readConsole;
        this.scanner = new Scanner(readConsole);
        printer = new Printer(printConsole);

        prints = new Properties();
        settings = new Properties();
        descriptions = new Properties();

        logger = LogManager.getLogger(Type.class);

        try (Reader inputPrints = new InputStreamReader(
            getClass().getResourceAsStream("/configs/prints"),
            StandardCharsets.UTF_8);
            Reader inputSettings = new InputStreamReader(
            getClass().getResourceAsStream("/configs/settings"),
            StandardCharsets.UTF_8);
            Reader inputDescriptions = new InputStreamReader(
            getClass().getResourceAsStream("/configs/typeofpassages.description"),
            StandardCharsets.UTF_8)) {
            prints.load(inputPrints);
            settings.load(inputSettings);
            descriptions.load(inputDescriptions);
        } catch (NullPointerException e) {
            logger.fatal("Не найден файл конфигурации", e);
        } catch (IOException e) {
            logger.fatal("Ошибка чтения файла конфигурации", e);
        }

        height = Character.getNumericValue(
            settings.getProperty(DEFAULT_HEIGHT).charAt(0));
        width = Character.getNumericValue(
            settings.getProperty(DEFAULT_WIDTH).charAt(0));

        this.generators = new ArrayList<>(generators);
        this.solvers = new ArrayList<>(solvers);

        curGenerator = this.generators.getFirst();
        curSolver = this.solvers.getFirst();
    }
}
