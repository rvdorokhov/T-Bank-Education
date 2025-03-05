package backend.academy.transformations.nonlinear;

import backend.academy.domain.model.Point;
import backend.academy.transformations.Transformation;

/**
 * Описывает нелинейное преобразование в виде Сердца
 * <blockquote><pre>
 *    newX = sqrt(x^2 + y^2) * sin(sqrt(x^2 + y^2) * arctan(y / x)),
 *    newY = -sqrt(x^2 + y^2) * cos(sqrt(x^2 + y^2) * arctan(y / x));
 * </pre></blockquote>
 * @see Transformation
 */
public class HeartTransformation implements Transformation {
    /**
     * Применяет нелинейное преобразование Сердце к точке с координатами x и y
     * @param x координата x
     * @param y координата y
     * @return экземпляр класса {@code Point} - результат преобразования
     * @see Point
     */
    public Point transfrom(double x, double y) {
        double x2 = Math.pow(x, 2);
        double y2 = Math.pow(y, 2);
        double euclidSum = Math.sqrt(x2 + y2);

        double newX = euclidSum * Math.sin(euclidSum * Math.atan(y / x));
        double newY = -newX;

        return new Point(newX, newY);
    }
}
