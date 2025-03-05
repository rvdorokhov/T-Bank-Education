package backend.academy.generators;

import backend.academy.maze.Coordinate;
import backend.academy.maze.Delta;
import backend.academy.maze.Maze;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DFSGenerator extends GeneratorWithBorders {
    private int curX;
    private int curY;
    private Coordinate curCell;
    private boolean[][] passedCells;

    public Maze generate(int height, int width) {
        buildWalls(height, width);

        chooseStartPos(height, width);

        setPassedCells();

        changeCur(startY, startX);

        Stack<Coordinate> passedCellsStack = new Stack<>();
        passedCellsStack.push(new Coordinate(startY, startX));
        // dNeighbours - индексы для массивов dX, dY и dIntX и dIntY доступных соседей
        List<Integer> dNeighbours = getNeighbours(curCell);
        while (!passedCellsStack.empty()) {
            Coordinate prevCell;
            while (!dNeighbours.isEmpty()) {
                int dInd = dNeighbours.get(random.nextInt(dNeighbours.size()));
                // intY и intX - "сквозные" клетки между текущей и выбранной для генерации
                int intY = curY + Delta.dIntY()[dInd];
                int intX = curX + Delta.dIntX()[dInd];
                Coordinate intCell = new Coordinate(intY, intX);

                chooseType(curCell);
                chooseType(intCell);

                passedCellsStack.push(curCell);
                passedCells[curY][curX] = true;

                changeCur(curY + Delta.dY()[dInd], curX + Delta.dX()[dInd]);

                dNeighbours = getNeighbours(curCell);
            }

            // Дополнительная проверка последней посещенной клетки
            if (maze.checkCell(curCell)) {
                chooseType(curCell);
                passedCells[curY][curX] = true;
            }

            prevCell = passedCellsStack.pop();
            changeCur(prevCell.row(), prevCell.col());
            dNeighbours = getNeighbours(curCell);
        }

        makeMazeNotIdeal();

        if (height % 2 == 0) {
            breakEvenWallsOnRows();
        }

        if (width % 2 == 0) {
            breakEvenWallsOnCols();
        }

        return new Maze(widthWithBorders, heightWithBorders, grid);
    }

    // Возвращает список "соседей" - клеток, которые можно использовать для генерации прохода
    private List<Integer> getNeighbours(Coordinate cell) {
        int y = cell.row();
        int x = cell.col();
        List<Integer> neighbours = new ArrayList<>();
        for (int i = 0; i < Delta.dX().length; ++i) {
            int checkY = y + Delta.dY()[i];
            int checkX = x + Delta.dX()[i];
            if (maze.checkCell(new Coordinate(checkY, checkX)) && !passedCells[checkY][checkX]) {
                neighbours.add(i);
            }
        }

        return neighbours;
    }

    // Инициализация passedCells значениями false
    private void setPassedCells() {
        passedCells = new boolean[heightWithBorders][widthWithBorders];
    }

    private void changeCur(int y, int x) {
        curY = y;
        curX = x;
        curCell = new Coordinate(curY, curX);
    }
}
