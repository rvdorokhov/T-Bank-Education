package backend.academy.transformations.nonlinear;

import backend.academy.domain.model.Point;
import backend.academy.transformations.Transformation;

/**
 * Описывает нелинейное синусоидальное полярное преобразование
 * <blockquote><pre>
 *    newX = sin(x),
 *    newY = sin(y);
 * </pre></blockquote>
 * @see Transformation
 */
public class SinTransformation implements Transformation {
    /**
     * Применяет нелинейное синусоидальное преобразование к точке с координатами x и y
     * @param x координата x
     * @param y координата y
     * @return экземпляр класса {@code Point} - результат преобразования
     * @see Point
     */
    public Point transfrom(double x, double y) {
        double newX = Math.sin(x);
        double newY = Math.sin(y);

        return new Point(newX, newY);
    }
}
