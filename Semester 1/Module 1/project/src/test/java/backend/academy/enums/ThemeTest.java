package backend.academy.enums;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThemeTest {
    @Test
    void getRandomThemeTest() {
        ArrayList<Theme> themes = new ArrayList<>(List.of(Theme.values()));

        Theme result = Theme.getRandomTheme();

        assertTrue(themes.contains(result));
    }
}
