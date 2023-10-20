package com.huoxin4415.bwai;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }


    public Boolean equals(Point p) {
        if (p.getX() == x && p.getY() == y) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public String toString() {
        return "X:" + x + ";Y:" + y;
    }
}