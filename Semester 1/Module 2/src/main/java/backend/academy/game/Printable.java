package backend.academy.game;

import backend.academy.maze.Maze;
import backend.academy.solvers.Path;

public interface Printable {
    void print(Maze maze);

    void print(Maze maze, Path path);
}
