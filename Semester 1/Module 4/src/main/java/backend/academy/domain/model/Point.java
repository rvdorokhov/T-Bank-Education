package backend.academy.domain.model;

/**
 * Запись некоторой точки, хранит координаты точки на некоторой плоскости
 * @param x {@code double} координата x
 * @param y {@code double} координата y
 */
public record Point(double x, double y) {}
