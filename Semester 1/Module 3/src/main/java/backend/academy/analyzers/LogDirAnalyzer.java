package backend.academy.analyzers;

import backend.academy.analyzers.interfaces.LogAnalyzer;
import backend.academy.analyzers.interfaces.ResourceAnalyzer;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>{@code  LogFileStatisticsAnalyzer} - анализатор директории с логами -
 * предоставляет API для рассчета статистики локальной директории с логами.
 * <p>Вид рассчитываемой статистикой зависит от типа {@code T} и реализации параметра {@code fileAnalyzer}
 * <p>Пример использования:
 * <blockquote><pre>
 * LogAnalyzer&lt;LogAnalysisResult&gt; logAnalyzer =
 *             new LogStatisticAnalyzer("GET", OffsetDateTime.MIN, OffsetDateTime.MAX);
 * ResourceAnalyzer&lt;LogAnalysisResult&gt; fileAnalyzer =
 *             new LogFileStatisticsAnalyzer<>(logAnalyzer);
 * LogAnalysisResult result = LogDirAnalyzer.analyze(fileAnalyzer, glob, path);
 * </pre></blockquote>
 * @see ResourceAnalyzer
 * @see LogAnalyzer
 * @see LogStatisticAnalyzer
 * @see LogFileStatisticsAnalyzer
 * @see LogAnalysisResult
 */
@UtilityClass
public class LogDirAnalyzer {
    private static final Logger LOGGER = LogManager.getLogger(LogDirAnalyzer.class);

    private static PathMatcher pathMatcher;

    /**
     * Принимает анализатор файла с логами, глоб-выражение, путь до файла или директории в качестве
     * и возвращает результат анализа переданного файла
     * (или всех подходящих под глоб-выражение файлов внутри переданной директории)
     * @param fileAnalyzer {@code ResourceAnalyzer<T>} - Анализатор файла логов, возвращающий результат типа T
     * @param glob {@code String} - Глоб-выражение для отбора файлов по имени
     * @param path {@code Path} - Путь до файла/директории
     * @return Результат анализа ресурса
     * @param <T> Тип возвращаемого значения
     * @throws IOException при возникновении IO-ошибок
     */
    public static <T> T analyze(
        ResourceAnalyzer<T> fileAnalyzer,
        String glob,
        Path path) throws IOException {

        pathMatcher = FileSystems.getDefault().getPathMatcher(glob);

        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                if (pathMatcher.matches(path) && Files.isRegularFile(path)) {
                    fileAnalyzer.analyze(path.toString());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                LOGGER.warn("Не удалось просмотреть файл {}", file.toString());
                return FileVisitResult.CONTINUE;
            }
        });

        return fileAnalyzer.result();
    }
}
