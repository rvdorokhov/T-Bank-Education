package backend.academy.transformations.nonlinear;

import backend.academy.domain.model.Point;
import backend.academy.transformations.Transformation;

/**
 * Описывает нелинейное сферическое преобразование
 * <blockquote><pre>
 *    newX = x / (x^2 + y^2),
 *    newY = y / (x^2 + y^2);
 * </pre></blockquote>
 * @see Transformation
 */
public class SphereTransformation implements Transformation {
    /**
     * Применяет нелинейное сферическое преобразование к точке с координатами x и y
     * @param x координата x
     * @param y координата y
     * @return экземпляр класса {@code Point} - результат преобразования
     * @see Point
     */
    public Point transfrom(double x, double y) {
        double newX = x / (Math.pow(x, 2) + Math.pow(y, 2));
        double newY = y / (Math.pow(y, 2) + Math.pow(y, 2));

        return new Point(newX, newY);
    }
}
