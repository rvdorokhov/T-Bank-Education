package backend.academy.generators;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Delta;
import backend.academy.maze.Maze;
import backend.academy.maze.Type;
import java.util.ArrayList;
import java.util.List;

public class PrimsGenerator extends GeneratorWithBorders {
    public Maze generate(int height, int width) {
        buildWalls(height, width);

        chooseStartPos(height, width);

        // Список клеток, из которых можно построить проход
        List<Coordinate> curCells = new ArrayList<>(
            List.of(new Coordinate(startY, startX)));

        while (!curCells.isEmpty()) {
            Coordinate cell = curCells.get(random.nextInt(curCells.size()));

            int neighbour = random.nextInt(Delta.dX().length);
            int newY = cell.row() + Delta.dY()[neighbour];
            int newX = cell.col() + Delta.dX()[neighbour];
            Coordinate newCell = new Coordinate(newY, newX);

            if (checkWall(newCell)) {
                chooseType(newCell);
                int newIntY = newY - Delta.dIntY()[neighbour];
                int newIntX = newX - Delta.dIntX()[neighbour];
                chooseType(new Coordinate(newIntY, newIntX));

                curCells.add(new Coordinate(newY, newX));
            }

            if (!hasCells(cell)) {
                curCells.remove(cell);
            }
        }

        if (height % 2 == 0) {
            breakEvenWallsOnRows();
        }

        if (width % 2 == 0) {
            breakEvenWallsOnCols();
        }

        makeMazeNotIdeal();

        return new Maze(widthWithBorders, heightWithBorders, grid);
    }

    // Првоеряем, остались ли еще соседние клетки, которые можно использовать для генерации прохода
    private boolean hasCells(Coordinate cell) {
        boolean hasCells = false;
        for (int i = 0; i < Delta.dX().length; ++i) {
            int newY = cell.row() + Delta.dY()[i];
            int newX = cell.col() + Delta.dX()[i];
            if (checkWall(new Coordinate(newY, newX))) {
                hasCells = true;
            }
        }

        return hasCells;
    }

    // Проверка, что клетка находится внутри границ и является стеной
    public boolean checkWall(Coordinate cell) {
        return maze.checkCell(cell) && grid[cell.row()][cell.col()].type() == Type.WALL;
    }
}
