package backend.academy.solvers;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Delta;
import backend.academy.maze.Maze;
import backend.academy.maze.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public abstract class Solver {
    protected boolean[][] visited;
    protected Deque<Coordinate> resultPath;
    protected int time;
    protected Maze maze;
    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;

    protected void init(Maze maze, Coordinate start, Coordinate end) {
        this.maze = maze;
        this.startY = start.row();
        this.startX = start.col();
        this.endY = end.row();
        this.endX = end.col();
        time = 0;

        visited = new boolean[maze.height()][maze.width()];
        resultPath = new ArrayDeque<>();

        for (int row = 0; row < maze.height(); ++row) {
            for (int col = 0; col < maze.width(); ++col) {
                visited[row][col] = false;
            }
        }
    }

    protected void calculateTime() {
        for (Coordinate cell : resultPath) {
            time += maze.grid()[cell.row()][cell.col()].type().cost();
        }
    }

    // Возвращает список соседних клеток
    protected List<Integer> getNeighbours(Coordinate cell) {
        int y = cell.row();
        int x = cell.col();
        List<Integer> neighbours = new ArrayList<>();
        for (int i = 0; i < Delta.dIntX().length; ++i) {
            int checkY = y + Delta.dIntY()[i];
            int checkX = x + Delta.dIntX()[i];
            if (checkCell(new Coordinate(checkY, checkX))) {
                neighbours.add(i);
            }
        }

        return neighbours;
    }

    // Проверка, что клетка находится внутри границ, что она - не стенка и что она еще не посещена
    protected boolean checkCell(Coordinate cell) {
        int y = cell.row();
        int x = cell.col();
        return maze.checkCell(cell) && !visited[y][x]
            && maze.grid()[y][x].type() != Type.WALL;
    }

    protected void visitCell(Coordinate cell) {
        visited[cell.row()][cell.col()] = true;
    }

    public abstract Path solve(Maze maze, Coordinate start, Coordinate end);
}
