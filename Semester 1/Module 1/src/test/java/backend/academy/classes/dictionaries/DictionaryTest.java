package backend.academy.classes.dictionaries;

import backend.academy.enums.Theme;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DictionaryTest {
    final static Properties themes = new Properties();
    static Dictionary dictionary = null;

    @BeforeAll // Октрытие файла конфигурации
    static void openConfig() throws Exception {
        try (Reader input = new InputStreamReader(
            new FileInputStream("src/main/resources/configs/config.themes.properties"),
            StandardCharsets.UTF_8)) {
            themes.load(input);
        } catch (java.io.FileNotFoundException e) {
            throw new java.io.FileNotFoundException("Не найден файл конфигурации " + e.getMessage());
        }
    }

    @BeforeAll
    static void dictionaryInit() {
        Map<Theme, List<WordWithClue>> puttingDictionary = new HashMap<>();
        for (Theme theme : Theme.values()) {
            String themeName = theme.name().toLowerCase();

            String[] words = themes.getProperty(themeName + ".easy").split(", ");
            String[] clues = themes.getProperty(themeName + ".easy.clue").split(", ");

                ArrayList<WordWithClue> wordsWithClues = new ArrayList<>(6);
                for (int i = 0; i < words.length; ++i) {
                    wordsWithClues.add(new WordWithClue(words[i], clues[i]));
                }

                puttingDictionary.put(theme, wordsWithClues);
        }

        dictionary = new Dictionary(puttingDictionary);
    }

    @Test
    void getRandomThemeTest() {
        ArrayList<Theme> themes = new ArrayList<>(List.of(Theme.values()));

        Theme result = dictionary.getRandomTheme();

        assertTrue(themes.contains(result));
    }

    @Test // Тестирование метода, который получает в параметрах тему
    void getRandomWordTest1() {
        ArrayList<String> words = new ArrayList<>(List.of(
            themes.getProperty("countries.easy").split(", ")));

        String result = dictionary.getRandomWord(Theme.COUNTRIES).word();

        assertTrue(words.contains(result));
    }

    @Test // Тестирование метода, который ничего не получает в параметрах
    void getRandomWordTest2() {
        ArrayList<String> words = new ArrayList<>();
        Set<String> keys = themes.stringPropertyNames();
        for (String key : keys) {
            if(key.contains("easy")){
                words.addAll(List.of(themes.getProperty(key).split(", ")));
            }
        }

        String result = dictionary.getRandomWord().word();

        assertTrue(words.contains(result));
    }
}
