package com.mygdx.tetslv.model;

public enum Type {

    O(new int[][] {{-1, 0}, {0, 0}, {0, 1}, {-1, 1}}, new float[]{0.f, -1f}),
    T(new int[][] {{-1, 0},{0, 0}, {1, 0},{0, 1}}, new float[] {-0.5f, -1f}),
    L(new int[][] {{-1, 0}, {0, 0},{1, 0},{-1, 1}}, new float[] {-0.5f, -1f}),
    J(new int[][] {{-1, 0}, {0, 0}, {1, 0}, {1, 1}}, new float[] {-0.5f, -1f}),
    Z(new int[][] {{1, 0}, {0, 0}, {0, 1}, {-1, 1}}, new float[] {-0.5f, -1f}),
    S(new int[][] {{-1, 0}, {0, 0},{0, 1}, {1, 1}}, new float[] {-0.5f, -1f}),
    I(new int[][] {{-1, 0}, {0, 0}, {1, 0}, {2, 0}}, new float[] {-1f, -0.5f});

    public int[][] relativeIndexes;
    public final float[] delta;


    Type(int[][] relativeIndexes, float[] delta) {
        this.relativeIndexes = relativeIndexes;
        this.delta = delta;
    }
}
