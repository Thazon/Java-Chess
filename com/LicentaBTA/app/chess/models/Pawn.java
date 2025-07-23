package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.PlayerColor;

public class Pawn extends Piece {
    public Pawn(int row, int col, PlayerColor color) {
        super(row, col, color);
    }

    public Pawn(int row, int col, PlayerColor color, int moveCount) {
        super(row, col, color, moveCount);
    }

    @Override
    public int getValue() {
        return 100;
    }

    @Override
    public String getSymbol() {
        return color == PlayerColor.WHITE ? "♙" : "♟";
    }
}
