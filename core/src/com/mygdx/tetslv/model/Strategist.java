package com.mygdx.tetslv.model;

import java.util.ArrayList;


public class Strategist {
    private ArrayList<Integer> scoreList;
    private Piece piece;
    private int[][] oldMatrix;
    private int[][] newMatrix;
    private int rows;
    private int columns;


    public Strategist(GameField oldGameField, GameField newGameField, Piece piece) {
        this.piece = new Piece(piece);
        oldMatrix = oldGameField.getMatrix();
        newMatrix = newGameField.getMatrix();
        scoreList = new ArrayList<>();
        rows = oldMatrix.length;
        columns = oldMatrix[0].length;
        setHolesScore();
        setFullLineScore();
        setHeightScore();
    }

    public Strategist(ArrayList<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    public Piece getPiece() {
        return piece;
    }

    public ArrayList<Integer> getScoreList() {
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
        for (int i = 0; i < rows; i++) {
            int cells = 0;
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] == 1) cells++;
            }
            if (cells >= columns) lines++;
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
        for (int i = rows - 1; i >= 0; i--) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] == 1) {
                    return rows - h;
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
        for (int i = 0; i < rows; i++) {
            boolean holeExist = false;
            for (int j = columns - 2; j >= 0; j--) {
                if (i + 1 < columns) {
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
