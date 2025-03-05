package helpers;

import java.util.HashMap;
import java.util.Map;
import backend.academy.helpers.MapHandlerHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapHandlerHelperTest {
    @Test
    void findMaxes1() {
        Map<String, Integer> data = new HashMap<>(Map.of(
            "source1", 20,
            "source2", 40,
            "source3", 60,
            "source4", 10,
            "source5", 100));

        Map<String, Integer> result = MapHandlerHelper.findMaxes(data);

        Map<String, Integer> waitFor = new HashMap<>(Map.of(
            "source2", 40,
            "source3", 60,
            "source5", 100));
        assertEquals(waitFor, result);
    }

    @Test
    void findMaxes2() {
        Map<String, Integer> data = new HashMap<>(Map.of(
            "source2", 20,
            "source5", 100));

        Map<String, Integer> result = MapHandlerHelper.findMaxes(data);

        Map<String, Integer> waitFor = new HashMap<>(Map.of(
            "source2", 20,
            "source5", 100));
        assertEquals(waitFor, result);
    }

    @Test
    void findMaxes3() {
        Map<String, Integer> data = new HashMap<>(Map.of(
            "source1", 20,
            "source2", 40,
            "source3", 60,
            "source4", 100,
            "source5", 100));

        Map<String, Integer> result = MapHandlerHelper.findMaxes(data);

        Map<String, Integer> waitFor = new HashMap<>(Map.of(
            "source3", 60,
            "source4", 100,
            "source5", 100));
        assertEquals(waitFor, result);
    }
}
