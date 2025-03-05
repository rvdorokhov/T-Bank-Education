package backend.academy.game;

import backend.academy.generators.Generator;
import backend.academy.maze.Coordinate;
import backend.academy.solvers.Path;
import backend.academy.solvers.Solver;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class GameSession extends Game {
    private final static String GENERATOR_CONFIG = "generate.";
    private final static String GENERATOR_DESCRIPTION_CONFIG = "generate.description.";
    private final static String SOLVER_CONFIG = "solver.";
    private final static String SOLVER_DESCRIPTION_CONFIG = "solver.description.";

    public void startGame() {
        int choose = 0;

        while (choose != CHOOSE_END) {
            printConsole.println(prints.getProperty(START_MENU));
            do {
                choose = makeChoose();
            } while (!CHOOSES.contains(choose));

            switch (choose) {
                case CHOOSE_GENERATOR:
                    chooseGenerator();
                    break;
                case CHOOSE_SOLVER:
                    chooseSearch();
                    break;
                case CHOOSE_PROPERTIES:
                    chooseProperties();
                    break;
                case CHOOSE_MAZE:
                    chooseMaze();
                    break;
                case CHOOSE_END:
                    break;
                default:
                    logger.warn("Непредусмотренный ввод");
            }
        }

    }

    private void chooseGenerator() {
        curGenerator = chooseOption(generators, GENERATE_MENU,
            GENERATOR_CONFIG, GENERATOR_DESCRIPTION_CONFIG);
    }

    private void chooseSearch() {
        curSolver = chooseOption(solvers, SOLVER_MENU,
            SOLVER_CONFIG, SOLVER_DESCRIPTION_CONFIG);
    }

    private <T> T chooseOption(List<T> options, String menu, String object, String description) {
        int choose;
        printConsole.println(prints.getProperty(menu));
        for (int i = 0; i < options.size(); ++i) {
            String className = options.get(i).getClass().getName().toLowerCase();
            printConsole.print(i + 1 + ". ");
            printConsole.println(prints.getProperty(object + className));
            printConsole.println(prints.getProperty(description + className));
        }

        do {
            choose = makeChoose();
        } while (choose < 1 || choose > options.size());

        return options.get(choose - 1);
    }


    private void chooseProperties() {
        int chooseWidth;
        int chooseHeight;

        do {
            printConsole.println(prints.getProperty(CHOOSE_WIDTH_HEIGHT));
            chooseWidth = makeChoose();
            chooseHeight = makeChoose();
        } while (!isHeightWidthCorrect(chooseHeight, chooseWidth));

        width = chooseWidth;
        height = chooseHeight;
    }

    private void chooseMaze() {
        int startX = 1;
        int startY = 1;
        int endX = height;
        int endY = width;
        String input = "0";

        maze = curGenerator.generate(height, width);

        while (!input.equals(CHOOSE_EXIT)) {
            printer.print(maze);
            printDescriptions();

            do {
                printConsole.println(prints.getProperty(CHOOSE_START_POINT));
                startX = makeChoose();
                startY = maze.height() - makeChoose() - 1;
            } while (!isCoordinatesCorrect(startY, startX));

            scanner.nextLine();

            do {
                printConsole.println(prints.getProperty(CHOOSE_END_POINT));
                endX = makeChoose();
                endY = maze.height() - makeChoose() - 1;
            } while (!isCoordinatesCorrect(endY, endX));

            scanner.nextLine();

            Coordinate start = new Coordinate(startY, startX);
            Coordinate end = new Coordinate(endY, endX);
            Path path = curSolver.solve(maze, start, end);

            printer.print(maze, path);

            printConsole.println(prints.getProperty(QUIT_WARNING));
            input = scanner.next();
            scanner.nextLine();
        }
    }

    public GameSession(
        PrintStream printConsole,
        InputStream readConsole,
        List<Generator> generators,
        List<Solver> solvers) {
        super(printConsole, readConsole, generators, solvers);
    }
}
