package backend.academy.transformations;

import backend.academy.domain.model.Point;

/**
 * Интерфейс, описывающий методы-преобразование для некоторой точки на плоскости
 */
public interface Transformation {
    /**
     * Применяет преобразование к точке с координатами x и y
     * @param x координата x
     * @param y координата y
     * @return экземпляр класса {@code Point} - результат преобразования
     * @see Point
     */
    Point transfrom(double x, double y);

    /**
     * Применяет преобразование к точке с координатами x и y,
     * переданной как экземпляр класса {@code Point}
     * @param point точка на плоскости
     * @return экземпляр класса {@code Point} - результат преобразования
     * @see Point
     */
    default Point transfrom(Point point) {
        return transfrom(point.x(), point.y());
    }
}
