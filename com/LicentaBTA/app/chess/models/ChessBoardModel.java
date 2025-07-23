package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.utils.MoveHistory;

import java.util.ArrayList;
import java.util.List;

import static com.LicentaBTA.app.chess.models.PieceFactory.copyPiece;
import static com.LicentaBTA.app.chess.utils.BoardUtils.checkCheck;
import static com.LicentaBTA.app.chess.utils.BoardUtils.isInsideBoard;

public class ChessBoardModel {
    private Piece[][] board = new Piece[8][8];
    private PlayerModel whitePlayer;
    private PlayerModel blackPlayer;
    private PlayerColor currentPlayer = PlayerColor.WHITE;
    private int whiteMaterial = 0;
    private int blackMaterial = 0;
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;
    private Piece whiteKing;
    private Piece blackKing;
    private boolean playerChecked;
    private MoveHistory moveHistory;
    private List<Piece> checkingPieces;
    private Piece lastMovedPiece;
    private int currentTurn;

    public ChessBoardModel(boolean isClone, PlayerModel whitePlayer, PlayerModel blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        moveHistory = new MoveHistory();
        if (!isClone) resetBoard();
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        removePieceFromList(row, col);
        board[row][col] = piece;
        addPieceToList(piece);
    }

    public void deletePiece(int row, int col) {
        removePieceFromList(row, col);
        board[row][col] = null;
    }

    public void removePieceFromList(int row, int col) {
        Piece piece = getPiece(row, col);
        if (piece == null) return;

        List<Piece> list = (piece.getColor() == PlayerColor.WHITE) ? whitePieces : blackPieces;
        list.removeIf(p ->
                p.getRow() == piece.getRow() &&
                        p.getCol() == piece.getCol() &&
                        p.getClass().equals(piece.getClass())
        );
    }


    public void addPieceToList(Piece piece) {
        if (piece != null) {
            if (piece.getColor() == PlayerColor.WHITE) whitePieces.add(piece);
            else blackPieces.add(piece);
        }
    }

    public void resetBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board[r][c] = null;
            }
        }
        if (checkingPieces != null) checkingPieces.clear();
        else checkingPieces = new ArrayList<>();
        if (whitePieces != null) whitePieces.clear();
        else whitePieces = new ArrayList<>();
        if (blackPieces != null) blackPieces.clear();
        else blackPieces = new ArrayList<>();
        setupPieces(PlayerColor.WHITE, 7, 6);
        setupPieces(PlayerColor.BLACK, 0, 1);
        playerChecked = false;
        lastMovedPiece = null;
        currentPlayer = PlayerColor.WHITE;
        currentTurn = 0;
    }

    private void initializePieceLists(PlayerColor color, Piece addedPiece) {
        if (color == PlayerColor.WHITE) {
            whitePieces.add(addedPiece);
        }
        else blackPieces.add(addedPiece);
    }

    private void setupPieces(PlayerColor color, int mainRow, int pawnRow) {
        board[mainRow][0] = new Rook(mainRow, 0, color);
        board[mainRow][1] = new Knight(mainRow, 1, color);
        board[mainRow][2] = new Bishop(mainRow, 2, color);
        board[mainRow][3] = new Queen(mainRow, 3, color);
        board[mainRow][4] = new King(mainRow, 4, color);
        board[mainRow][5] = new Bishop(mainRow, 5, color);
        board[mainRow][6] = new Knight(mainRow, 6, color);
        board[mainRow][7] = new Rook(mainRow, 7, color);

        for (int c = 0; c < 8; c++) {
            board[pawnRow][c] = new Pawn(pawnRow, c, color);
            initializePieceLists(color, board[pawnRow][c]);
            if (board[mainRow][c] instanceof King) {
                if (color == PlayerColor.WHITE) whiteKing = board[mainRow][c];
                else blackKing = board[mainRow][c];
            }
            initializePieceLists(color, board[mainRow][c]);
        }
    }

    public void movePiece(Move move) {
        Piece movingPiece = getPiece(move.getFromRow(), move.getFromCol());
        Piece capturedPiece = getPiece(move.getToRow(), move.getToCol());
        removePieceFromList(move.getToRow(), move.getToCol());

        if (movingPiece != null) {
            board[move.getToRow()][move.getToCol()] = movingPiece;
            board[move.getFromRow()][move.getFromCol()] = null;
            movingPiece.setPosition(move.getToRow(), move.getToCol());
            if (move.hasAdditionalMove()) {
                Move additional = move.getAdditionalMove();

                // En passant
                if (additional.getToRow() == -1) {
                    Piece capturedPawn = getPiece(additional.getFromRow(), additional.getFromCol());
                    if (capturedPawn != null) {
                        capturePiece(capturedPawn);
                        deletePiece(capturedPawn.getRow(), capturedPawn.getCol());
                    }
                }
                // Rocada sau alte mutari speciale
                else if (isInsideBoard(additional.getFromRow(), additional.getFromCol()) &&
                        isInsideBoard(additional.getToRow(), additional.getToCol())) {
                    Piece rook = getPiece(additional.getFromRow(), additional.getFromCol());
                    if (rook != null) {
                        board[additional.getToRow()][additional.getToCol()] = rook;
                        board[additional.getFromRow()][additional.getFromCol()] = null;
                        rook.setPosition(additional.getToRow(), additional.getToCol());
                        rook.incrementMoveCount();
                    }
                }
            }

            if (movingPiece instanceof King) {
                if (movingPiece.getColor() == PlayerColor.WHITE) whiteKing = movingPiece;
                else blackKing = movingPiece;
            }
        }
        if (capturedPiece != null) capturePiece(capturedPiece);
    }

    private void capturePiece(Piece capturedPiece) {
        if (capturedPiece.getColor() == PlayerColor.WHITE) {
            whitePieces.remove(capturedPiece);
            blackMaterial += capturedPiece.getValue();
        }
        else {
            blackPieces.remove(capturedPiece);
            whiteMaterial += capturedPiece.getValue();
        }
    }

    private void undoMove(Move move) {
        Piece piece = move.getMovedPiece();
        piece.decrementMoveCount();

        // Intoarcem piesa principala la pozitia initiala
        setPiece(move.getToRow(), move.getToCol(), null);
        setPiece(move.getFromRow(), move.getFromCol(), piece);
        piece.setRow(move.getFromRow());
        piece.setCol(move.getFromCol());

        if (piece.getClass().getSimpleName().equals("King")) {
            if (piece.getColor() == PlayerColor.WHITE) setWhiteKing(piece);
            else setBlackKing(piece);
        }

        // Punem inapoi piesa capturata
        if (move.getCapturedPiece() != null) {
            Piece captured = move.getCapturedPiece();
            setPiece(captured.getRow(), captured.getCol(), captured);
            if (captured.getColor() == PlayerColor.WHITE) {
                whitePieces.add(captured);
                blackMaterial -= captured.getValue();
            } else {
                blackPieces.add(captured);
                whiteMaterial -= captured.getValue();
            }
        }

        // In cazul mutarilor speciale
        if (move.hasAdditionalMove()) {
            Move additional = move.getAdditionalMove();

            // === Rocada ===
            if (piece instanceof King && Math.abs(move.getToCol() - move.getFromCol()) >= 2) {
                Piece rook = getPiece(additional.getToRow(), additional.getToCol());
                if (rook != null) {
                    rook.decrementMoveCount();
                    setPiece(additional.getFromRow(), additional.getFromCol(), rook);
                    setPiece(additional.getToRow(), additional.getToCol(), null);
                    rook.setRow(additional.getFromRow());
                    rook.setCol(additional.getFromCol());
                }
            }

            // === En Passant ===
            else if (piece instanceof Pawn && move.getCapturedPiece() != null &&
                    getPiece(move.getToRow(), move.getToCol()) == null) {
                Piece capturedPawn = move.getCapturedPiece();
                setPiece(capturedPawn.getRow(), capturedPawn.getCol(), capturedPawn);
                if (capturedPawn.getColor() == PlayerColor.WHITE) {
                    whitePieces.add(capturedPawn);
                    blackMaterial -= capturedPawn.getValue();
                } else {
                    blackPieces.add(capturedPawn);
                    whiteMaterial -= capturedPawn.getValue();
                }
            }
        }
    }



    private void redoMove(Move move) {
        movePiece(move); // refolosim logica pentru mutarea pieselor pentru refacerea turelor
    }


    public int getWhiteMaterial() {
        return whiteMaterial;
    }

    public int getBlackMaterial() {
        return blackMaterial;
    }

    public void setWhiteMaterial(int whiteMaterial) {
        this.whiteMaterial = whiteMaterial;
    }

    public void setBlackMaterial(int blackMaterial) {
        this.blackMaterial = blackMaterial;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public Piece getWhiteKing() {
        return whiteKing;
    }

    public void setWhiteKing(Piece whiteKing) {
        this.whiteKing = whiteKing;
    }

    public Piece getBlackKing() {
        return blackKing;
    }

    public void setBlackKing(Piece blackKing) {
        this.blackKing = blackKing;
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    // Functia pentru schimbarea turelor
    public void switchPlayerTurn() {
        if (currentPlayer == PlayerColor.WHITE) {
            currentPlayer = PlayerColor.BLACK;
            checkingPieces = checkCheck(whitePieces, blackKing, this);
        } else {
            currentPlayer = PlayerColor.WHITE;
            checkingPieces = checkCheck(blackPieces, whiteKing, this);
        }
        playerChecked = !checkingPieces.isEmpty();
        currentTurn++;
    }

    public ChessBoardModel cloneBoard() {
        ChessBoardModel clone = new ChessBoardModel(true, whitePlayer, blackPlayer);
        clone.whitePieces = new ArrayList<>();
        clone.blackPieces = new ArrayList<>();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p != null) {
                    Piece newPiece = copyPiece(p);

                    if (newPiece != null) {
                        clone.setPiece(r, c, newPiece);
                        if (newPiece.getColor() == PlayerColor.WHITE) {
                            if (newPiece instanceof King) clone.setWhiteKing(newPiece);
                            clone.whitePieces.add(newPiece);
                        } else {
                            if (newPiece instanceof King) clone.setBlackKing(newPiece);
                            clone.blackPieces.add(newPiece);
                        }
                    }
                }
            }
        }

        clone.setCurrentPlayer(currentPlayer);
        clone.setWhiteMaterial(whiteMaterial);
        clone.setBlackMaterial(blackMaterial);
        clone.setMoveHistory(moveHistory);
        clone.setCurrentTurn(currentTurn);
        return clone;
    }



    public boolean isPlayerChecked() {
        return playerChecked;
    }

    public List<Piece> getCheckingPieces() {
        return checkingPieces;
    }

    public void setCurrentPlayer(PlayerColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Piece getLastMovedPiece() {
        return lastMovedPiece;
    }

    public void setLastMovedPiece(Piece lastMovedPiece) {
        this.lastMovedPiece = lastMovedPiece;
    }

    public Piece getKing(PlayerColor color) {
        return color == PlayerColor.WHITE ? whiteKing : blackKing;
    }

    public List<Piece> getAllPieces(PlayerColor color) {
        return color == PlayerColor.WHITE ? whitePieces : blackPieces;
    }

    public PlayerColor getOpponent(PlayerColor color) {
        return color == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    public PlayerModel getWhitePlayer() {
        return whitePlayer;
    }

    public PlayerModel getBlackPlayer() {
        return blackPlayer;
    }

    public MoveHistory getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(MoveHistory moveHistory) {
        this.moveHistory = moveHistory;
    }

    public boolean canUndo() {
        return moveHistory.getCurrentIndex() >= 2;
    }

    public boolean canRedo() {
        return moveHistory.getCurrentIndex() + 2 <= moveHistory.getSize();
    }

    public void undoLastFullTurn() {
        for (int i = 0; i < 2; i++) {
            if (!moveHistory.hasPrevious()) return;
            Move move = moveHistory.undoMove();
            undoMove(move);
        }
        currentTurn--;
    }

    public void redoNextFullTurn() {
        for (int i = 0; i < 2; i++) {
            if (!moveHistory.hasNext()) return;
            Move move = moveHistory.redoMove();
            redoMove(move);
        }
        currentTurn++;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }
}
