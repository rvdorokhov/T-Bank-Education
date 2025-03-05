package backend.academy.transformations.affine;

import backend.academy.exeptions.IllegalRGBValue;
import backend.academy.helpers.Check;
import lombok.Getter;

/**
 * Представляет собой афинное преобразование с дополнительными полями, описывающими цвет
 * @see AffineTransformation
 * @see IllegalRGBValue
 */
@Getter
@SuppressWarnings("ParameterNumber")
public class ImageAffineTransformation extends AffineTransformation {
    protected final int red;
    protected final int green;
    protected final int blue;

    /**
     * Конструктор, создающий объект с заданными параметрами
     * @param a коэффициент a
     * @param b коэффициент b
     * @param c коэффициент c
     * @param d коэффициент d
     * @param e коэффициент e
     * @param f коэффициент f
     * @param red значение компоненты красного
     * @param green значение компоненты зеленого
     * @param blue значение компоненты синего
     * @throws IllegalRGBValue если значения компонент RGB не лежат в диапазоне [0, 255]
     * @see Check#areColorsCorrect(int, int, int)
     */
    public ImageAffineTransformation(
        double a,
        double b,
        double c,
        double d,
        double e,
        double f,
        int red,
        int green,
        int blue
    ) {
        super(a, b, c, d, e, f);

        if (!Check.areColorsCorrect(red, green, blue)) {
            throw new IllegalRGBValue(red, green, blue);
        } else {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }
}
