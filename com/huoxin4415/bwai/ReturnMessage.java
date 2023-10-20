package com.huoxin4415.bwai;

public class ReturnMessage {

    //目前找到的最佳下棋位置
    private Point point;
    //分数
    private Double score;


    public ReturnMessage(Point point, Double score) {
        this.point = point;
        this.score = score;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return point + ";score:" + score + "\n";
    }
}
