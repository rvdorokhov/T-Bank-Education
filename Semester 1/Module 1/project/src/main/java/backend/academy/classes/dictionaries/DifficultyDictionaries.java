package backend.academy.classes.dictionaries;

import backend.academy.enums.Difficulty;
import backend.academy.enums.Theme;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Класс, содержащий в себе несколько словарей, разделенных по уровню сложности
public class DifficultyDictionaries {
    private final Properties themes;
    private final Logger logger;
    private SecureRandom secureRandom = new SecureRandom(); // Будет использоваться для генерации случайных чисел
    private final Map<Difficulty, Dictionary> dictionaries = new HashMap<>(); // "словарь словарей"

    public DifficultyDictionaries() throws Exception {
        themes = new Properties();

        logger = LogManager.getLogger(DifficultyDictionaries.class);

        try (Reader input = new InputStreamReader(
            getClass().getResourceAsStream("/configs/config.themes.properties"),
            StandardCharsets.UTF_8)) {
            themes.load(input);
        } catch (NullPointerException e) {
            logger.fatal("Не найден файл конфигурации с темами, словами и подсказками к ним", e);
        }

        setDictionaries();
    }

    // Инициализация словарей
    private void setDictionaries() {
        for (Difficulty difficulty : Difficulty.values()) {
            String difficultyName = difficulty.name().toLowerCase();
            Map<Theme, List<WordWithClue>> puttingDictionary = new HashMap<>();
            for (Theme theme : Theme.values()) {
                String themeName = theme.name().toLowerCase();

                String[] words = themes.getProperty(themeName + "." + difficultyName).split(", ");
                String[] clues = themes.getProperty(themeName + "." + difficultyName + ".clue").split(", ");

                ArrayList<WordWithClue> wordsWithClues = new ArrayList<>();
                for (int i = 0; i < words.length; ++i) {
                    wordsWithClues.add(new WordWithClue(words[i], clues[i]));
                }

                puttingDictionary.put(theme, wordsWithClues);
            }
            dictionaries.put(difficulty, new Dictionary(puttingDictionary));
        }
    }

    // Выбор случайной сложности
    public Difficulty getRandomDifficulty() {
        List<Difficulty> difficulties = new ArrayList<>(dictionaries.keySet());
        int randomNumber = secureRandom.nextInt(difficulties.size());

        return difficulties.get(randomNumber);
    }

    // Выбор случайного слова с указанной сложностю и указанной темой
    public WordWithClue getRandomWord(Difficulty difficulty, Theme theme) {
        return dictionaries.get(difficulty).getRandomWord(theme);
    }

    // Выбор случайного слова без указанной сложности без указанной темы
    public WordWithClue getRandomWord() {
        return dictionaries.get(getRandomDifficulty()).getRandomWord();
    }

    // Выбор случайного слова без указания темы
    public WordWithClue getRandomWord(Difficulty difficulty) {
        return dictionaries.get(difficulty).getRandomWord();
    }

    // Выбор случайного слова без указания сложности
    public WordWithClue getRandomWord(Theme theme) {
        return dictionaries.get(getRandomDifficulty()).getRandomWord(theme);
    }
}
