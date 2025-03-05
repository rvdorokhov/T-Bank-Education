package backend.academy.user.session;

import backend.academy.domain.model.FractalImage;
import backend.academy.exeptions.IllegalRGBValue;
import backend.academy.exeptions.InvalidAffineTransformationCoefs;
import backend.academy.helpers.ConfigOpen;
import backend.academy.helpers.Input;
import backend.academy.transformations.Transformation;
import backend.academy.transformations.affine.ImageAffineTransformation;
import backend.academy.transformations.render.RenderMultiThread;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Описывает поведение пользовательского консольного интерфейса
 * @see UserInterface
 */
public class UserSession extends UserInterface {
    private final static int POOLS_NUMBER = 12;

    /**
     * Добавлет в список преобразований, которые будут применяться
     * к изображению, нелинейное преобразование,
     * выбранное пользователем из заранее определенных
     */
    protected void addNonLinear() {
        for (int i = 0; i < nonLinearVariations.size(); ++i) {
            String className = nonLinearVariations
                .get(i)
                .getClass()
                .getName()
                .toLowerCase();
            printStream.print(i + 1 + ". ");
            printStream.println(prints.getProperty(className));
        }

        int choose;
        do {
            printStream.println(prints.getProperty(ADD_NON_LINEAR_MESSAGE));
            choose = Input.makeChoose(
                scanner,
                printStream,
                prints.getProperty(ADD_NON_LINEAR_TRY_AGAIN),
                Integer::parseInt);
        } while (choose < 1 || choose > nonLinearVariations.size());

        choosedNonLinearVariations.add(nonLinearVariations.get(choose - 1));
    }

    /**
     * Добавлет в список преобразований афинное преобразование, введенное пользователем
     */
    protected void addAffine() {
        printStream.println(prints.getProperty(ADD_AFFINE_MESSAGE));

        final int aInd = 0;
        final int bInd = 1;
        final int cInd = 2;
        final int dInd = 3;
        final int eInd = 4;
        final int fInd = 5;
        final int redInd = 6;
        final int greenInd = 7;
        final int blueInd = 8;
        final int coefNumber = 6;
        final int colorsNumber = 3;

        double[] values = new double[coefNumber + colorsNumber]; // 8 основных значений и 3 для red, green, blue
        String propertyMessage = "coef.try.again";

        for (int i = 0; i < coefNumber + colorsNumber; i++) {
            values[i] = Input.makeChoose(
                scanner,
                printStream,
                prints.getProperty(propertyMessage),
                Double::parseDouble);
        }

        double a = values[aInd];
        double b = values[bInd];
        double c = values[cInd];
        double d = values[dInd];
        double e = values[eInd];
        double f = values[fInd];
        int red = (int) values[redInd];
        int green = (int) values[greenInd];
        int blue = (int) values[blueInd];

        try {
            affineVariations.add(
                new ImageAffineTransformation(a, b, c, d, e, f, red, green, blue));
        } catch (IllegalRGBValue error) {
            printStream.println(prints.getProperty(WRONG_RGB));
            addAffine();
        } catch (InvalidAffineTransformationCoefs error) {
            printStream.println(prints.getProperty(WRONG_COEFS));
            addAffine();
        }
    }

    /**
     * Задает параметры изображения и рендерит его
     */
    protected void paint() {
        String[] messages = {
            HEIGHT_MESSAGE,
            WIDTH_MESSAGE,
            SAMPLE_MESSAGE,
            ITER_PER_SAMPLE_MESSAGE
        };

        String[] tryAgainMessages = {
            HEIGHT_MESSAGE_TRY_AGAIN,
            WIDTH_MESSAGE_TRY_AGAIN,
            SAMPLE_MESSAGE_TRY_AGAIN,
            ITER_PER_SAMPLE_MESSAGE_TRY_AGAIN
        };

        // Массив для хранения результатов
        int[] results = new int[messages.length];
        final int heightInd = 0;
        final int widthInd = 1;
        final int samplesInd = 2;
        final int iterPerSampleInd = 3;

        for (int i = 0; i < messages.length; i++) {
            printStream.println(prints.getProperty(messages[i]));
            results[i] = Input.makeChoose(
                scanner,
                printStream,
                prints.getProperty(tryAgainMessages[i]),
                Integer::parseInt);
        }

        int height = results[heightInd];
        int width = results[widthInd];
        int samples = results[samplesInd];
        int iterPerSample = results[iterPerSampleInd];

        printStream.println(prints.getProperty(WORKING));

        FractalImage canvas = new FractalImage(width, height);
        try {
            long startTime = System.currentTimeMillis();
            RenderMultiThread.render(
                canvas,
                choosedNonLinearVariations,
                affineVariations,
                samples,
                iterPerSample,
                (a, b) -> (a + b) / 2,
                1);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            printStream.println(prints.getProperty(NO_MULTI_POOL) + duration);

            startTime = System.currentTimeMillis();
            RenderMultiThread.render(
                canvas,
                choosedNonLinearVariations,
                affineVariations,
                samples,
                iterPerSample,
                (a, b) -> (a + b) / 2,
                POOLS_NUMBER);
            endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            printStream.println(prints.getProperty(MULTI_POOL) + duration);

            save(canvas);
        } catch (IllegalArgumentException e) {
            printStream.println(prints.getProperty(VOID_TRANSFORMATORS));
        }
    }

    /**
     * Конструктор, создающий пользовательскую сессию на переданных
     * поток ввода-вывода и с заданным списком нелинейных преобразований
     * @param printStream поток вывода
     * @param inputStream поток ввода
     * @param nonLinearVariations список заранее определнных нелинейных преобразований
     */
    public UserSession(
        PrintStream printStream,
        InputStream inputStream,
        List<Transformation> nonLinearVariations
    ) {
        this.printStream = printStream;
        this.inputStream = inputStream;
        this.affineVariations = new ArrayList<>();
        this.nonLinearVariations = new ArrayList<>(nonLinearVariations);
        this.scanner = new Scanner(inputStream);

        choosedNonLinearVariations = new ArrayList<>();
        this.prints = ConfigOpen.getPrints();
    }
}
