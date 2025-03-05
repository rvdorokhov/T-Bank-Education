package backend.academy.maze;

import java.util.Arrays;
import lombok.experimental.UtilityClass;

// Класс, содержащий массивы, которые содержат "разницы" координат между соседними клетками
@SuppressWarnings("ConstantName")
@UtilityClass
public final class Delta {
    static final int[] dX = {-2, 0, 2, 0};
    static final int[] dY = {0, 2, 0, -2};
    static final int[] dIntX = {-1, 0, 1, 0};
    static final int[] dIntY = {0, 1, 0, -1};

    static public int[] dX() {
        return Arrays.copyOf(dX, dX.length);
    }

    static public int[] dY() {
        return Arrays.copyOf(dY, dY.length);
    }

    static public int[] dIntX() {
        return Arrays.copyOf(dIntX, dIntX.length);
    }

    static public int[] dIntY() {
        return Arrays.copyOf(dIntY, dIntY.length);
    }
}
