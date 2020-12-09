package com.mygdx.tetslv.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Arrays;

public class GameField extends Actor {

    public static final int CELL_SIZE = 32;
    public static final int NUM_COLUMNS = 20;
    public static final int NUM_ROWS = 30;

    private int[][] matrix;
    private int rows;
    private int columns;
    private int score;

    public GameField() {
        rows = NUM_ROWS;
        columns = NUM_COLUMNS;
        matrix = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(matrix[i], 0);
        }
    }

    public GameField(GameField gameField) {
        rows = NUM_ROWS;
        columns = NUM_COLUMNS;
        score = gameField.getScore();
        matrix = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            if (columns >= 0) System.arraycopy(gameField.matrix[i], 0, matrix[i], 0, columns);
        }
    }

    public GameField(int[][] matrix){
        rows = matrix.length;
        columns = matrix[0].length;
        this.matrix = Arrays.copyOf(matrix, rows);
    }

    public void reset(){
        rows = NUM_ROWS;
        columns = NUM_COLUMNS;
        score = 0;
        matrix = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(matrix[i], 0);
        }
    }

    private boolean addFigure(Piece piece) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int fi = piece.getCurrentRow() + i;
                int fj = piece.getCurrentColumn() + j;
                int l = rows;
                int value = piece.matrix[i][j];
                if (value == 1) {
                    if (fi < l && fi >= 0 && fj < l && fj >= 0) {
                        matrix[fi][fj] = 1;
                    }
                }
            }
        }
        return true;
    }

    public boolean update(Piece piece) {
        int[][] pieceMatrix = piece.matrix;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int row = piece.getCurrentRow();
                int column = piece.getCurrentColumn();
                if (pieceMatrix[i][j] == 1) {
                    if (row + i == 0) {
                        return addFigure(piece);
                    } else if ((row + i - 1 < rows && column + j < columns)) {
                        if ((matrix[row + i - 1][column + j]) == 1) {
                            return addFigure(piece);
                        }
                    }
                }
            }
        }
        return false;
    }

    public void clearLine(boolean solverON) {
        boolean lineIsFilled = false;
        for (int i = 0; i < rows; i++) {
            int counter = 0;
            for (int j = 0; j < columns; j++) {
                if (lineIsFilled && i + 1 < rows) {
                    matrix[i - 1][j] = matrix[i][j];
                } else if (matrix[i][j] == 1) {
                    counter++;
                } else if (counter != 0) {
                    break;
                }
            }
            if (counter >= columns && !lineIsFilled) {
                lineIsFilled = true;
                if (solverON) score--;
                else score++;
                for (int j = 0; j < columns; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public boolean gameIsNotOver() {
        for (int i = 0; i < columns; i++) {
            if(matrix[rows-1][i] == 1) return false;
        }
        return true;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    int getRows() {
        return rows;
    }

    int getCols() {
        return columns;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.translate(getX(), getY(), 0);


        // Background
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(1, 1, CELL_SIZE * NUM_COLUMNS, CELL_SIZE * NUM_ROWS);
        shapeRenderer.end();

        int width = CELL_SIZE * NUM_COLUMNS;
        int height = CELL_SIZE * NUM_ROWS;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        for (int i = 0; i * 32 < height; i++ ) {
            for (int j = 0; j * 32 < width; j++) {
                shapeRenderer.rect(1, 1, CELL_SIZE + CELL_SIZE * j,  CELL_SIZE + CELL_SIZE * i);
            }
        }

        shapeRenderer.end();
        batch.begin();

    }

}

