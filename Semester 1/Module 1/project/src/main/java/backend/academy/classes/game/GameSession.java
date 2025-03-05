package backend.academy.classes.game;

import backend.academy.classes.HangmanVisualization;
import backend.academy.enums.Difficulty;
import backend.academy.enums.Theme;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class GameSession extends GamePrint {
    private static final int CHOOSE_DIFFICULTY = 1;
    private static final int CHOOSE_THEME = 2;
    private static final int START_GAME = 3;
    private static final String CHOOSES_STR = """
                    Выберите ваше действие:
                    1. Выбрать сложность
                    2. Выбрать тему
                    3. Начать игру""";
    private static final HashSet<Integer> POSSIBLE_CHOOSES = new HashSet<Integer>(
        List.of(CHOOSE_DIFFICULTY, CHOOSE_THEME, START_GAME)
    );

    private Theme theme = null;
    private Difficulty difficulty = null;
    private Scanner scanner;

    public GameSession(PrintStream printConsole, InputStream readConsole) throws Exception {
        super(printConsole, readConsole);
    }

    // Вывод информации о текущем "состоянии" игры - прогресса отгадывания слова, использованных букв и т.д.
    protected void printAll() {
        visualization.print(gameLogic.mistakes());
        printConsole.println("Осталось попыток: " + (difficulty.getAttempts() - gameLogic.mistakes()));

        printWordGuessProcess();
        printUnusedLetters();
        printUsedLetters();
    }

    // Выбор очередной буквы
    private char chooseLetter() {
        char choose = 0;

        printConsole.println("Введите букву (для подсказки введите слово подсказка):");

        boolean validInput = false;
        while (!validInput) {
            try {
                String input = scanner.nextLine().toLowerCase();
                choose = input.charAt(0);

                // Проверка на правильность ввода - длина введенной строки должна быть равна 1
                // и не содержаться в массиве использованных букв
                if (input.equals("подсказка")) {
                    printAll();
                    printClue();
                } else if ((input.length() != 1) || !gameLogic.unusedLetters().contains(choose)) {
                    printAll();
                    printConsole.println("Некорректный ввод. Введите одну букву из списка не использованных:");
                } else {
                    validInput = true;
                }
            } catch (Exception e) {
                printAll();
                printConsole.println("Некорректный ввод. Введите букву еще раз");
            }
        }

        return choose;
    }

    // Реализует "активную" часть игры - в которой пользователь непосредственно угадывает слово
    private void gameProcess() {
        printConsole.println("Количество попыток: " + difficulty.getAttempts());
        while (!gameLogic.isEnd()) {
            printAll();

            char letter =  chooseLetter();
            if (gameLogic.checkLetter(letter)) {
                printConsole.println("Буква " + letter + " есть в слове!");
            } else {
                printConsole.println("Увы, но буквы " + letter + " нет в слове...");
            }
        }

        printGameResult();
    }

    public void startGame() throws Exception {
        scanner = new Scanner(readConsole);

        printConsole.println("Добро пожаловать в игру \"Висельница\"!");

        int choose = -1;
        while (choose != START_GAME) {
            try {
                printConsole.println(CHOOSES_STR);

                String input = scanner.nextLine();
                choose = Integer.parseInt(input);

                if (!POSSIBLE_CHOOSES.contains(choose)) {
                    printConsole.println("Выбора с такой цифрой нет.");
                }
            } catch (NumberFormatException e) {
                printConsole.println("Некорректный ввод. Введите цифру, соответствующую вашему выбору.");
            }

            switch (choose) {
                case CHOOSE_DIFFICULTY:
                    difficulty = GameSettingUp.difficultySettingUp(printConsole, readConsole);
                    printConsole.println("Выбрана сложность " + difficulty.getRussianName());
                    break;
                case CHOOSE_THEME:
                    theme = GameSettingUp.themeSettingUp(printConsole, readConsole);
                    printConsole.println("Выбрана тема " + theme.getRussianName());
                    break;
                case START_GAME:
                    printConsole.println("Начнем игру!");
                    break;
                default:
                    printConsole.println("Непредусмотренный ввод");
            }
        }

        if (difficulty == null) {
            difficulty = Difficulty.getRandomDifficulty();
        }

        if (theme == null) {
            theme = Theme.getRandomTheme();
        }

        gameLogic = new GameLogic(difficulty, theme, printConsole);
        visualization = new HangmanVisualization(difficulty, printConsole);

        printConsole.println("Выбрана тема: " + gameLogic.theme().getRussianName() + "\n"
            + "Выбрана сложность: " + gameLogic.difficulty().getRussianName());

        gameProcess();

        scanner.close();
    }
}
