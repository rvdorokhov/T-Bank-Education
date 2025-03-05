package backend.academy.analyzers;

import backend.academy.analyzers.interfaces.LogAnalyzer;
import backend.academy.analyzers.interfaces.ResourceAnalyzer;
import backend.academy.helpers.ResourceCheckHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>{@code  LogFileStatisticsAnalyzer} - анализатор локального или удаленного ресурса с логами
 * - предоставляет API для рассчета статистики локального или удаленного ресурса с логами.
 * <p>Вид рассчитываемой статистикой зависит от типа {@code T} и реализации поля {@code logAnalyzer}
 * <p>Пример использования:
 * <blockquote><pre>
 * LogAnalyzer&lt;LogAnalysisResult&gt; logAnalyzer =
 *             new LogStatisticAnalyzer("GET", OffsetDateTime.MIN, OffsetDateTime.MAX);
 * ResourceAnalyzer&lt;LogAnalysisResult&gt; fileAnalyzer =
 *             new LogFileStatisticsAnalyzer<>(logAnalyzer);
 * LogAnalysisResult result = fileAnalyzer.analyze(resource);
 * </pre></blockquote>
 * @see ResourceAnalyzer
 * @see LogAnalyzer
 * @see LogStatisticAnalyzer
 * @see LogAnalysisResult
 * @param <T> тип возвращаемого значения
 */
public class LogFileStatisticsAnalyzer<T> implements ResourceAnalyzer<T> {
    protected static final Logger LOGGER = LogManager.getLogger(LogFileStatisticsAnalyzer.class);

    protected LogAnalyzer<T> logAnalyzer;
    @Getter protected T result;

    /**
     * Принимает ресурс в качестве строки {@code String} и возвращает результат анализа ресурса
     * @param resource  {@code String}
     * @return результат анализа ресурса, если ресурс передан корректно
     * <p>{@code null}, если ресурс передан некорректно или его не существует
     * @see LogFileStatisticsAnalyzer#analyze(String)
     * @see LogFileStatisticsAnalyzer#analyze(Path)
     */
    public T analyze(String resource) {
        Path path = ResourceCheckHelper.checkPath(resource);
        URI uri = ResourceCheckHelper.checkURI(resource);

        if (uri != null) {
            return analyze(uri);
        } else if (path != null) {
            return analyze(path);
        } else {
            LOGGER.warn("Ресурс {} не является ни локальным, ни удаленным", resource);
            return null;
        }
    }

    /**
     * Принимает ресурс в качестве {@code Path} и возвращает результат анализа ресурса
     * @param pathToFile  {@code Path} - путь до файла
     * @return результат анализа ресурса, если ресурс передан корректно
     * <p>{@code null}, если при чтении ресурса возникла ошибка
     * @see LogFileStatisticsAnalyzer#analyze(String)
     */
    protected T analyze(Path pathToFile) {
        try (BufferedReader reader = Files.newBufferedReader(pathToFile)) {
            result = logAnalyzer.analyzeLogs(reader);
        } catch (IOException e) {
            LOGGER.error("Ошибка при открытии файла", e);
            result = null;
        }

        return result;
    }

    /**
     * <p>Работает асинхронно
     * <p>Принимает ресурс в качестве {@code URI} и возвращает результат анализа ресурса.
     * @param uri  {@code URI} - путь до файла
     * @return результат анализа ресурса, если ресурс передан корректно
     * <p>{@code null}, если при чтении ресурса возникла ошибка
     * @see LogFileStatisticsAnalyzer#analyze(String)
     */
    protected T analyze(URI uri) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

            result = client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApplyAsync(response -> {
                    try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(response.body()))) {
                        return logAnalyzer.analyzeLogs(bufferedReader);
                    } catch (IOException e) {
                        LOGGER.error("Ошибка при получении потока данных с удаленного файла", e);
                        return null;
                    }
                }).join();
        } catch (Exception e) {
            LOGGER.error("Ошибка при установке соединения с удаленным файлом", e);
            result = null;
        }

        return result;
    }

    /**
     * <p>Инициализирует новый {@code LogFileStatisticsAnalyzer}
     * с указанным в параметре {@code LogAnalyzer<T>}
     * @param logAnalyzer {@code LogAnalyzer<T>} -
     * анализатор потока логов, возвращающий результат типа T
     */
    public LogFileStatisticsAnalyzer(LogAnalyzer<T> logAnalyzer) {
        this.logAnalyzer = logAnalyzer;
    }
}
