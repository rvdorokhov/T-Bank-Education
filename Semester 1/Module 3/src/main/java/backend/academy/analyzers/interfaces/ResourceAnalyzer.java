package backend.academy.analyzers.interfaces;

/**
 * <p>{@code ResourceAnalyzer<T>} предоставляет:
 * <ul>
 *   <li>Метод {@code analyze(String)} для анализа потока логов</li>
 *   <li>Метод {@code  result()} для получения результата анализа потока логов</li>
 * </ul>
 * @param <T> Тип возвращаемой статистики
 */
public interface ResourceAnalyzer<T> {
    T analyze(String resource);

    T result();
}
