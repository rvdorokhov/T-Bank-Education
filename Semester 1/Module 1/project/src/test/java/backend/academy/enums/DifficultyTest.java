package backend.academy.enums;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DifficultyTest {
    @Test
    void getRandomDifficultyTest() {
        ArrayList<Difficulty> difficulties = new ArrayList<>(List.of(Difficulty.values()));

        Difficulty result = Difficulty.getRandomDifficulty();

        assertTrue(difficulties.contains(result));
    }
}
