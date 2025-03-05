package backend.academy.solvers;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Delta;
import backend.academy.maze.Maze;
import java.util.ArrayDeque;
import java.util.Deque;

public class DFSSolver extends Solver {
    private Deque<Coordinate> path;

    public Path solve(Maze maze, Coordinate start, Coordinate end) {
        init(maze, start, end);

        dfs(start);

        calculateTime();

        return new Path(resultPath, time);
    }

    private void dfs(Coordinate cell) {
        int y = cell.row();
        int x = cell.col();

        visited[y][x] = true;
        path.add(new Coordinate(y, x));

        if (isEnd(cell)) {
            resultPath = new ArrayDeque<>(path);
        }

        for (int i = 0; i < Delta.dX().length; ++i) {
            int newY = y + Delta.dIntY()[i];
            int newX = x + Delta.dIntX()[i];
            Coordinate newCell = new Coordinate(newY, newX);
            if (checkCell(newCell)) {
                dfs(newCell);
            }
        }

        path.removeLast();
    }

    protected void init(Maze maze, Coordinate start, Coordinate end) {
        super.init(maze, start, end);

        path = new ArrayDeque<>();
    }

    private boolean isEnd(Coordinate cell) {
        int y = cell.row();
        int x = cell.col();

        return (y == endY) && (x == endX);
    }
}
