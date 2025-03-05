package backend.academy.transformations.render;

import backend.academy.domain.model.FractalImage;
import backend.academy.domain.model.Pixel;
import backend.academy.domain.model.Point;
import backend.academy.transformations.Transformation;
import backend.academy.transformations.affine.ImageAffineTransformation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@UtilityClass
public class RenderMultiThread {
    private final static Logger LOGGER = LogManager.getLogger(RenderMultiThread.class);

    /**
     * {@code X_MIN} {@code X_MAX} {@code Y_MIN} {@code Y_MAX}
     * - это коэффициенты нормализации.
     * При отрисовке точки преобразуются из нормализованных координат в пиксельные.
     * В частности от этих коэффициентов зависит ширина черных граничных полос изображения
     */
    private final static double X_MIN = -0.75;
    private final static double X_MAX = 0.75;
    private final static double Y_MIN = -1;
    private final static double Y_MAX = 1;
    private final static double GAMMA = 10.2;

    /**
     * Рендерит переданное в параметрах изображение в соответствии
     * с переданными в {@code affineVariations} и {@code nonLinearVariations}
     * преобразованиями
     * @param canvas изображение в формате экземпляра класса {@code FractalImage}
     * @param nonLinearVariations список нелинейных преборазований
     * @param affineVariations список афинных преобразований
     * @param samples количество начальных точек
     * @param iterPerSample количество итераций на каждую точку
     * @param colorChange BiFunction, описывающее правило изменения цвета
     *                    при повторном изменении пикселя
     * @see backend.academy.domain.model.FractalImage
     * @see Pixel
     * @see backend.academy.transformations.Transformation
     * @see backend.academy.transformations.affine.ImageAffineTransformation
     * @see RenderMultiThread#renderSample(FractalImage, List, List, int, BiFunction)
     */
    public static void render(
        FractalImage canvas,
        List<Transformation> nonLinearVariations,
        List<ImageAffineTransformation> affineVariations,
        int samples,
        int iterPerSample,
        BiFunction<Integer, Integer, Integer> colorChange,
        int numberOfPools
    ) {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfPools);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < samples; ++i) {
            futures.add(CompletableFuture.runAsync(() -> {
                renderSample(canvas, nonLinearVariations, affineVariations, iterPerSample, colorChange);
            }, executor).exceptionally(e -> {
                LOGGER.warn("Rendering error: {}", e.getLocalizedMessage(), e);
                return null;
            }));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();

        GammaCorrection.gammaCorrection(canvas, GAMMA);
    }

    /**
     * Рендерит изображение для конкретной начальной точки
     * @param canvas изображение в формате экземпляра класса {@code FractalImage}
     * @param nonLinearVariations список нелинейных преборазований
     * @param affineVariations список афинных преобразований
     * @param iterPerSample количество итераций на каждую точку
     * @param colorChange BiFunction, описывающее правило изменения цвета
     *                   при повторном изменении пикселя
     * @see RenderMultiThread#render(FractalImage, List, List, int, int, BiFunction, int)
     * @see RenderMultiThread#updateImage(FractalImage, Point, int, int, int, BiFunction)
     * @see Point
     * @see backend.academy.domain.model.FractalImage
     * @see Pixel
     * @see backend.academy.transformations.Transformation
     * @see backend.academy.transformations.affine.ImageAffineTransformation
     */
    private static void renderSample(
        FractalImage canvas,
        List<Transformation> nonLinearVariations,
        List<ImageAffineTransformation> affineVariations,
        int iterPerSample,
        BiFunction<Integer, Integer, Integer> colorChange) {
        double newX = ThreadLocalRandom.current().nextDouble(X_MIN, X_MAX);
        double newY = ThreadLocalRandom.current().nextDouble(Y_MIN, Y_MAX);
        for (int step = 0; step < iterPerSample; ++step) {
            // выбор случайных преобразований из предложенных
            int indLinear = ThreadLocalRandom.current().nextInt(affineVariations.size());
            int indNonLinear = ThreadLocalRandom.current().nextInt(nonLinearVariations.size());
            Transformation nonLinearTrans = nonLinearVariations.get(indNonLinear);
            ImageAffineTransformation linearTrans = affineVariations.get(indLinear);

            // линейное преобразование
            Point point = linearTrans.transfrom(newX, newY);

            // нелинейное преобразование
            point = nonLinearTrans.transfrom(point.x(), point.y());

            updateImage(canvas, point, linearTrans.red(), linearTrans.green(), linearTrans.blue(), colorChange);
        }
    }

    /**
     * Преобразует "математические" координаты, лежащие в диапазоне [X_MIN. X_MAX], [Y_MIN, Y_MAX],в реальные
     * и изменяет цветовое значение пикселя по координатам
     * @param canvas изображение в формате экземпляра класса {@code FractalImage}
     * @param point координаты точки в "матетических" координатах
     * @param red значение красной компоненты
     * @param green значение зеленой компоненты
     * @param blue значение синей компоненты
     * @param colorChange BiFunction, описывающее правило изменения цвета
     *                 при повторном изменении пикселя
     * @see Point
     * @see backend.academy.domain.model.FractalImage
     * @see Pixel
     * @see RenderMultiThread#render(FractalImage, List, List, int, int, BiFunction, int)
     * @see backend.academy.transformations.render.RenderMultiThread#renderSample
     */
    private static void updateImage(
        FractalImage canvas,
        Point point,
        int red,
        int green,
        int blue,
        BiFunction<Integer, Integer, Integer> colorChange) {
        // Вычисляем координаты из "математичсеких" в "реальные"
        int width = canvas.width();
        int height = canvas.height();
        int x = (int) (width - Math.round((X_MAX - point.x()) / (X_MAX - X_MIN) * width));
        int y = (int) (height - Math.round((Y_MAX - point.y()) / (Y_MAX - Y_MIN) * height));

        if (canvas.contains(x, y)) {
            if (canvas.getPixel(x, y).hitCount() == 0) {
                canvas.getPixel(x, y).setRGB(red, green, blue);
            } else {
                canvas.getPixel(x, y).changeRGB(
                    colorChange, red, green, blue);
            }
        }
    }
}
