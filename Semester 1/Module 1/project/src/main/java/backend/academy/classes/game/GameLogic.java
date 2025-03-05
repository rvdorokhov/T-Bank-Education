package backend.academy.classes.game;

import backend.academy.classes.dictionaries.DifficultyDictionaries;
import backend.academy.classes.dictionaries.WordWithClue;
import backend.academy.enums.Difficulty;
import backend.academy.enums.Theme;
import java.io.PrintStream;
import java.util.TreeSet;
import lombok.Getter;

@Getter
public class GameLogic {
    private int mistakes = 0;
    private final Theme theme;
    private final Difficulty difficulty;
    private final WordWithClue guessWord;
    private final char[] wordGuessProcess;
    private final TreeSet<Character> usedLetters = new TreeSet<>();
    private final TreeSet<Character> unusedLetters = new TreeSet<>();
    private final DifficultyDictionaries dictionary = new DifficultyDictionaries();

    private final PrintStream printConsole;

    public GameLogic(Difficulty difficulty, Theme theme, PrintStream printConsole) throws Exception {
        this.difficulty = difficulty;

        this.theme = theme;

        this.printConsole = printConsole;
        guessWord = dictionary.getRandomWord(this.difficulty, this.theme);
        wordGuessProcess = "_".repeat(guessWord.word().length()).toCharArray();

        for (char letter = 'а'; letter <= 'я'; ++letter) {
            unusedLetters.add(letter);
        }
    }

    // Проверка очередной буквы на принадлежность загаданному слову
    public boolean checkLetter(char letter) {
        unusedLetters.remove((Character) letter);
        usedLetters.add(letter);

        // Изменение состояния угадываемого слова (замена _ на угаданную букву)
        boolean success = false;
        for (int i = 0; i < guessWord.word().length(); ++i) {
            if (guessWord.word().charAt(i) == letter) {
                wordGuessProcess[i] = letter;
                success = true;
            }
        }

        if (!success) {
            mistakes += 1;
        }

        return success;
    }

    // Проверка на победу пользователя
    public boolean isWin() {
        return !String.valueOf(wordGuessProcess).contains("_");
    }

    // Проверка на конец игры
    public boolean isEnd() {
        return isWin() || (mistakes == difficulty.getAttempts());
    }
}
