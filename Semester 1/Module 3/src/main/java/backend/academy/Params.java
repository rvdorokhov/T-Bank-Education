package backend.academy;

import backend.academy.helpers.InputCheckHelper;
import com.beust.jcommander.Parameter;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Params {
    @Parameter(names = "--path", required = true,
        description = "Путь до файла или директории, в которой осуществляется поиск файлов")
    private String path;

    @Parameter(names = "--glob", description = "Глоб-выражение для мэтча файлов")
    private String glob = "**/*";

    @Parameter(names = "--filter-value", description = "Фильтр по ключевому слову")
    private String value = "";

    @Parameter(names = "--format", description = "Формат вывода - в консоль (по умолчанию), markdown, adoc")
    private String format = "console";

    @Parameter(names = "--language", description = "Язык вывода - на данный момент доступен только ru")
    private String language = "ru";

    @Parameter(names = "--result-path", description = "Формат вывода - в консоль (по умолчанию), markdown, adoc")
    private String resultPath = null;

    private OffsetDateTime to = OffsetDateTime.MAX;

    @Parameter(names = "--time-to", description = "Фильтр-самое \"новое\" допустимое время")
    public void to(String value) {
        this.to = InputCheckHelper.checkDateTime(value);
    }

    private OffsetDateTime from = OffsetDateTime.MIN;

    @Parameter(names = "--time-from", description = "Фильтр-самое \"старое\" допустимое время")
    public void from(String value) {
        this.from = InputCheckHelper.checkDateTime(value);
    }
}
