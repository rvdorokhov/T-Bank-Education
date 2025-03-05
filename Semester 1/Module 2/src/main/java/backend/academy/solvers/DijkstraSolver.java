package backend.academy.solvers;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Delta;
import backend.academy.maze.Maze;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DijkstraSolver extends BestPathSolver {
    private Set<Coordinate> curCells;
    private Coordinate curMin;

    public Path solve(Maze maze, Coordinate start, Coordinate end) {
        init(maze, start, end);

        visitCell(start);
        distances[startY][startX] = 0;

        curCells.add(start);
        curMin = start;
        while (!curCells.isEmpty()) {
            relaxation();

            chooseMin();
        }

        restorePath();
        calculateTime();

        return new Path(resultPath, time);
    }

    private void relaxation() {
        List<Integer> neighbours = getNeighbours(curMin);
        for (int ind : neighbours) {
            Coordinate neighbour = new Coordinate(
                curMin.row() + Delta.dIntY()[ind], curMin.col() + Delta.dIntX()[ind]);
            relaxCell(curMin, neighbour);
            curCells.add(neighbour);
        }
        curCells.remove(curMin);
    }

    private void relaxCell(Coordinate from, Coordinate to) {
        int fromY = from.row();
        int fromX = from.col();
        int toY = to.row();
        int toX = to.col();

        double newDistance = distances[fromY][fromX] + maze.grid()[toY][toX].type().cost();
        if (newDistance < distances[toY][toX]) {
            distances[toY][toX] = newDistance;
            fromCells[toY][toX] = new Coordinate(fromY, fromX);
        }
    }

    private void chooseMin() {
        double minimDistance = inf;
        for (Coordinate cell : curCells) {
            int y = cell.row();
            int x = cell.col();
            if ((distances[y][x] < minimDistance) && (!visited[y][x])) {
                minimDistance = distances[y][x];
                curMin = new Coordinate(y, x);
            }
        }

        visitCell(curMin);
    }

    protected void init(Maze maze, Coordinate start, Coordinate end) {
        super.init(maze, start, end);
        curCells = new HashSet<>();
    }
}
