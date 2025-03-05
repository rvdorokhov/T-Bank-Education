package backend.academy.classes.dictionaries;

import backend.academy.enums.Difficulty;
import backend.academy.enums.Theme;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DifficultyDictionariesTest{
    final static Properties themes = new Properties();

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

    @Test
    void getRandomDifficultyTest() throws Exception {
        DifficultyDictionaries dictionaries = new DifficultyDictionaries();
        ArrayList<Difficulty> difficulties = new ArrayList<>(List.of(Difficulty.values()));

        Difficulty result = dictionaries.getRandomDifficulty();

        assertTrue(difficulties.contains(result));
    }

    @Test // Тестирование метода, который получает в параметрах тему и сложность
    void getRandomWordTest1() throws Exception {
        DifficultyDictionaries dictionaries = new DifficultyDictionaries();
        ArrayList<String> words = new ArrayList<>(List.of(
            themes.getProperty("animals.easy").split(", ")));

        String result = dictionaries.getRandomWord(Difficulty.EASY, Theme.ANIMALS).word();

        assertTrue(words.contains(result));
    }

    @Test // Тестирование метода, который не получает в параметрах ни тему, ни сложность
    void getRandomWordTest2() throws Exception {
        DifficultyDictionaries dictionaries = new DifficultyDictionaries();
        ArrayList<String> words = new ArrayList<>();
        Set<String> keys = themes.stringPropertyNames();
        for (String key : keys) {
            words.addAll(List.of(themes.getProperty(key).split(", ")));
        }

        String result = dictionaries.getRandomWord().word();

        assertTrue(words.contains(result));
    }

    @Test // Тестирование метода, который не получает в параметрах тему, только сложность
    void getRandomWordTest3() throws Exception {
        DifficultyDictionaries dictionaries = new DifficultyDictionaries();
        ArrayList<String> words = new ArrayList<>();
        Set<String> keys = themes.stringPropertyNames();
        for (String key : keys) {
            if(key.contains("medium")){
                words.addAll(List.of(themes.getProperty(key).split(", ")));
            }
        }

        String result = dictionaries.getRandomWord(Difficulty.MEDIUM).word();

        assertTrue(words.contains(result));
    }

    @Test // Тестирование метода, который не получает в параметрах сложность, только тему
    void getRandomWordTest4() throws Exception {
        DifficultyDictionaries dictionaries = new DifficultyDictionaries();
        ArrayList<String> words = new ArrayList<>();
        Set<String> keys = themes.stringPropertyNames();
        for (String key : keys) {
            if(key.contains("sports")){
                words.addAll(List.of(themes.getProperty(key).split(", ")));
            }
        }

        String result = dictionaries.getRandomWord(Theme.SPORTS).word();

        assertTrue(words.contains(result));
    }
}
