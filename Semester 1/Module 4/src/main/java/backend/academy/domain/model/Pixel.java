package backend.academy.domain.model;

import backend.academy.exeptions.IllegalRGBValue;
import backend.academy.helpers.Check;
import java.util.function.BiFunction;
import lombok.Getter;

/**
 * Описывает пиксель (точку) некоторого изображения без привязки к координам
 * @see FractalImage
 */
@Getter
public class Pixel {
    protected int red = 0;
    protected int green = 0;
    protected int blue = 0;
    /** Счетчик "попаданий" в пиксель - сколько раз данный пиксель был изменен */
    protected int hitCount = 0;

    /**
     * Изменяет значения компонент цвета на переданные в параметрах
     * @param red новое значение красной компоненты
     * @param green новое значение зеленой компоненты
     * @param blue новое значение синей компоненты
     * @throws IllegalRGBValue если переданные в параметрах значения RGB не находятся в диапазоне [0, 255]
     */
    public synchronized void setRGB(int red, int green, int blue) throws IllegalRGBValue {
        if (!Check.areColorsCorrect(red, green, blue)) {
            throw new IllegalRGBValue(red, green, blue);
        } else {
            this.red = red;
            this.green = green;
            this.blue = blue;
            hitCount += 1;
        }
    }

    /**
     * Изменяет значения компонент цвета в соответствии с переданными в параметрах компонентами
     * применением переданной функции
     * @param changeColor BiFunction, описывающее правило изменения
     * @param red новое значение красной компоненты
     * @param green новое значение зеленой компоненты
     * @param blue новое значение синей компоненты
     * @throws IllegalRGBValue если переданные в параметрах значения RGB не находятся в диапазоне [0, 255]
     * @see Pixel#setRGB(int, int, int)
     */
    public synchronized void changeRGB(
        BiFunction<Integer, Integer, Integer> changeColor,
        int red,
        int green,
        int blue) {
        setRGB(changeColor.apply(red, this.red),
            changeColor.apply(green, this.green),
            changeColor.apply(blue, this.blue));
    }
}
