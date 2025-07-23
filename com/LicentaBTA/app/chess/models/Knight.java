package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.PlayerColor;

public class Knight extends Piece {
    public Knight(int row, int col, PlayerColor color, int moveCount) {
        super(row, col, color, moveCount);
    }

    public Knight(int row, int col, PlayerColor color) {
        super(row, col, color);
    }

    @Override
    public int getValue() {
        return 320;
    }

    @Override
    public String getSymbol() {
        return color == PlayerColor.WHITE ? "♘" : "♞";
    }
}
