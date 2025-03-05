package backend.academy.domain.model;

import backend.academy.helpers.Check;
import lombok.Getter;

/**
 * Описывает изображение, состоящее из пикселей
 * @see Pixel
 */
public class FractalImage {
    protected Pixel[][] pixels;
    @Getter protected int width;
    @Getter protected int height;

    /**
     * <p>Возвращает пиксель по заданными координатам</p>
     * <p>Начало оси координат в левом верхнем углу изображения, ось X направлена влево, ось Y направлена вниз</p>
     * @param x координата x
     * @param y координата y
     * @see Pixel
     * @return объект {@code Pixel} - пиксель с координатами x, y
     */
    public Pixel getPixel(int x, int y) {
        return pixels[y][x];
    }

    /**
     * <p>Проверяет принадлежность точки с переданными в параметрах координатами к изображению</p>
     * <p>Другими словами - проверяет, лежит ли точка в гранизах изображения</p>
     * <p>Начало оси координата в левом верхнем углу изображения, ось X направлена влево, ось Y направлена вниз</p>
     * @param x координата x
     * @param y координата y
     * @return {@code true} - если точка принадлежит изображению,{@code false} - если не принадлежит
     */
    public boolean contains(int x, int y) {
        return Check.contains(x, y, 0, width - 1, 0, height - 1);
    }

    /**
     * Конструктор, создающий изображение, заполненное пикселями с значениями по-умолчанию
     * @param width ширина изображения
     * @param height высота изображения
     * @see Pixel
     */
    public FractalImage(int width, int height) {
        pixels = new Pixel[height][width];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixels[y][x] = new Pixel();
            }
        }

        this.width = width;
        this.height = height;
    }
}
