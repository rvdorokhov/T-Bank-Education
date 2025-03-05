package transformations.affine;

import backend.academy.transformations.affine.AffineTransformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AffineTransformationTest {
    @Test
    @DisplayName("Введены корректные коэффициенты")
    void isCorrectTrue() {
        double a = 0.5;
        double b = 0.3;
        double c = 0.7;
        double d = 0.4;
        double e = 0.2;
        double f = 0.75;
        AffineTransformation test = new AffineTransformation(a, b, c, d, e, f);

        boolean result = test.isCorrect(a, b, d, e);

        assertTrue(result);
    }

    @Test
    @DisplayName("Введены некорректные коэффициенты")
    void isCorrectFalse() {
        double a = 1;
        double b = 1;
        double c = 1;
        double d = 1;
        double e = 1;
        double f = 1;
        AffineTransformation test = new AffineTransformation(a, b, c, d, e, f);

        boolean result = test.isCorrect(a, b, d, e);

        assertFalse(result);
    }
}
