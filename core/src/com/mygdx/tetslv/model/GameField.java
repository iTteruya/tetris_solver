package com.mygdx.tetslv.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.HashSet;
import java.util.Set;


public class GameField extends Actor {

    public static final int CELL_SIZE = 32;

    static final int INDEX_COLUMN = 0;
    static final int INDEX_ROW = 1;

    public static final int NUM_COLUMNS = 10;
    public static final int NUM_ROWS = 20;
    private boolean solverStatus;

    private boolean[][] isFilled = new boolean[NUM_COLUMNS][NUM_ROWS];
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public void reset() {
        for (int i = 0; i < NUM_COLUMNS; i++) {
            for (int j = 0; j < NUM_ROWS; j++) {
                isFilled[i][j] = false;
            }
        }
    }

    private boolean isLineFilled(int l) {
        for (int c = 0;  c < NUM_COLUMNS; c++) {
            if (!isFilled[c][l]) {
                return false;
            }
        }
        return true;
    }

    public int placePiece(int[][] cells) {
        for (int[] cell: cells) {
            isFilled[cell[INDEX_COLUMN]][cell[INDEX_ROW]] = true;
        }

        Set<Integer> linesToRemove = new HashSet<>();
        for (int r = 0; r < NUM_ROWS; r++) {
            if (isLineFilled(r)) {
                linesToRemove.add(r);
            }
        }

        if (linesToRemove.isEmpty()) {
            return 0;
        }

        int delta = 0;
        int newLine = -1;
        for (int r = 0; r < NUM_ROWS; r++) {
            if (isLineFilled(r) && r > newLine) {
                delta++;
                newLine = r + 1;
                if (r < NUM_ROWS - 2) {
                    while (isLineFilled(newLine) && newLine != NUM_ROWS) {
                        delta++;
                        newLine++;
                    }
                }
            }
            if (r < NUM_ROWS - 2) {
                if (!isLineFilled(r) && isLineFilled(r + 1)) {
                    delta = 0;
                    newLine = r;
                }
            }

            if (r + delta < NUM_ROWS) {
                for (int c = 0; c < NUM_COLUMNS; c++) {
                    isFilled[c][r] = isFilled[c][r + delta];
                }
            } else {
                for (int c = 0; c < NUM_COLUMNS; c++) {
                    isFilled[c][r] = false;
                }
            }
        }
        return linesToRemove.size();
    }

    public boolean isOnGround(int[][] cells) {
        for (int[] cell: cells) {
            if (cell[INDEX_ROW] - 1 >= NUM_ROWS) {
                continue;
            }
            if (cell[INDEX_ROW] <= 0 || isFilled[cell[INDEX_COLUMN]][cell[INDEX_ROW] - 1]) {
                return true;
            }
        }
        return false;
    }

    boolean canPlace(int[][] cells) {
        for (int[] cell: cells) {
            int row = cell[INDEX_ROW];
            int column = cell[INDEX_COLUMN];
            if (row < 0 || column < 0 || row >= NUM_ROWS || column >= NUM_COLUMNS || isFilled[column][row]) {
                return false;
            }
        }
        return true;
    }

    public GameField(boolean solverStatus) {
        this.solverStatus = solverStatus;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
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
                shapeRenderer.rect(1, 1, 32 + 32 * j, 32 + 32 * i);
            }
        }
        shapeRenderer.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (solverStatus) shapeRenderer.setColor(Color.SCARLET);
        else shapeRenderer.setColor(Color.ROYAL);
        for (int i = 0; i < NUM_COLUMNS; i++) {
            for (int j = 0; j < NUM_ROWS; j++) {
                if (isFilled[i][j]) {
                    shapeRenderer.rect(i * CELL_SIZE + 1, j * CELL_SIZE + 1, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        for (int i = 0; i < NUM_COLUMNS; i++) {
            for (int j = 0; j < NUM_ROWS; j++) {
                if (isFilled[i][j]) {
                    shapeRenderer.rect(i * CELL_SIZE + 1, j * CELL_SIZE + 1, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        shapeRenderer.end();

        batch.begin();
    }
}
