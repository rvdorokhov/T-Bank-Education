package backend.academy.exeptions;

import backend.academy.transformations.affine.AffineTransformation;

/**
 * Сигнализирует о неверном значении коэффициентов a, b, d, e афинного преобразования
 * @see AffineTransformation
 */
public class InvalidAffineTransformationCoefs extends RuntimeException {
    public InvalidAffineTransformationCoefs(
        double a,
        double b,
        double d,
        double e) {
        super(String.format(
            "The coefficients %f, %f, %f, %f "
            + "do not satisfy one of the following conditions: "
            + "a^2 + d^2 < 1, "
            + "b^2 + e^2 < 1, "
            + "a^2 + b^2 + d^2 + e^2 < 1 + (ae - bd)^2", a, b, d, e));
    }
}
