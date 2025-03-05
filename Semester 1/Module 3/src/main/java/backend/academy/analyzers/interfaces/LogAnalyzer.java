package backend.academy.analyzers.interfaces;

import java.io.BufferedReader;

/**
 * {@code LogAnalyzer<T>} предоставляет метод {@code analyzeLogs(BufferedReader)}
 * для анализа потока логов
 * @param <T> Тип возвращаемой статистики
 */
public interface LogAnalyzer<T> {
    T analyzeLogs(BufferedReader reader);
}
