package com.LicentaBTA.app.chess.utils;

import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Piece;
import com.LicentaBTA.app.chess.models.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.LicentaBTA.app.chess.utils.MoveGenerator.*;
import static com.LicentaBTA.app.chess.utils.ScoreUtils.*;

public class BoardUtils {
    public static boolean isEmpty(ChessBoardModel model, int row, int col) {
        return isInsideBoard(row, col) && model.getPiece(row, col) == null;
    }

    public static boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public static boolean checkCastlingLeft(Piece king, ChessBoardModel model) {
        Piece leftRook = model.getPiece(king.getRow(), 0);
        if (leftRook == null || leftRook.getMoveCount() != 0) return false;
        for (int col = king.getCol() -1; col > 0; col--) {
            if (model.getPiece(king.getRow(), col) != null || isSquareUnderAttack(king.getRow(), col, king.getColor(), model)) return false;
        }
        return true;
    }

    public static boolean checkCastlingRight(Piece king, ChessBoardModel model) {
        Piece rightRook = model.getPiece(king.getRow(), 7);
        if (rightRook == null || rightRook.getMoveCount() != 0) return false;
        for (int col = king.getCol() + 1; col < 7; col++) {
            if (model.getPiece(king.getRow(), col) != null || isSquareUnderAttack(king.getRow(), col, king.getColor(), model)) return false;
        }
        return true;
    }

    private static boolean isSquareUnderAttack(int row, int col, PlayerColor byColor, ChessBoardModel model) {
        for (Piece piece : (byColor == PlayerColor.WHITE ? model.getBlackPieces() : model.getWhitePieces())) {
            List<Point> moves = generateAllMoves(piece, model);
            if (moves.contains(new Point(row, col))) {
                return true;
            }
        }
        return false;
    }

    public static List<Piece> checkCheck(List<Piece> enemyPieces, Piece king, ChessBoardModel model) {
        Point kingPosition = new Point(king.getRow(), king.getCol());
        List<Piece> checkingPieces = new ArrayList<>();

        for (Piece piece : enemyPieces) {
            List<Point> attackSquares;

            // Folosim numai atacurile pieselor pentru verificarea sahului
            attackSquares = generateAttackingMoves(piece, model);

            if (attackSquares.contains(kingPosition)) {
                checkingPieces.add(piece);
            }
        }
        return checkingPieces;
    }

    public static boolean checkmate(PlayerColor playerColor, ChessBoardModel model) {
        List<Piece> playerPieces = new ArrayList<>(
                (playerColor == PlayerColor.WHITE) ? model.getWhitePieces() : model.getBlackPieces()
        );
        Piece king = (playerColor == PlayerColor.WHITE) ? model.getWhiteKing() : model.getBlackKing();
        playerPieces.add(king);
        for (Piece piece : playerPieces) {
            List<Point> legalMoves = generateLegalMoves(piece, model);
            if (!legalMoves.isEmpty()) return false;
        }
        return true;
    }

    public static int evaluateBoard(ChessBoardModel board, PlayerColor color) {
        int score = 0;

        // Punctaj bazat pe scorul pieselor
        score += getMaterialScore(board, color);

        // Punctaj pentru mobilitatea curenta
        score += getMobilityScore(board, color);

        // Punctaj pentru pozitia pieselor
        score += getPieceSquareScore(board, color);

        // Punctaj pentru siguranta regelui
        score += getKingSafetyScore(board, color);

        // Punctaj pentru structura pionilor
        score += getPawnStructureScore(board, color);

        return score;
    }

    public static boolean isStalemate(ChessBoardModel model) {
        int moveIndex = model.getMoveHistory().getCurrentIndex();
        Move m1 = model.getMoveHistory().getMoveAt(moveIndex - 1);
        if (m1 == null) return false;
        Move m2 = model.getMoveHistory().getMoveAt(moveIndex - 5);
        if (m2 == null || !movesEqual(m1, m2)) return false;
        Move m3 = model.getMoveHistory().getMoveAt(moveIndex - 3);
        if (m3 == null) return false;
        Move m4 = model.getMoveHistory().getMoveAt(moveIndex - 7);
        if (m4 == null) return false;
        return movesEqual(m3, m4);
    }

    private static boolean movesEqual(Move m1, Move m2) {
        return m1.getFromRow() == m2.getFromRow() &&
                m1.getFromCol() == m2.getFromCol() &&
                m1.getToRow() == m2.getToRow() &&
                m1.getToCol() == m2.getToCol() &&
                m1.getMovedPiece() != null &&
                m2.getMovedPiece() != null &&
                m1.getMovedPiece().getClass().equals(m2.getMovedPiece().getClass());
    }

}
