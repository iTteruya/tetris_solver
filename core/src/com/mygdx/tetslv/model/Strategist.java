package com.mygdx.tetslv.model;

import java.util.ArrayList;
import static com.mygdx.tetslv.model.GameField.NUM_COLUMNS;
import static com.mygdx.tetslv.model.GameField.NUM_ROWS;

public class Strategist {
    private ArrayList<Integer> scoreList;
    private Piece piece;
    private int[][] oldMatrix;
    private int[][] newMatrix;


    public Strategist(GameField oldGameField, GameField newGameField, Piece piece) {
        this.piece = new Piece(piece);
        oldMatrix = oldGameField.getMatrix();
        newMatrix = newGameField.getMatrix();
        scoreList = new ArrayList<>();
        setHolesScore();
        setFullLineScore();
        setHeightScore();

    }


    public Piece getPiece() {
        return piece;
    }

    private ArrayList<Integer> getScoreList() {
        return scoreList;
    }


    public Strategist getBetter(Strategist newScores) {
        ArrayList<Integer> newScoreList = newScores.getScoreList();
        int diff1 = newScoreList.get(0) - scoreList.get(0);
        int diff2 = newScoreList.get(1) - scoreList.get(1);
        int diff3 = newScoreList.get(2) - scoreList.get(2);

        if(diff1 > 0){
            return newScores;
        } else if (diff1 == 0) {
            if(diff2 > 0) {
                return newScores;
            } else if (diff2 == 0 ) {
                if (diff3 > 0) {
                    return newScores;
                }
            }
        }
        return this;
    }

    private void setFullLineScore() {
        int oldLines = countLines(oldMatrix);
        int newLines = countLines(newMatrix);
        int diff = newLines - oldLines;
        scoreList.add(diff);
    }

    private int countLines(int[][] matrix) {
        int lines = 0;
        for (int i = 0; i < NUM_ROWS; i++) {
            int cells = 0;
            for (int j = 0; j < NUM_COLUMNS; j++) {
                if (matrix[i][j] == 1) cells++;
            }
            if (cells >= NUM_COLUMNS) lines++;
        }
        return lines;
    }

    private void setHeightScore() {
        int oldHeight = getHeight(oldMatrix);
        int newHeight = getHeight(newMatrix);
        int diff = oldHeight - newHeight;
        scoreList.add(diff);
    }

    private int getHeight(int[][] matrix) {
        int h = 0;
        for (int i = NUM_ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < NUM_COLUMNS; j++) {
                if (matrix[i][j] == 1) {
                    return NUM_ROWS - h;
                }
            }
            h++;
        }
        return 0;
    }


    private void setHolesScore() {
        int oldHolesCount = countHoles(oldMatrix);
        int newHolesCount = countHoles(newMatrix);
        int diff = oldHolesCount - newHolesCount;
        scoreList.add(diff);
    }

    private int countHoles(int[][] matrix) {
        int holesCount = 0;
        for (int i = 0; i < NUM_ROWS; i++) {
            boolean holeExist = false;
            for (int j = NUM_COLUMNS - 2; j >= 0; j--) {
                if (i + 1 < NUM_COLUMNS) {
                    if ((matrix[i + 1][j] == 1|| holeExist) && matrix[i][j] == 0) {
                        holeExist = true;
                        holesCount++;
                    }
                }
            }
        }
        return holesCount;
    }
}
