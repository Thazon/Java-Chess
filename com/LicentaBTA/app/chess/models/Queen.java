package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.PlayerColor;

public class Queen extends Piece {
    public Queen(int row, int col, PlayerColor color, int moveCount) {
        super(row, col, color, moveCount);
    }

    public Queen(int row, int col, PlayerColor color) {
        super(row, col, color);
    }

    @Override
    public int getValue() {
        return 900;
    }

    @Override
    public String getSymbol() {
        return color == PlayerColor.WHITE ? "♕" : "♛";
    }
}
