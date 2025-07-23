package com.LicentaBTA.app.chess.models;

public class Move {
    private final int fromRow, fromCol;
    private final int toRow, toCol;
    private final Piece movedPiece;
    private final Piece capturedPiece;
    private final Move additionalMove; // pentru en passant sau rocada

    public Move(int fromRow, int fromCol, int toRow, int toCol,
                Piece movedPiece, Piece capturedPiece,
                Move additionalMove) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.additionalMove = additionalMove;
    }

    public Move(int fromRow, int fromCol, int toRow, int toCol, Piece movedPiece, Piece capturedPiece) {
        this(fromRow, fromCol, toRow, toCol, movedPiece, capturedPiece, null);
    }
    public Move(int fromRow, int fromCol, int toRow, int toCol, Move additionalMove) {
        this(fromRow, fromCol, toRow, toCol, null, null, additionalMove);
    }

    public Move(int fromRow, int fromCol, int toRow, int toCol, Piece movedPiece) {
        this(fromRow, fromCol, toRow, toCol, movedPiece, null, null);
    }

    public boolean hasAdditionalMove() {
        return additionalMove != null;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }

    public Move getAdditionalMove() {
        return additionalMove;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    private static final String[] COL_NAMES = {"A", "B", "C", "D", "E", "F", "G", "H"};

    private String squareName(int row, int col) {
        return COL_NAMES[col] + (8 - row);
    }

    @Override
    public String toString() {
        // Rocada
        if (movedPiece instanceof King && Math.abs(fromCol - toCol) >= 2) {
            return (toCol == 6) ? "O-O" : "O-O-O";
        }

        StringBuilder sb = new StringBuilder();

        boolean isCapture = capturedPiece != null || (hasAdditionalMove() && additionalMove.getToRow() == -1);
        boolean isPromotion = movedPiece instanceof Pawn && (toRow == 0 || toRow == 7);
        boolean isEnPassant = hasAdditionalMove() && additionalMove.getToRow() == -1;

        // Pentru pionii capturati afisam coloana initiala
        if (movedPiece instanceof Pawn && isCapture) {
            sb.append(COL_NAMES[fromCol]);
        } else {
            sb.append(pieceSymbol(movedPiece));
        }

        if (isCapture) sb.append("x");

        sb.append(squareName(toRow, toCol));

        if (isEnPassant) sb.append(" e.p.");

        if (isPromotion) sb.append("=Q"); // default promovare la regina

        return sb.toString();
    }

    private String pieceSymbol(Piece piece) {
        if (piece == null) return "";
        return switch (piece.getClass().getSimpleName()) {
            case "Pawn" -> "";
            case "Knight" -> "N";
            case "Bishop" -> "B";
            case "Rook" -> "R";
            case "Queen" -> "Q";
            case "King" -> "K";
            default -> "?";
        };
    }

}
