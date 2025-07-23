package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.PlayerColor;

public class PieceFactory {
    public static Piece copyPiece(Piece piece) {
        int row = piece.getRow();
        int col = piece.getCol();
        PlayerColor color = piece.getColor();
        int moveCount = piece.getMoveCount();

        return switch (piece) {
            case Pawn pawn -> new Pawn(row, col, color, moveCount);
            case Rook rook -> new Rook(row, col, color, moveCount);
            case Knight knight -> new Knight(row, col, color, moveCount);
            case Bishop bishop -> new Bishop(row, col, color, moveCount);
            case Queen queen -> new Queen(row, col, color, moveCount);
            case King king -> new King(row, col, color, moveCount);
            default -> null;
        };

    }
}
