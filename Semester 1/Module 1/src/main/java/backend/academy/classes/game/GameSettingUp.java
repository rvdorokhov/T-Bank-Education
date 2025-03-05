package backend.academy.classes.game;

import backend.academy.enums.Difficulty;
import backend.academy.enums.Theme;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class GameSettingUp {
    private GameSettingUp() {}

    // Выбор уровня сложности
    @SuppressWarnings("MultipleStringLiterals")
    public static Difficulty difficultySettingUp(PrintStream printConsole, InputStream readConsole) {
        String input;
        int choose = -1;
        Difficulty difficulty = null;
        Scanner scanner = new Scanner(readConsole);
        Difficulty[] difficulties = Difficulty.values();

        // Вывод в консоль меню выбора сложности
        printConsole.println("Выберите сложность из предложенных ниже:");
        for (int i = 0; i < difficulties.length; ++i) {
            printConsole.println(i + 1 + ". " + difficulties[i].getRussianName());
        }
        printConsole.println(difficulties.length + 1 + ". Случайная сложность");
        printConsole.println("Для выбора сложности напечатайте соответсвующую цифру. Например "
            + 1 + " для выбора сложности " + difficulties[0].getRussianName());

        // Считывание введенного значения
        boolean validInput = false;
        while (!validInput) {
            try {
                input = scanner.nextLine();
                choose = Integer.parseInt(input);
                choose--;

                if (choose != difficulties.length) {
                    difficulty = difficulties[choose];
                } else {
                    difficulty = Difficulty.getRandomDifficulty();
                }

                validInput = true;
            } catch (NumberFormatException e) {
                printConsole.println("Некорректный ввод. Напечатайте одну цифру из предложенного выше списка");
            } catch (ArrayIndexOutOfBoundsException e) {
                printConsole.println("Уровня сложности, который соответсвовал бы введенной цифре, нет. "
                    + "Напечатайте одну цифру из предложенного выше списка");
            }
        }

        return difficulty;
    }

    // Выбор темы
    public static Theme themeSettingUp(PrintStream printConsole, InputStream readConsole) {
        int choose = -1;
        String input;
        Theme theme = null;
        Theme[] themes = Theme.values();
        Scanner scanner = new Scanner(readConsole);

        // Вывод в консоль меню выбора темы
        printConsole.println("Выберите тему из предложенных ниже: ");
        for (int i = 0; i < themes.length; ++i) {
            printConsole.println(i + 1 + ". " + themes[i].getRussianName());
        }
        printConsole.println(themes.length + 1 + ". Случайная тема");
        printConsole.println("Для выбора темы напечатайте соответсвующую цифру. Например "
            + 1 + " для выбора темы " + themes[0].getRussianName());

        // Считывание введенного значения
        boolean validInput = false;
        while (!validInput) {
            try {
                input = scanner.nextLine();
                choose = Integer.parseInt(input);
                choose--;

                if (choose != themes.length) {
                    theme = themes[choose];
                } else {
                    theme = Theme.getRandomTheme();
                }

                validInput = true;
            } catch (NumberFormatException e) {
                printConsole.println("Некорректный ввод. Напечатайте одну цифру из предложенного выше списка");
            } catch (ArrayIndexOutOfBoundsException e) {
                printConsole.println("Темы, которая соответсвовала бы введенной цифре, нет. "
                    + "Напечатайте одну цифру из предложенного выше списка");
            }
        }

        return theme;
    }
}
