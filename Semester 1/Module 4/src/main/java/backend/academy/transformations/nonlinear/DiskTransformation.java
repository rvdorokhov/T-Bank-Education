package backend.academy.transformations.nonlinear;

import backend.academy.domain.model.Point;
import backend.academy.transformations.Transformation;

/**
 * Описывает нелинейное преобразование в виде Диска
 * <blockquote><pre>
 *    newX = (1 / π) * arctan(y / x) * sin(π * sqrt(x^2 + y^2)),
 *    newY = (1 / π) * arctan(y / x) * cos(π * sqrt(x^2 + y^2));
 * </pre></blockquote>
 * @see Transformation
 */
public class DiskTransformation implements Transformation {
    /**
     * Применяет нелинейное преобразование Диск к точке с координатами x и y
     * @param x координата x
     * @param y координата y
     * @return экземпляр класса {@code Point} - результат преобразования
     * @see Point
     */
    public Point transfrom(double x, double y) {
        double x2 = Math.pow(x, 2);
        double y2 = Math.pow(y, 2);
        double euclidSum = Math.sqrt(x2 + y2);

        double newX = (1 / Math.PI) * Math.atan(y / x) * Math.sin(Math.PI * euclidSum);
        double newY = newX / Math.tan(Math.PI * euclidSum);

        return new Point(newX, newY);
    }
}
