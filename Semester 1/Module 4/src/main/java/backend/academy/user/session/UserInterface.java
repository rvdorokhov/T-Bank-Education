package backend.academy.user.session;

import backend.academy.domain.model.FractalImage;
import backend.academy.helpers.Input;
import backend.academy.helpers.Save;
import backend.academy.transformations.Transformation;
import backend.academy.transformations.affine.ImageAffineTransformation;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Абстрактный класс, описывающий основные поля и методы пользовательского консольного интерфейса
 */
public abstract class UserInterface {
    protected final static int CHOOSE_ADD_NON_LINEAR = 1;
    protected final static int CHOOSE_ADD_AFFINE = 2;
    protected final static int CHOOSE_PAINT = 3;
    protected final static int CHOOSE_END = 4;

    protected List<Transformation> nonLinearVariations;
    protected List<Transformation> choosedNonLinearVariations;
    protected List<ImageAffineTransformation> affineVariations;

    protected PrintStream printStream;
    protected InputStream inputStream;
    protected Scanner scanner;
    protected Properties prints;

    // ключи для properties-файла
    protected final static String START_MENU = "start.menu";
    protected final static String ADD_AFFINE_MESSAGE = "add.affine.message";
    protected final static String ADD_NON_LINEAR_MESSAGE = "add.non.linear.message";
    protected final static String WRONG_RGB = "wrong.rgb";
    protected final static String WRONG_COEFS = "wrong.coefs";
    protected final static String FILE_PATH_MESSAGE = "file.path.message";
    protected final static String WRITING_TO_FILE_ERROR = "writing.to.file.error";

    protected final static String START_MENU_TRY_AGAIN = "start.menu.try.again";
    protected final static String ADD_NON_LINEAR_TRY_AGAIN = "add.non.linear.try.again";

    protected final static String HEIGHT_MESSAGE_TRY_AGAIN = "height.message.try.again";
    protected final static String WIDTH_MESSAGE_TRY_AGAIN = "width.message.try.again";
    protected final static String SAMPLE_MESSAGE_TRY_AGAIN = "sample.message.try.again";
    protected final static String ITER_PER_SAMPLE_MESSAGE_TRY_AGAIN = "iter.per.sample.message.try.again";

    protected final static String HEIGHT_MESSAGE = "height.message";
    protected final static String WIDTH_MESSAGE = "width.message";
    protected final static String SAMPLE_MESSAGE = "sample.message";
    protected final static String ITER_PER_SAMPLE_MESSAGE = "iter.per.sample.message";
    protected final static String VOID_TRANSFORMATORS = "void.transformators";

    protected final static String ONE_POOL = "one.pool";
    protected final static String MULTI_POOL = "multi.pool";
    protected final static String NO_MULTI_POOL = "without.completable.future";
    protected final static String WORKING = "working";

    protected final static Logger LOGGER = LogManager.getLogger(UserInterface.class);

    /**
     * Запускает процесс взаимодействия с пользователем
     */
    public void start() {
        int choose = 0;

        while (choose != CHOOSE_END) {
            printStream.println(prints.getProperty(START_MENU));
            choose = Input.makeChoose(
                scanner,
                printStream,
                prints.getProperty(START_MENU_TRY_AGAIN),
                Integer::parseInt);

            switch (choose) {
                case CHOOSE_ADD_NON_LINEAR:
                    addNonLinear();
                    break;
                case CHOOSE_ADD_AFFINE:
                    addAffine();
                    break;
                case CHOOSE_PAINT:
                    paint();
                    break;
                default:
                    if (choose != CHOOSE_END) {
                        LOGGER.warn("Unexpected input {}", choose);
                    }
            }
        }
    }

    /**
     * Сохраняет изображение по пути, полученному в {@code InputStream} пользователя
     * @param canvas сохраняемое изображения в формате объекта FractalImage
     */
    protected void save(FractalImage canvas) {
        boolean validInput = false;
        while (!validInput) {
            try {
                printStream.println(prints.getProperty(FILE_PATH_MESSAGE));
                scanner.nextLine();
                String pathString = scanner.nextLine();
                Path path = Path.of(pathString);
                Save.save(canvas, path);
                validInput = true;
            } catch (Exception e) {
                printStream.println(prints.getProperty(WRITING_TO_FILE_ERROR));
            }
        }
    }

    /**
     * <p>Добавлет в список преобразований, которые будут применяться
     * к изображению, нелинейное преобразование,
     * выбранное пользователем из заранее определенных</p>
     * <p>Выбираются из {@code backend.academy.user.session.UserInterface#nonLinearVariations}</p>
     */
    abstract protected void addNonLinear();

    /**
     * Добавлет в список преобразований афинное преобразование, введенное пользователем
     */
    abstract protected void addAffine();

    /**
     * Задает параметры изображения и рендерит его
     */
    abstract protected void paint();
}
