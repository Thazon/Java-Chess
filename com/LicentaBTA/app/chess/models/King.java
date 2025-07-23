package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.PlayerColor;

public class King extends Piece {

    public King(int row, int col, PlayerColor color, int moveCount) {
        super(row, col, color, moveCount);
    }

    public King(int row, int col, PlayerColor color) {
        super(row, col, color);
    }

    @Override
    public int getValue() {
        return 20000;
    }

    @Override
    public String getSymbol() {
        return color == PlayerColor.WHITE ? "♔" : "♚";
    }
}
