package backend.academy.exeptions;

import backend.academy.domain.model.FractalImage;
import backend.academy.domain.model.Pixel;
import backend.academy.transformations.affine.ImageAffineTransformation;

/**
 * Сигнализирует о некорректном значении цветов RGB
 * @see ImageAffineTransformation
 * @see Pixel
 * @see FractalImage
 */
public class IllegalRGBValue extends RuntimeException {
    public IllegalRGBValue(
        int red,
        int green,
        int blue) {
        super(String.format(
            "The values red %d, green %d, blue %d "
                + "must be in the range [0, 255]", red, green, blue));
    }
}
