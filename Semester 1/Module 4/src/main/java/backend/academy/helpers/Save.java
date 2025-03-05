package backend.academy.helpers;

import backend.academy.domain.model.FractalImage;
import backend.academy.domain.model.Pixel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>Вспомогательный класс, сохраняющий изображение по указанному пути</p>
 * <p>Поддерживаемые форматы: .jpeg</p>
 * @see FractalImage
 */
@UtilityClass
public class Save {
    private final static Logger LOGGER = LogManager.getLogger(Save.class);
    private final static String FORMAT = "jpeg";

    /** Значения левого сдвига для компонент красного и зеленого цвета */
    private final static int RED_SHIFT = 16;
    private final static int GREEN_SHIFT = 8;

    /**
     * Сохраняет изображение по указанному пути в формате .jpeg
     * @param canvas изображения для сохранения
     * @param filename путь, по которому сохраняется изображение
     * @see FractalImage
     */
    public void save(FractalImage canvas, Path filename) {
        BufferedImage image = new BufferedImage(
            canvas.width(),
            canvas.height(),
            BufferedImage.TYPE_INT_RGB
        );

        for (int x = 0; x < canvas.width(); ++x) {
            for (int y = 0; y < canvas.height(); ++y) {
                Pixel pixel = canvas.getPixel(x, y);
                int red = pixel.red();
                int green = pixel.green();
                int blue = pixel.blue();

                int rgb = (red << RED_SHIFT) | (green << GREEN_SHIFT) | blue;

                image.setRGB(x, y, rgb);
            }
        }

        try {
            ImageIO.write(image, FORMAT, filename.toFile());
        } catch (IOException e) {
            LOGGER.error("Error writing to the file {}", filename.toString(), e);
        }
    }
}
