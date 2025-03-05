package backend.academy.generators;

import backend.academy.maze.Cell;
import backend.academy.maze.Coordinate;
import backend.academy.maze.Maze;
import backend.academy.maze.Type;

public abstract class GeneratorWithBorders extends Generator {
    protected int heightWithBorders;
    protected int widthWithBorders;

    private final static int ONE_HUNDRED_RATE = 101;
    private final static String RATE_OF_BREAK_EVEN_WALL = "rate.of.break.even.wall";
    private final static String RATE_OF_BREAK_THE_WALL = "rate.of.break.the.wall";

    // Генерация проходов в крайнем правом столбце в случае четной размернсоти лабиринта
    protected void breakEvenWallsOnCols() {
        int col = widthWithBorders - 2;
        for (int row = 1; row < heightWithBorders - 1; ++row) {
            Coordinate cell = new Coordinate(row, col);
            if (shouldBreak(cell)) {
                chooseType(cell);
            }
        }
    }

    // Генерация проходов в крайнем нижнем ряду в случае четной размернсоти лабиринта
    protected  void breakEvenWallsOnRows() {
        int row = heightWithBorders - 2;
        for (int col = 1; col < widthWithBorders - 1; ++col) {
            Coordinate cell = new Coordinate(row, col);
            if (shouldBreak(cell)) {
                chooseType(cell);
            }
        }
    }

    // "бросаем монетку" - ломаем стену или нет и проверяем, что к убираемой стенке есть проход
    private boolean shouldBreak(Coordinate cell) {
        int y = cell.row();
        int x = cell.col();
        int rate = Integer.parseInt(settings.getProperty(RATE_OF_BREAK_EVEN_WALL));
        return (random.nextInt(ONE_HUNDRED_RATE) <= rate)
            && ((grid[y - 1][x].type() != Type.WALL) || (grid[y][x - 1].type() != Type.WALL));
    }

    // Заполнение всей сетки стенами
    protected void buildWalls(int height, int width) {
        heightWithBorders = height + 2;
        widthWithBorders = width + 2;

        grid = new Cell[heightWithBorders][widthWithBorders];
        maze = new Maze(widthWithBorders, heightWithBorders, grid);

        for (int row = 0; row < heightWithBorders; ++row) {
            for (int col = 0; col < widthWithBorders; ++col) {
                grid[row][col] = new Cell(Type.WALL);
            }
        }
    }

    // Сделаем лабиринт не идеальным - пройдемся по всем клеткам и произвольно удалим стенки
    protected void makeMazeNotIdeal() {
        int rate = Integer.parseInt(settings.getProperty(RATE_OF_BREAK_THE_WALL));
        for (int row = 1; row < heightWithBorders - 1; ++row) {
            for (int col = 1; col < widthWithBorders - 1; ++col) {
                if ((grid[row][col].type() == Type.WALL)
                    && (random.nextInt(ONE_HUNDRED_RATE) <= rate)) {
                    grid[row][col].type(Type.PASSAGE);
                }
            }
        }
    }

    protected void chooseStartPos(int height, int width) {
        super.chooseStartPos(height, width);
        startX++; // прибавляем координатам по 1, чтобы учитывать граничные стенки
        startY++;
        grid[startY][startX].type(Type.PASSAGE);
    }

    abstract public Maze generate(int height, int width);
}
