package com.mygdx.tetslv.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.tetslv.view.GameScreen;
import java.util.Random;

import static com.mygdx.tetslv.model.GameField.INDEX_COLUMN;
import static com.mygdx.tetslv.model.GameField.INDEX_ROW;
import static com.mygdx.tetslv.model.GameField.NUM_COLUMNS;
import static com.mygdx.tetslv.model.GameField.NUM_ROWS;


public class TetPiece {

    private static final Type[] TYPE = Type.values();
    private static Random random = new Random();

    private int currentColumn;
    private int currentRow;
    private Type type;
    private static boolean solverStatus;

    private int[][] relativeIndexes;
    private final int[][] currentRelativeIndexes;
    private final float[] currentDelta;

    public static TetPiece newPiece(boolean solverStatus) {
        TetPiece.solverStatus = solverStatus;
        TetPiece tetpiece = new TetPiece(TYPE[random.nextInt(TYPE.length)]);
        tetpiece.initialPosition();
        return tetpiece;
    }



    private TetPiece(Type type) {
        this.type = type;
        this.currentRelativeIndexes = type.relativeIndexes;
        this.relativeIndexes = type.relativeIndexes;
        this.currentDelta = type.delta;

    }


    public void initialPosition() {
        currentColumn = NUM_COLUMNS / 2;
        if (this.type == Type.I) currentRow = NUM_ROWS - 1;
        else currentRow = NUM_ROWS - 2;
    }

    private int[][] getCells(int[][] relativeIndexes) {
        return new int[][] {
                new int[] {currentColumn + relativeIndexes[0][INDEX_COLUMN], currentRow + relativeIndexes[0][INDEX_ROW]},
                new int[] {currentColumn + relativeIndexes[1][INDEX_COLUMN], currentRow + relativeIndexes[1][INDEX_ROW]},
                new int[] {currentColumn + relativeIndexes[2][INDEX_COLUMN], currentRow + relativeIndexes[2][INDEX_ROW]},
                new int[] {currentColumn + relativeIndexes[3][INDEX_COLUMN], currentRow + relativeIndexes[3][INDEX_ROW]}
        };
    }

    public int[][] getCells() {
        return getCells(relativeIndexes);
    }

    public void fall() {
        currentRow--;
    }

    public void rotate(GameField gameField) {
        if (this.type == Type.O) {
            return;
        }
        int[][] rotated = new int[][] {
                new int[] {-relativeIndexes[0][INDEX_ROW], relativeIndexes[0][INDEX_COLUMN]},
                new int[] {-relativeIndexes[1][INDEX_ROW], relativeIndexes[1][INDEX_COLUMN]},
                new int[] {-relativeIndexes[2][INDEX_ROW], relativeIndexes[2][INDEX_COLUMN]},
                new int[] {-relativeIndexes[3][INDEX_ROW], relativeIndexes[3][INDEX_COLUMN]}};
        int[][] newPositions = getCells(rotated);
        if (gameField.canPlace(newPositions)) {
            relativeIndexes = rotated;
        }
    }

    public void moveLeft(GameField gameField) {
        int[][] cells = getCells();
        for (int[] cell: cells) {
            cell[INDEX_COLUMN]--;
        }
        if (gameField.canPlace(cells)) {
            currentColumn--;
        }
    }

    public void moveRight(GameField gameField) {
        int[][] cells = getCells();
        for (int[] cell: cells) {
            cell[INDEX_COLUMN]++;
        }
        if (gameField.canPlace(cells)) {
           currentColumn++;
        }
    }

    public void render(ShapeRenderer renderer) {
        if (renderer.getCurrentType() == ShapeRenderer.ShapeType.Line) {
            for (int[] cell: getCells()) {
                GameScreen.renderPiece(renderer, cell[INDEX_COLUMN], cell[INDEX_ROW]);
            }
        }
        else if (solverStatus) renderer.setColor(Color.SCARLET);
        else renderer.setColor(Color.ROYAL);

        for (int[] cell: getCells()) {
            GameScreen.renderPiece(renderer, cell[INDEX_COLUMN], cell[INDEX_ROW]);
        }
    }

    public void render(ShapeRenderer renderer, int startX, int startY, int boxSize) {
        if (renderer.getCurrentType() == ShapeRenderer.ShapeType.Line) {
            for (int[] cell : getCells()) {
                GameScreen.renderPiece(renderer, cell[INDEX_COLUMN], cell[INDEX_ROW]);
            }
        }
        else if (solverStatus) renderer.setColor(Color.SCARLET);
        else renderer.setColor(Color.ROYAL);
        int originX = startX + boxSize * 2;
        int originY = startY + boxSize * 2;
        for (int[] cell: currentRelativeIndexes) {
            renderer.rect(originX + (cell[INDEX_COLUMN] + currentDelta[INDEX_COLUMN]) * boxSize,
                    originY + (cell[INDEX_ROW] + currentDelta[INDEX_ROW]) * boxSize,
                    boxSize,
                    boxSize);
        }
    }

}
