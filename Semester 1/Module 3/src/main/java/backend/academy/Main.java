package backend.academy;

import backend.academy.analyzers.LogAnalysisResult;
import backend.academy.analyzers.LogDirAnalyzer;
import backend.academy.analyzers.LogFileStatisticsAnalyzer;
import backend.academy.analyzers.LogStatisticAnalyzer;
import backend.academy.analyzers.interfaces.LogAnalyzer;
import backend.academy.analyzers.interfaces.ResourceAnalyzer;
import backend.academy.helpers.ResourceCheckHelper;
import backend.academy.writers.StatsWriter;
import com.beust.jcommander.JCommander;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class Main {
    private final static String MARKDOWN = "markdown";
    private final static String ADOC = "adoc";
    private final static String CONSOLE = "console";

    public static void main(String[] args) throws URISyntaxException, IOException {
        Params params = new Params();

        JCommander.newBuilder()
            .addObject(params)
            .build()
            .parse(args);
        log.info(params);


        if (!params.format().equals(CONSOLE) && params.resultPath() == null) {
            throw new IllegalArgumentException(
                "The --result-path parameter is required if --format is specified");
        }

        LogAnalyzer<LogAnalysisResult> logAnalyzer =
            new LogStatisticAnalyzer(params.value(), params.from(), params.to());
        ResourceAnalyzer<LogAnalysisResult> fileAnalyzer =
            new LogFileStatisticsAnalyzer<>(logAnalyzer);

        Path path = ResourceCheckHelper.checkPath(params.path());
        URI uri = ResourceCheckHelper.checkURI(params.path());

        LogAnalysisResult result;
        if (uri != null) {
            result = fileAnalyzer.analyze(params.path());
        } else if (path != null) {
            result = LogDirAnalyzer.analyze(fileAnalyzer, "glob:" + params.glob(), path);
        } else {
            log.error("Ресурс {} не является ни локальным, ни удаленным", params.path());
            throw new IllegalArgumentException("Не удалось считать ресурс");
        }

        StatsWriter statsWriter;
        switch (params.format()) {
            case CONSOLE:
                statsWriter = new StatsWriter(MARKDOWN, params.language()); // Выводится в консоль в разметке markdown
                try (OutputStreamWriter outputStream = new OutputStreamWriter(System.out, StandardCharsets.UTF_8)) {
                    statsWriter.write(result, outputStream);
                }
                statsWriter.write(result, new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
                break;
            case MARKDOWN, ADOC:
                statsWriter = new StatsWriter(params.format(), params.language());
                try (OutputStreamWriter outputStream = new OutputStreamWriter(
                    new FileOutputStream(params.resultPath()), StandardCharsets.UTF_8)) {
                    statsWriter.write(result, outputStream);
                }
                break;
            default:
                throw new IllegalArgumentException("Неверный формат вывода");
        }
    }
}
