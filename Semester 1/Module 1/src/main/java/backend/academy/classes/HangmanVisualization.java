package backend.academy.classes;

import backend.academy.enums.Difficulty;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HangmanVisualization {
    private final HashMap<Integer, String> visualization;
    private final PrintStream console;
    private final Properties themes;
    private final Logger logger;

    public HangmanVisualization(Difficulty difficulty, PrintStream console) throws Exception {
        themes = new Properties();

        logger = LogManager.getLogger(HangmanVisualization.class);


        try (Reader input = new InputStreamReader(
            getClass().getResourceAsStream("/configs/config.hangman.view.properties"),
            StandardCharsets.UTF_8)) {
            themes.load(input);
        } catch (NullPointerException e) {
            logger.error("Не найден файл конфигурации с визуальным отображением висельника", e);
        }

        // Инициализация визуальных состояний висельника
        // и допустимого количества ошибок в зависимости от сложности
        visualization = new HashMap<>();
        setVisualization(difficulty);

        // Инициализация потока ввода-вывода
        this.console = console;
    }

    // Инициализация словаря в соответствии с переданной сложностью
    private void setVisualization(Difficulty difficulty) {
        String difficultyName = difficulty.name().toLowerCase();

        for (int mistakes = 0; mistakes <= difficulty.getAttempts(); ++mistakes) {
            visualization.put(mistakes, themes.getProperty(mistakes + ".mistake." + difficultyName));
        }
    }

    public void print(int mistakes) {
        console.println(visualization.get(mistakes));
    }
}
