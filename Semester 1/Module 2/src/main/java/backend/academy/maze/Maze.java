package backend.academy.maze;

import java.util.Arrays;
import lombok.Getter;

@Getter
public class Maze {
    protected final int width;
    protected final int height;
    protected final Cell[][] grid;

    // Проверяем, что клетка находится внутри границ
    public boolean checkCell(Coordinate cell) {
        int y = cell.row();
        int x = cell.col();
        return ((y > 0) && (y < height - 1))
            && ((x > 0) && (x < width - 1));
    }

    public Maze(int width, int height, Cell[][] grid) {
        this.width = width;
        this.height = height;

        this.grid = new Cell[height][width];
        for (int row = 0; row < height; ++row) {
            this.grid[row] = Arrays.copyOf(grid[row], width);
        }
    }
}
