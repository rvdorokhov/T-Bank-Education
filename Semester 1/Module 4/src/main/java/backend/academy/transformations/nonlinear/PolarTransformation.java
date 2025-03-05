package backend.academy.transformations.nonlinear;

import backend.academy.domain.model.Point;
import backend.academy.transformations.Transformation;

/**
 * Описывает нелинейное полярное преобразование
 * <blockquote><pre>
 *    newX = arctan(y / x) / π,
 *    newY = sqrt(x^2 + y^2) - 1;
 * </pre></blockquote>
 * @see Transformation
 */
public class PolarTransformation implements Transformation {
    /**
     * Применяет нелинейное полярное преобразование к точке с координатами x и y
     * @param x координата x
     * @param y координата y
     * @return экземпляр класса {@code Point} - результат преобразования
     * @see Point
     */
    public Point transfrom(double x, double y) {
        double newX = Math.atan(y / x) / Math.PI;
        double newY = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) - 1;

        return new Point(newX, newY);
    }
}
