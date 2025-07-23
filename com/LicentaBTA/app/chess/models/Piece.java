package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.PlayerColor;

public abstract class Piece {
    protected int row, col, value, moveCount;
    protected PlayerColor color;

    public Piece(int row, int col, PlayerColor color, int moveCount) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.moveCount = moveCount;
    }

    public Piece(int row, int col, PlayerColor color) {
        this(row, col, color, 0);
    }

    public PlayerColor getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void incrementMoveCount() {
        this.moveCount++;
    }

    public void decrementMoveCount() {
        if (this.moveCount > 0) this.moveCount--;
    }

    public int getMoveCount() {
        return this.moveCount;
    }

    public abstract int getValue();

    public abstract String getSymbol();

}
