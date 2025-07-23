package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.PlayerColor;

public class Bishop extends Piece {
    public Bishop(int row, int col, PlayerColor color, int moveCount) {
        super(row, col, color, moveCount);
    }

    public Bishop(int row, int col, PlayerColor color) {
        super(row, col, color);
    }

    @Override
    public int getValue() {
        return 330;
    }

    @Override
    public String getSymbol() {
        return color == PlayerColor.WHITE ? "♗" : "♝";
    }
}
