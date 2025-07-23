package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.PlayerColor;

public class Rook extends Piece {
    public Rook(int row, int col, PlayerColor color, int moveCount) {
        super(row, col, color, moveCount);
    }

    public Rook(int row, int col, PlayerColor color) {
        super(row, col, color);
    }

    @Override
    public int getValue() {
        return 500;
    }

    @Override
    public String getSymbol() {
        return color == PlayerColor.WHITE ? "♖" : "♜";
    }
}
