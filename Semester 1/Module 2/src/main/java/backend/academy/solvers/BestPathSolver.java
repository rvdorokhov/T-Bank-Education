package backend.academy.solvers;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;

public abstract class BestPathSolver extends Solver {
    protected double[][] distances;
    protected Coordinate[][] fromCells;
    protected final double inf = Double.POSITIVE_INFINITY; // условная константа бесконечности

    protected void restorePath() {
        if (visited[endY][endX]) {
            int curY = endY;
            int curX = endX;
            while ((curY != startY) || (curX != startX)) {
                resultPath.add(new Coordinate(curY, curX));
                Coordinate curCell = fromCells[curY][curX];
                curY = curCell.row();
                curX = curCell.col();
            }

            resultPath.add(new Coordinate(startY, startX));
        }
    }

    protected void init(Maze maze, Coordinate start, Coordinate end) {
        super.init(maze, start, end);

        fromCells = new Coordinate[maze.height()][maze.width()];

        distances = new double[maze.height()][maze.width()];
        for (int row = 0; row < maze.height(); ++row) {
            for (int col = 0; col < maze.width(); ++col) {
                distances[row][col] = inf;
                fromCells[row][col] = new Coordinate(-1, -1);
            }
        }
    }
}
