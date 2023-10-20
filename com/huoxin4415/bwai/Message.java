package com.huoxin4415.bwai;

import java.awt.*;

public class Message {

    private Integer depth;
    private ChessBoard cb   ;
    private Double alpha;
    private Double beta;
    private Boolean gameTurn;

    public Message(Integer depth, ChessBoard cb, Double alpha, Double beta, Boolean gameTurn) {
        this.depth = depth;
        this.cb = cb;
        this.alpha = alpha;
        this.beta = beta;
        this.gameTurn = gameTurn;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public ChessBoard getCb() {
        return cb;
    }

    public void setCb(ChessBoard cb) {
        this.cb = cb;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public Double getBeta() {
        return beta;
    }

    public void setBeta(Double beta) {
        this.beta = beta;
    }

    public Boolean getGameTurn() {
        return gameTurn;
    }

    public void setGameTurn(Boolean gameTurn) {
        this.gameTurn = gameTurn;
    }
}
