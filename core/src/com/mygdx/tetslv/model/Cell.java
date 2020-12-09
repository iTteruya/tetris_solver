package com.mygdx.tetslv.model;

import static com.mygdx.tetslv.model.GameField.CELL_SIZE;

class Cell {
    private int x;
    private int y;
    private int width;
    private int height;

    Cell(int y, int x) {
        this.x = x;
        this.y = y;
        width = CELL_SIZE;
        height = CELL_SIZE;
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
            return x;
        }

    int getY() {
        return y;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

}
