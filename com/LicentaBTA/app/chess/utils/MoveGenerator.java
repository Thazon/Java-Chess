package com.LicentaBTA.app.chess.utils;

import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.*;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.LicentaBTA.app.chess.utils.BoardUtils.*;

public class MoveGenerator {

    public static List<Move> generateAllMoves(ChessBoardModel board, PlayerColor color) {
        List<Move> allMoves = new ArrayList<>();

        List<Piece> playerPieces = (color == PlayerColor.WHITE)
                ? board.getWhitePieces()
                : board.getBlackPieces();

        for (Piece piece : playerPieces) {
            List<Point> legalCoords = generateLegalMoves(piece, board);

            for (Point dest : legalCoords) {
                int fromRow = piece.getRow();
                int fromCol = piece.getCol();
                int toRow = dest.x;
                int toCol = dest.y;

                Piece captured = board.getPiece(toRow, toCol);
                Move additionalMove = null;

                // Check for castling (king moving to column 6 or 1)
                if (piece instanceof King && piece.getMoveCount() == 0 && captured == null) {
                    if (toCol == 6 && checkCastlingRight(piece, board)) {
                        Piece rook = board.getPiece(fromRow, 7);
                        additionalMove = new Move(fromRow, 7, fromRow, 5, rook);
                    } else if (toCol == 1 && checkCastlingLeft(piece, board)) {
                        Piece rook = board.getPiece(fromRow, 0);
                        additionalMove = new Move(fromRow, 0, fromRow, 2, rook);
                    }
                }

                // Check for en passant
                if (piece instanceof Pawn && captured == null) {
                    Piece lastMoved = board.getLastMovedPiece();
                    if (lastMoved instanceof Pawn
                            && lastMoved.getColor() != piece.getColor()
                            && Math.abs(lastMoved.getRow() - piece.getRow()) == 0
                            && Math.abs(lastMoved.getCol() - piece.getCol()) == 1
                            && toRow - piece.getRow() == (piece.getColor() == PlayerColor.WHITE ? -1 : 1)
                            && toCol == lastMoved.getCol()) {

                        // En passant capture
                        captured = lastMoved;
                        additionalMove = new Move(lastMoved.getRow(), lastMoved.getCol(), -1, -1, (Move) null);
                    }
                }

                Move move = new Move(fromRow, fromCol, toRow, toCol, piece, captured, additionalMove);
                allMoves.add(move);
            }
        }

        return allMoves;
    }


    public static List<Point> generateLegalMoves(Piece piece, ChessBoardModel model) {
        List<Point> allMoves = generateAllMoves(piece, model);
        if (!model.isPlayerChecked()) {
            if (piece.getClass().getSimpleName().equals("King")) {
                //Adaugam rocada daca este valida
                if (piece.getMoveCount() == 0) {
                    if (checkCastlingLeft(piece, model)) allMoves.add(new Point(piece.getRow(), 1));
                    if (checkCastlingRight(piece, model)) allMoves.add(new Point(piece.getRow(), 6));
                }
            }
        }
        List<Point> legalMoves = new ArrayList<>();
        for (Point move : allMoves) {
            ChessBoardModel clone = model.cloneBoard();
            // Luam echivalentul piesei corespunzatoare de pe tabla clonata
            Piece clonedPiece = clone.getPiece(piece.getRow(), piece.getCol());
            //System.out.println("Cloned piece: Row: " + clonedPiece.getRow() + " Col: " + clonedPiece.getCol());
            // Linia de mai sus face parte din debug-ul tablelor clonate si va incetini considerabil aplicatia

            clone.movePiece(new Move(clonedPiece.getRow(), clonedPiece.getCol(), move.x, move.y, clonedPiece, clone.getPiece(move.x, move.y)));
            //DebugUtils.showBoardInWindow(clone, "Simulated Move: " + move.x + "," + move.y);
            //Inca o linie folosita pentru debug

            Piece king = (piece.getColor() == PlayerColor.WHITE) ? clone.getWhiteKing() : clone.getBlackKing();
            List<Piece> enemyPieces = (piece.getColor() == PlayerColor.WHITE) ? clone.getBlackPieces() : clone.getWhitePieces();

            List<Piece> threats = checkCheck(enemyPieces, king, clone);
            if (threats.isEmpty()) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }


    static List<Point> generateAllMoves(Piece piece, ChessBoardModel model) {
        List<Point> allMoves;
        switch (piece.getClass().getSimpleName()) {
            case "King" -> allMoves = getKingMoves(piece, model);
            case "Queen" -> allMoves = getQueenMoves(piece, model);
            case "Rook" -> allMoves = getRookMoves(piece, model);
            case "Bishop" -> allMoves = getBishopMoves(piece, model);
            case "Knight" -> allMoves = getKnightMoves(piece, model);
            default -> allMoves = getPawnMoves(piece, model);
        }
        return allMoves;
    }

    public static List<Point> generateAttackingMoves(Piece piece, ChessBoardModel model) {
        List<Point> allMoves;
        switch (piece.getClass().getSimpleName()) {
            case "King" -> allMoves = getKingMoves(piece, model);
            case "Queen" -> allMoves = getQueenMoves(piece, model);
            case "Rook" -> allMoves = getRookMoves(piece, model);
            case "Bishop" -> allMoves = getBishopMoves(piece, model);
            case "Knight" -> allMoves = getKnightMoves(piece, model);
            default -> allMoves = getPawnAttacks(piece, model);
        }
        return allMoves;
    }

    private static List<Point> getKingMoves(Piece selectedPiece, ChessBoardModel model) {
        List<Point> moves = new ArrayList<>();
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] dir : directions) {
            int newRow = selectedPiece.getRow() + dir[0];
            int newCol = selectedPiece.getCol() + dir[1];

            if (isInsideBoard(newRow, newCol)) {
                Piece p = model.getPiece(newRow, newCol);
                if (p == null || p.getColor() != selectedPiece.getColor()) {
                    moves.add(new Point(newRow, newCol));
                }
            }
        }

        return moves;
    }

    private static List<Point> getQueenMoves(Piece selectedPiece, ChessBoardModel model) {
        List<Point> moves = new ArrayList<>();
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] dir : directions) {
            int r = selectedPiece.getRow() + dir[0];
            int c = selectedPiece.getCol() + dir[1];
            while (isInsideBoard(r, c)) {
                Piece p = model.getPiece(r, c);
                if (p == null) {
                    moves.add(new Point(r, c));
                } else {
                    if (p.getColor() != selectedPiece.getColor()) {
                        moves.add(new Point(r, c));
                    }
                    break;
                }
                r += dir[0];
                c += dir[1];
            }
        }

        return moves;
    }

    private static List<Point> getRookMoves(Piece selectedPiece, ChessBoardModel model) {
        List<Point> moves = new ArrayList<>();
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        for (int[] dir : directions) {
            int r = selectedPiece.getRow() + dir[0];
            int c = selectedPiece.getCol() + dir[1];
            while (isInsideBoard(r, c)) {
                Piece p = model.getPiece(r, c);
                if (p == null) {
                    moves.add(new Point(r, c));
                } else {
                    if (p.getColor() != selectedPiece.getColor()) {
                        moves.add(new Point(r, c));
                    }
                    break;
                }
                r += dir[0];
                c += dir[1];
            }
        }

        return moves;
    }

    private static List<Point> getBishopMoves(Piece selectedPiece, ChessBoardModel model) {
        List<Point> moves = new ArrayList<>();
        int[][] directions = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] dir : directions) {
            int r = selectedPiece.getRow() + dir[0];
            int c = selectedPiece.getCol() + dir[1];
            while (isInsideBoard(r, c)) {
                Piece p = model.getPiece(r, c);
                if (p == null) {
                    moves.add(new Point(r, c));
                } else {
                    if (p.getColor() != selectedPiece.getColor()) {
                        moves.add(new Point(r, c));
                    }
                    break;
                }
                r += dir[0];
                c += dir[1];
            }
        }

        return moves;
    }

    private static List<Point> getKnightMoves(Piece selectedPiece, ChessBoardModel model) {
        List<Point> moves = new ArrayList<>();
        int[][] offsets = {
                {-2, -1}, {-2, 1},
                {-1, -2}, {-1, 2},
                {1, -2},  {1, 2},
                {2, -1},  {2, 1}
        };

        for (int[] offset : offsets) {
            int r = selectedPiece.getRow() + offset[0];
            int c = selectedPiece.getCol() + offset[1];

            if (isInsideBoard(r, c)) {
                Piece p = model.getPiece(r, c);
                if (p == null || p.getColor() != selectedPiece.getColor()) {
                    moves.add(new Point(r, c));
                }
            }
        }

        return moves;
    }

    private static List<Point> getPawnMoves(Piece selectedPiece, ChessBoardModel model) {
        List<Point> movement = new ArrayList<>();
        // One forward
        if (isEmpty(model, selectedPiece.getRow() + getDir(selectedPiece), selectedPiece.getCol())) {
            movement.add(new Point(selectedPiece.getRow() + getDir(selectedPiece), selectedPiece.getCol()));

            // Two forward from start
            if (selectedPiece.getMoveCount() == 0 && isEmpty(model, selectedPiece.getRow() + 2 * getDir(selectedPiece), selectedPiece.getCol())) {
                movement.add(new Point(selectedPiece.getRow() + 2 * getDir(selectedPiece), selectedPiece.getCol()));
            }
        }

        // Add attacking moves
        List<Point> moves = Stream.concat(movement.stream(), getPawnAttacks(selectedPiece, model).stream()).collect(Collectors.toList());

        // Add en Passant
        if (isInsideBoard(selectedPiece.getRow(), selectedPiece.getCol()-1)) {
            if(enPassantCheck(model.getPiece(selectedPiece.getRow(), selectedPiece.getCol()-1), model.getLastMovedPiece()))
                moves.add(new Point(selectedPiece.getRow() + getDir(selectedPiece), selectedPiece.getCol()-1));
            else if (isInsideBoard(selectedPiece.getRow(), selectedPiece.getCol()+1)) {
                if(enPassantCheck(model.getPiece(selectedPiece.getRow(), selectedPiece.getCol()+1), model.getLastMovedPiece()))
                    moves.add(new Point(selectedPiece.getRow() + getDir(selectedPiece), selectedPiece.getCol()+1));
            }
        }

        return moves;
    }

    private static List<Point> getPawnAttacks(Piece selectedPiece, ChessBoardModel model) {
        List<Point> attacks = new ArrayList<>();
        //Pawns only attack diagonally
        for (int attack : new int[]{-1, 1}) {
            int newRow = selectedPiece.getRow() + getDir(selectedPiece);
            int newCol = selectedPiece.getCol() + attack;

            if (isInsideBoard(newRow, newCol)) {
                Piece p = model.getPiece(newRow, newCol);
                if (p != null && p.getColor() != selectedPiece.getColor()) {
                    attacks.add(new Point(newRow, newCol));
                }
            }
        }
        return attacks;
    }

    private static boolean enPassantCheck(Piece checkedPiece, Piece lastMovedPiece) {
        if (checkedPiece instanceof Pawn && checkedPiece == lastMovedPiece && checkedPiece.getMoveCount() == 1) {
            if (checkedPiece.getColor() == PlayerColor.WHITE && checkedPiece.getRow() == 4){
                return true;
            }
            else return checkedPiece.getRow() == 3;
        }
        return false;
    }

    public static int getDir(Piece piece) {
        return (piece.getColor() == PlayerColor.WHITE) ? -1 : 1;
    }
}
