package backend.academy.transformations.render;

import backend.academy.domain.model.FractalImage;
import backend.academy.domain.model.Pixel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GammaCorrection {
    /**
     * <p>Логарифмическая и гамма-коррекция изображения.</p>
     * <p>Отражает количество "попаданий" в пиксель (количество его изменений) и подавляет шумы</p>
     * @param canvas корректируемое изображение в формате экземпляра класса {@code FractalImage}
     * @param gammaCoef коэффициент гамма-коррекции (чем ниже тем сильнее коррекция)
     * @see FractalImage
     * @see Pixel
     */
    public static void gammaCorrection(FractalImage canvas, double gammaCoef) {
        double max = 0;
        for (int y = 0; y < canvas.height(); ++y) {
            for (int x = 0; x < canvas.width(); ++x) {
                int count = canvas.getPixel(x, y).hitCount();
                double logCount = Math.log10(count);
                if (count != 0 && logCount > max) {
                    max = logCount;
                }
            }
        }

        for (int y = 0; y < canvas.height(); ++y) {
            for (int x = 0; x < canvas.width(); ++x) {
                int count = canvas.getPixel(x, y).hitCount();
                double normal = Math.log10(count) / max;

                int newRed = (int) Math.round(canvas.getPixel(x, y).red()
                    * Math.pow(normal, (1 / gammaCoef)));
                int newGreen = (int) Math.round(canvas.getPixel(x, y).green()
                    * Math.pow(normal, (1 / gammaCoef)));
                int newBlue = (int) Math.round(canvas.getPixel(x, y).blue()
                    * Math.pow(normal, (1 / gammaCoef)));

                canvas.getPixel(x, y).setRGB(newRed, newGreen, newBlue);
            }
        }
    }
}
