package domain.model;

import backend.academy.domain.model.FractalImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FractalImageTest {
    FractalImage fractalImage = new FractalImage(11, 11);

    @Test
    @DisplayName("x и y входят в диапазон")
    void containsTestTrue() {
        int checkX = 1;
        int checkY = 1;

        boolean result = fractalImage.contains(checkX, checkY);

        assertTrue(result);
    }

    @Test
    @DisplayName("y меньше нижнего значения диапзаона")
    void containsTestFalse1() {
        int checkX = 1;
        int checkY = -1;

        boolean result = fractalImage.contains(checkX, checkY);

        assertFalse(result);
    }

    @Test
    @DisplayName("x меньше нижнего значения диапзаона")
    void containsTestFalse2() {
        int checkX = -1;
        int checkY = 1;

        boolean result = fractalImage.contains(checkX, checkY);

        assertFalse(result);
    }

    @Test
    @DisplayName("x больше верхнего значения диапзаона")
    void containsTestFalse3() {
        int checkX = 11;
        int checkY = 1;

        boolean result = fractalImage.contains(checkX, checkY);

        assertFalse(result);
    }

    @Test
    @DisplayName("y больше верхнего значения диапзаона")
    void containsTestFalse4() {
        int checkX = 1;
        int checkY = 11;

        boolean result = fractalImage.contains(checkX, checkY);

        assertFalse(result);
    }
}
