package backend.academy.helpers;

import lombok.experimental.UtilityClass;

/**
 * Вспомогательный класс, совершающий разного рода проверка
 */

@UtilityClass
public class Check {

    private final static int COLOR_MIN = 0;
    private final static int COLOR_MAX = 255;

    /**
     * Проверяет, что x и y лежат в некотором диапазон
     * @param checkX проверяемый x
     * @param checkY проверяемый y
     * @param minX нижняя граница x
     * @param maxX верхняя граница x
     * @param minY нижняя граница y
     * @param maxY верхняя граница x
     * @return {@code true} если x и y лежат в заданном диапазоне, {@code false} если не лежат
     */
    public boolean contains(
        double checkX,
        double checkY,
        double minX,
        double maxX,
        double minY,
        double maxY) {
        return (checkX <= maxX && checkY <= maxY)
            && (checkX >= minX && checkY >= minY);
    }

    /**
     * Проверяет, что переданные компоненты цвета лежат в диапазоне [0, 255]
     * @param red значение компонента красного
     * @param green значение компоненты зеленого
     * @param blue значение компоненты синего
     * @return {@code true} если каждая из переданных значений компонент лежит в диапазоне [0, 255],
     * {@code false} если не лежит
     * @see Check#isColorCorrect(int)
     */
    public boolean areColorsCorrect(int red, int green, int blue) {
        return isColorCorrect(red) && isColorCorrect(blue) && isColorCorrect(green);
    }

    /**
     * Проверяет, что переданная компонента цвета лежит в диапазоне [0, 255]
     * @param color значение переданной компоненты
     * @return {@code true} если переданное значение компоненты лежит в диапазоне [0, 255],
     * {@code false} если не лежит
     * @see Check#areColorsCorrect(int, int, int)
     */
    public boolean isColorCorrect(int color) {
        return color >= COLOR_MIN && color <= COLOR_MAX;
    }
}
