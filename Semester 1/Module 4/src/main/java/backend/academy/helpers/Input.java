package backend.academy.helpers;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Function;
import lombok.experimental.UtilityClass;

/**
 * Вспомогательный класс, осуществляющий пользовательский ввод
 */
@UtilityClass
public class Input {
    /**
     * <p>Осуществляет пользовательский ввод типа {@code <T>}</p>
     * <p>Пример использования для чтения {@code Integer}</p>
     * <blockquote><pre>
     * int result = Input.makeChoose(
     *      scanner,
     *      printStream,
     *      "Try again",
     *      Integer::parseInt);
     * </pre></blockquote>
     * @param scanner сканнер потока ввода
     * @param printConsole поток вывода
     * @param message сообщение, выводимое в поток при ошибке ввода
     * @param parser функция-парсер строки в объект типа {@code T}
     * @return считанное с ввода значение типа {@code T}
     * @param <T> тип считываемого значения
     */
    public <T> T makeChoose(
        Scanner scanner,
        PrintStream printConsole,
        String message,
        Function<String, T> parser) {
        T result = null;
        boolean validInput;

        do {
            try {
                String choose = scanner.next().toLowerCase();
                result = parser.apply(choose);
                validInput = true;
            } catch (Exception e) {
                validInput = false;
                scanner.nextLine(); // "съедаем" всю введенную строку
                printConsole.println(message);
            }
        } while (!validInput);

        return result;
    }
}
