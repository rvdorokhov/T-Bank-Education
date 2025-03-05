package helpers;

import backend.academy.helpers.Check;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckTest {
    @Test
    @DisplayName("x и y входят в диапазон")
    void containsTestTrue() {
        int checkX = 1;
        int checkY = 1;
        int minX = 0;
        int minY = 0;
        int maxX = 10;
        int maxY = 10;

        boolean result = Check.contains(checkX, checkY, minX, maxX, minY, maxY);

        assertTrue(result);
    }

    @Test
    @DisplayName("y меньше нижнего значения диапзаона")
    void containsTestFalse1() {
        int checkX = 1;
        int checkY = -1;
        int minX = 0;
        int minY = 0;
        int maxX = 10;
        int maxY = 10;

        boolean result = Check.contains(checkX, checkY, minX, maxX, minY, maxY);

        assertFalse(result);
    }

    @Test
    @DisplayName("x меньше нижнего значения диапзаона")
    void containsTestFalse2() {
        int checkX = -1;
        int checkY = 1;
        int minX = 0;
        int minY = 0;
        int maxX = 10;
        int maxY = 10;

        boolean result = Check.contains(checkX, checkY, minX, maxX, minY, maxY);

        assertFalse(result);
    }

    @Test
    @DisplayName("x больше верхнего значения диапзаона")
    void containsTestFalse3() {
        int checkX = 11;
        int checkY = 1;
        int minX = 0;
        int minY = 0;
        int maxX = 10;
        int maxY = 10;

        boolean result = Check.contains(checkX, checkY, minX, maxX, minY, maxY);

        assertFalse(result);
    }

    @Test
    @DisplayName("y больше верхнего значения диапзаона")
    void containsTestFalse4() {
        int checkX = 1;
        int checkY = 11;
        int minX = 0;
        int minY = 0;
        int maxX = 10;
        int maxY = 10;

        boolean result = Check.contains(checkX, checkY, minX, maxX, minY, maxY);

        assertFalse(result);
    }

    @Test
    @DisplayName("Проверка нормальных значений")
    void areColorsCorrectTrue1() {
        int red = 128;
        int green = 128;
        int blue = 128;

        boolean result = Check.areColorsCorrect(red, green, blue);

        assertTrue(result);
    }

    @Test
    @DisplayName("Проверка нижних границ")
    void areColorsCorrectTrue2() {
        int red = 128;
        int green = 128;
        int blue = 128;
        int redZero = 0;
        int greenZero = 0;
        int blueZero = 0;

        boolean result1 = Check.areColorsCorrect(redZero, green, blue);
        boolean result2 = Check.areColorsCorrect(red, greenZero, blue);
        boolean result3 = Check.areColorsCorrect(red, green, blueZero);

        assertTrue(result1 && result2 && result3);
    }

    @Test
    @DisplayName("Проверка верхних границ")
    void areColorsCorrectTrue3() {
        int red = 128;
        int green = 128;
        int blue = 128;
        int redMax = 255;
        int greenMax = 255;
        int blueMax = 255;

        boolean result1 = Check.areColorsCorrect(redMax, green, blue);
        boolean result2 = Check.areColorsCorrect(red, greenMax, blue);
        boolean result3 = Check.areColorsCorrect(red, green, blueMax);

        assertTrue(result1 && result2 && result3);
    }

    @Test
    @DisplayName("Проверка значений, превышающих верхнюю границу")
    void areColorsCorrectFalse1() {
        int red = 128;
        int green = 128;
        int blue = 128;
        int redBad = 256;
        int greenBad = 256;
        int blueBad = 256;

        boolean result1 = Check.areColorsCorrect(redBad, green, blue);
        boolean result2 = Check.areColorsCorrect(red, greenBad, blue);
        boolean result3 = Check.areColorsCorrect(red, green, blueBad);

        assertFalse(result1 || result2 || result3);
    }

    @Test
    @DisplayName("Проверка значений, превышающих нижнюю границу")
    void areColorsCorrectFalse2() {
        int red = 128;
        int green = 128;
        int blue = 128;
        int redBad = -1;
        int greenBad = -1;
        int blueBad = -1;

        boolean result1 = Check.areColorsCorrect(redBad, green, blue);
        boolean result2 = Check.areColorsCorrect(red, greenBad, blue);
        boolean result3 = Check.areColorsCorrect(red, green, blueBad);

        assertFalse(result1 || result2 || result3);
    }
}
