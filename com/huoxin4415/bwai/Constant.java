package com.huoxin4415.bwai;

public enum Constant {
    BLACK(1), //黑棋
    WHITE(-1), //白棋
    NULL(0); //空

    private int value;

    public int getValue() {
        return value;
    }

    Constant(int value) {
        this.value = value;
    }
}
