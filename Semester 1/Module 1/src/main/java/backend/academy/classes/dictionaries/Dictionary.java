package backend.academy.classes.dictionaries;

import backend.academy.enums.Theme;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {
    private final SecureRandom secureRandom = new SecureRandom(); // Будет использоваться для генерации случайных чисел
    private final Map<Theme, List<WordWithClue>> dictionaries;
    private final int numberOfThemes; // Количество тем

    public Dictionary(Map<Theme, List<WordWithClue>> dictionary) {
        this.dictionaries = new HashMap<>(dictionary);
        this.numberOfThemes = dictionaries.size();
    }

    // Выбор случайного словаря
    public Theme getRandomTheme() {
        List<Theme> keys = new ArrayList<>(dictionaries.keySet());
        int randomNumber = secureRandom.nextInt(numberOfThemes);

        return keys.get(randomNumber);
    }

    // Выбор случайного слова с указанной темой
    public WordWithClue getRandomWord(Theme theme) {
        int size = dictionaries.get(theme).size();
        int randomNumber = secureRandom.nextInt(size);

        return dictionaries.get(theme).get(randomNumber);
    }

    // Выбор случайного слова без указания темы
    public WordWithClue getRandomWord() {
        Theme randomTheme = getRandomTheme();

        return getRandomWord(randomTheme);
    }
}
