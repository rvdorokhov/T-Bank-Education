package backend.academy.solvers;

import backend.academy.maze.Coordinate;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.Getter;

public class Path {
    private final Deque<Coordinate> path;
    @Getter private final int time;

    public Path(Deque<Coordinate> path, int time) {
        this.path = new ArrayDeque<>(path);
        this.time = time;
    }

    public Deque<Coordinate> path() {
        return new ArrayDeque<>(path);
    }
}
