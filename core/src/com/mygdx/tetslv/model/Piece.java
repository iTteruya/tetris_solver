package com.mygdx.tetslv.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Random;
import static com.mygdx.tetslv.model.GameField.CELL_SIZE;
import static com.mygdx.tetslv.model.GameField.NUM_COLUMNS;
import static com.mygdx.tetslv.view.GameScreen.STAGE_START_X;
import static com.mygdx.tetslv.view.GameScreen.STAGE_START_Y;
import static com.mygdx.tetslv.view.GameScreen.NEXT_PIECE_BOXX;
import static com.mygdx.tetslv.view.GameScreen.NEXT_PIECE_BOXY;

public class Piece {
    int[][] matrix;
    private int x;
    private int y;
    private int deltaX;
    private int deltaY;
    private int currentRow;
    private int currentColumn;
    private final int ROWS = 4;
    private final int COLS = 4;

    public enum Type {
        T, O, S, L, I
    }

    private Enum<Type> pieceType;

    public Piece(int i, int j) {
        while (this.pieceType == null) {
            for (Enum<Type> e : Type.values()) {
                if (new Random().nextInt(10) == 1) {
                    this.pieceType = e;
                }
            }
        }
        init(i, j);
    }

    public Piece(Piece piece) {
        set(piece);
    }

    private void init(int i, int j) {
        currentRow = i;
        currentColumn = j;
        x = j * CELL_SIZE;
        y = i * CELL_SIZE;

        if (pieceType.equals(Type.I)) {
            deltaX = 60;
            deltaY = 950;
            matrix = new int[][]{
                    new int[]{1, 0, 0, 0},
                    new int[]{1, 0, 0, 0},
                    new int[]{1, 0, 0, 0},
                    new int[]{1, 0, 0, 0}};
        } else if (pieceType.equals(Type.L)) {
            deltaX = 48;
            deltaY = 930;
            matrix = new int[][]{
                    new int[]{1, 0, 0, 0},
                    new int[]{1, 0, 0, 0},
                    new int[]{1, 1, 0, 0},
                    new int[]{0, 0, 0, 0}};
        } else if (pieceType.equals(Type.S)) {
            deltaX = 28;
            deltaY = 916;
            matrix = new int[][]{
                    new int[]{0, 1, 1, 0},
                    new int[]{1, 1, 0, 0},
                    new int[]{0, 0, 0, 0},
                    new int[]{0, 0, 0, 0}};
        } else if (pieceType.equals(Type.T)) {
            deltaX = 28;
            deltaY = 916;
            matrix = new int[][]{
                    new int[]{0, 1, 0, 0},
                    new int[]{1, 1, 1, 0},
                    new int[]{0, 0, 0, 0},
                    new int[]{0, 0, 0, 0}};
        } else if (pieceType.equals(Type.O)) {
            deltaX = 42;
            deltaY = 916;
            matrix = new int[][]{
                    new int[]{1, 1, 0, 0},
                    new int[]{1, 1, 0, 0},
                    new int[]{0, 0, 0, 0},
                    new int[]{0, 0, 0, 0}};
        }

        if (new Random().nextBoolean()) mirror();
    }

    private void shift(int direction, int[][] matrix) {
        for (int i = 0; i < ROWS; i++) {
            if (matrix[i][0] == 1 && direction == -1) return;
            if (matrix[i][COLS - 1] == 1 && direction == 1) {
                return;
            }
        }
        for (int j = 0; j < COLS; j++) {
            if (matrix[0][j] == 1 && direction == -2) return;
            if (matrix[ROWS - 1][j] == 1 && direction == 2) return;
        }
        for (int i = 0; i < ROWS; i++) {

            for (int j = 0; j < COLS; j++) {

                if (direction == 1) {
                    int li = ROWS - i - 1;
                    int lj = COLS - j - 1;
                    if (lj - 1 >= 0) {
                        matrix[li][lj] = matrix[li][lj - 1];
                    } else matrix[li][lj] = 0;
                } else if (direction == -1) {
                    if (j + 1 < COLS) {
                        matrix[i][j] = matrix[i][j + 1];
                    } else matrix[i][j] = 0;
                } else if (direction == 2) {
                    int li = ROWS - i - 1;
                    int lj = COLS - j - 1;
                    if (li - 1 >= 0) {
                        matrix[li][lj] = matrix[li - 1][lj];
                    } else matrix[li][lj] = 0;
                } else if (direction == -2) {
                    if (i + 1 < ROWS) {
                        matrix[i][j] = matrix[i + 1][j];
                    } else matrix[i][j] = 0;
                }
            }
        }
    }

    private int[][] moveToCorner(int[][] matrix) {
        boolean shiftLeft = true;
        boolean shiftDown = true;
        while (shiftLeft || shiftDown) {
            for (int i = 0; i < ROWS; i++) {
                if (matrix[i][0] == 1) {
                    shiftLeft = false;
                    break;
                }
            }
            if (shiftLeft) shift(-1, matrix);
            for (int j = 0; j < COLS; j++) {
                if (matrix[0][j] == 1) {
                    shiftDown = false;
                    break;
                }
            }
            if (shiftDown) shift(-2, matrix);
        }
        return matrix;
    }

    private void mirror() {
        int[][] mirrored = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(matrix[ROWS - i - 1], 0, mirrored[i], 0, COLS);
        }
        matrix = moveToCorner(mirrored);
    }


    public void rotate() {
        int[][] rotated = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                rotated[COLS - 1 - j][i] = matrix[i][j];
            }
        }
        moveToCorner(rotated);
        while (!canPlace(0, rotated)) {
            for (int i = 1; i <= 4; i++) {
                if (canPlace(i, rotated)) {
                    shift(1, rotated);
                    break;
                } else if (canPlace(-i, rotated)) {
                    shift(-1, rotated);
                    break;
                }
            }
            changeX(-CELL_SIZE);
        }
        matrix = rotated;
    }

    private boolean canPlace(int col, int[][] matrix) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int js = currentColumn + col + j;
                if (matrix[i][j] == 1 && (js >= NUM_COLUMNS || js < 0)) return false;
            }
        }
        return true;
    }

    public boolean canPlace(int col) {
        return canPlace(col, matrix);
    }


    private int getX() {
        return x;
    }

    public void changeX(int inc) {
        x += inc;
        currentColumn = x / CELL_SIZE;
    }

    private int getY() {
        return y;
    }

    public void changeY(int inc) {
        y += inc;
        currentRow = y / CELL_SIZE;
    }


    public void moveRight() {
        if (canPlace(1)) {
            currentColumn++;
            x = currentColumn * CELL_SIZE;
        }
    }

    public void fall() {
        currentRow--;
        y = currentRow * CELL_SIZE;
    }

    int getCurrentRow() {
        return currentRow;
    }

    int getCurrentColumn() {
        return currentColumn;
    }

    private int[][] getMatrix() {
        return matrix;
    }

    public void set(Piece piece){
        currentRow = piece.getCurrentRow();
        currentColumn = piece.getCurrentColumn();
        deltaX = piece.deltaX;
        deltaY = piece.deltaY;
        pieceType = piece.pieceType;
        x = currentColumn * CELL_SIZE;
        y = currentRow * CELL_SIZE;
        matrix = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(piece.getMatrix()[i], 0, matrix[i], 0, COLS);
        }
    }

    private ArrayList<Cell> getCells(){
        ArrayList<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Cell cell = new Cell(0, 0);
            cells.add(cell);
        }
        return cells;
    }

    private void renderCell(Cell cell, ShapeRenderer renderer, Color color, boolean nextPiece) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);
        if (!nextPiece)
            renderer.rect( STAGE_START_X + cell.getX(), STAGE_START_Y + cell.getY(), cell.getWidth(), cell.getHeight());
        else {
            renderer.rect(NEXT_PIECE_BOXX + cell.getX() + deltaX, NEXT_PIECE_BOXY + cell.getY() - deltaY,
                    cell.getWidth(), cell.getHeight());
        }
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLACK);
        if (!nextPiece)
            renderer.rect( STAGE_START_X + cell.getX(), STAGE_START_Y + cell.getY(), cell.getWidth(), cell.getHeight());
        else {
            renderer.rect(NEXT_PIECE_BOXX + cell.getX() + deltaX , NEXT_PIECE_BOXY + cell.getY() - deltaY,
                    cell.getWidth(), cell.getHeight());
        }
        renderer.end();
    }

    public void renderPiece(Piece piece, ShapeRenderer renderer, Color color, boolean nextPiece) {
        ArrayList<Cell> cells = getCells();
        int x = piece.getX();
        int y = piece.getY();
        for (int i = 0; i < piece.getMatrix().length; i++) {
            for (int j = 0; j < piece.getMatrix().length; j++) {
                if (piece.getMatrix()[i][j] == 1) {
                    cells.get(i).setPosition(x, y);
                    renderCell(cells.get(i), renderer, color, nextPiece);
                }
                x += CELL_SIZE;
            }
            y += CELL_SIZE;
            x = piece.getX();
        }
    }

    public void fieldStateRender(GameField gameField, ShapeRenderer renderer, Color color) {
        for (int i = 0; i < gameField.getRows(); i++) {
            for (int j = 0; j < gameField.getCols(); j++) {
                if (gameField.getMatrix()[i][j] == 1) {
                    Cell cell = new Cell(i* CELL_SIZE, j* CELL_SIZE);
                    this.renderCell(cell, renderer, color, false);
                }
            }
        }
    }

}

