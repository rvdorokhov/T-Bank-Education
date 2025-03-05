package backend.academy.classes.game;

import backend.academy.classes.HangmanVisualization;
import java.io.InputStream;
import java.io.PrintStream;

abstract class GamePrint {
    protected GameLogic gameLogic;

    protected HangmanVisualization visualization;

    protected final PrintStream printConsole;
    protected final InputStream readConsole;

    GamePrint(PrintStream printConsole, InputStream readConsole) {
        this.printConsole = printConsole;
        this.readConsole = readConsole;
    }

    // Вывод текущего состояния угадываемого слова
    protected void printWordGuessProcess() {
        for (char letter : gameLogic.wordGuessProcess()) {
            printConsole.print(letter);
        }
        printConsole.println();
    }

    // Вывод неиспользованных букв
    protected void printUnusedLetters() {
        printConsole.print("Не использованные буквы: ");
        for (char letter : gameLogic.unusedLetters()) {
            printConsole.print(letter + " ");
        }
        printConsole.println();
    }

    // Вывод использованных букв
    protected void printUsedLetters() {
        printConsole.print("Использованные буквы: ");
        for (char letter : gameLogic.usedLetters()) {
            printConsole.print(letter + " ");
        }
        printConsole.println();
    }

    // Вывод результата игры
    protected void printGameResult() {
        if (!gameLogic.isWin()) {
            visualization.print(gameLogic.mistakes());
            printConsole.println("Игра окончена. Вы проиграли. " + "\n"
                + "Было загадано слово " + gameLogic.guessWord().word() + ".");
        } else {
            printConsole.println("Слово угадано! Вы победили!");
        }
    }

    protected void printClue() {
        printConsole.println(gameLogic.guessWord().clue());
    }

    abstract protected void printAll();
}
