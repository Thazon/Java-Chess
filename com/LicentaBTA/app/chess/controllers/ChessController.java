package com.LicentaBTA.app.chess.controllers;

import com.LicentaBTA.app.AppManager;
import com.LicentaBTA.app.chess.PC.Algorithm;
import com.LicentaBTA.app.chess.PC.AlgorithmFactory;
import com.LicentaBTA.app.chess.enums.PlayerType;
import com.LicentaBTA.app.chess.models.*;
import com.LicentaBTA.app.chess.views.ChessBoardView;
import com.LicentaBTA.app.chess.enums.PlayerColor;

import javax.swing.*;
import java.awt.Point;
import java.util.List;

import static com.LicentaBTA.app.chess.utils.BoardUtils.*;
import static com.LicentaBTA.app.chess.utils.MoveGenerator.generateLegalMoves;

public class ChessController {
    private final ChessBoardModel model;
    private final ChessBoardView view;
    private Piece selectedPiece;
    private List<Point> legalMoves;
    private final Algorithm whiteAlgorithm;
    private final Algorithm blackAlgorithm;
    // Declaratii pentru mesajele folosite de fereastra de final joc
    String gameOver = "<html><center><h2>Checkmate!</h2><p>";
    String stalemate = "<html><center><h2>Stalemate!</h2><p>";
    String wrongGameOver = "<html><center><h2>Excuse me, HOW?</h2><p>";

    public ChessController(ChessBoardModel model, ChessBoardView view) {
        this.model = model;
        this.view = view;
        this.selectedPiece = null;
        //Initializarea verifica daca se foloseste un singur algoritm sau doi si initiaza pe acestia corespunzator
        if (model.getWhitePlayer().getPlayerType() == PlayerType.PC &&
                model.getBlackPlayer().getPlayerType() == PlayerType.PC &&
                model.getWhitePlayer().getAlgorithm() == model.getBlackPlayer().getAlgorithm()) {
            Algorithm sharedAlgorithm = AlgorithmFactory.getAlgorithm(model.getWhitePlayer().getAlgorithm());
            whiteAlgorithm = sharedAlgorithm;
            blackAlgorithm = sharedAlgorithm;
        }
        else {
            this.whiteAlgorithm = model.getWhitePlayer().getPlayerType() == PlayerType.PC
                    ? AlgorithmFactory.getAlgorithm(model.getWhitePlayer().getAlgorithm())
                    : null;
            this.blackAlgorithm = model.getBlackPlayer().getPlayerType() == PlayerType.PC
                    ? AlgorithmFactory.getAlgorithm(model.getBlackPlayer().getAlgorithm())
                    : null;
        }
        CPUMove();
    }

    private void CPUMove() {
        // Mai intai, verificam daca folosim un algoritm. Daca nu, dam tura jucatorului uman.
        Algorithm algorithm = (model.getCurrentPlayer() == PlayerColor.WHITE) ? whiteAlgorithm : blackAlgorithm;
        if (algorithm == null) {
            updateUndoRedoButtons();
            return;
        }
        view.showAIThinking(algorithm.getAlgorithmType());

        // Tura jucatorului controlat de calculator este pusa pe un alt Thread pentru a nu interfera cu afisarea UI-ului
        new Thread(() -> {
            view.setUndo(false);
            view.setRedo(false);
            // Acesta alege cea mai buna miscare bazat pe algoritmul selectat pentru jucatorul curent.
            Move bestMove = algorithm.findBestMove(model.cloneBoard(), model.getCurrentPlayer());
            // Verificare redundanta pentru null. Dar este o siguranta pentru cazul in care cumva ar ajunge sa nu primeasca o mutare
            if (bestMove != null) {
                SwingUtilities.invokeLater(() -> {
                    model.movePiece(bestMove);
                    model.getMoveHistory().recordMove(bestMove);
                    model.setLastMovedPiece(model.getPiece(bestMove.getToRow(), bestMove.getToCol()));
                    // Promovam automat pionii la regina, aceasta fiind promovarea optima in 99% din cazuri
                    Piece moved = model.getPiece(bestMove.getToRow(), bestMove.getToCol());
                    if (moved instanceof Pawn) {
                        int targetRow = bestMove.getToRow();
                        if (targetRow == 0 || targetRow == 7) {
                            Piece promotedPiece = new Queen(targetRow, bestMove.getToCol(), moved.getColor());
                            model.setPiece(targetRow, bestMove.getToCol(), promotedPiece);
                        }
                    }
                    updateMoveHistoryView();
                    view.clearAIThinking();
                    model.switchPlayerTurn();
                    view.updateTurnLabel(model.getCurrentPlayer());
                    view.refreshBoard();
                    if (checkmate(model.getCurrentPlayer(), model)) {
                        String winner = (model.getCurrentPlayer() == PlayerColor.WHITE) ? "Black" : "White";
                        gameOver(gameOver, winner);
                    }
                    else if (isStalemate(model)) {
                        String winner = "Nobody";
                        gameOver(stalemate, winner);
                    }
                    else CPUMove(); // Continuam cu un alt apel la CPUMove() pentru a incepe urmatoarea tura
                });
            }
        }).start();
    }


    public void onTileClicked(int row, int col) {
        PlayerModel currentPlayer = (model.getCurrentPlayer() == PlayerColor.WHITE)
                ? model.getWhitePlayer()
                : model.getBlackPlayer();
        // Daca jucatorul curent nu este uman, nu permitem utilizatorului sa interfere cu jocul
        if (currentPlayer.getPlayerType() != PlayerType.HUMAN) return;
        PlayerColor currentPlayerColor = model.getCurrentPlayer();
        Piece clickedPiece = model.getPiece(row, col);
        Move currentMove;
        Move additionalMove = null;
        Piece captured;

        if (selectedPiece == null) {
            // Daca nu avem o piesa selectata, folosim functia showLegalMoves pentru a o selecta si afisa mutarile
            if (clickedPiece != null && clickedPiece.getColor() == currentPlayerColor) {
                showLegalMoves(clickedPiece);
            }
        } else {
            // Avem deja o piesa selectata
            if (clickedPiece == selectedPiece) {
                // Deselectam piesa daca aceasta era deja selectata
                deselectPiece();
            } else if (clickedPiece != null && clickedPiece.getColor() == selectedPiece.getColor()) {
                view.clearHighlights();
                showLegalMoves(clickedPiece);
            } else if (canMoveTo(row, col)) {
                //Verificam rocada
                if (selectedPiece instanceof King && selectedPiece.getMoveCount() == 0) {
                    if (col == 6 && checkCastlingRight(selectedPiece, model)) {
                        additionalMove = new Move(row, 7, row, 5,  model.getPiece(row, 7));
                    }
                    else if (col == 1 && checkCastlingLeft(selectedPiece, model)) {
                        additionalMove = new Move(row, 0, row, 2, model.getPiece(row, 0));
                    }
                }
                // Memoram piesa capturata
                captured = model.getPiece(row, col);
                // Verificam en passant
                if (captured == null && selectedPiece instanceof Pawn && model.getLastMovedPiece() instanceof Pawn &&
                        model.getLastMovedPiece().getRow() + getDir(model) == row && model.getLastMovedPiece().getCol() == col) {
                    captured = model.getLastMovedPiece();
                    additionalMove = new Move(captured.getRow(), captured.getCol(), -1, -1, (Move) null);
                }
                // Executam mutarea si apoi o memoram in istoric
                currentMove = new Move(selectedPiece.getRow(), selectedPiece.getCol(), row, col, selectedPiece, captured, additionalMove);
                model.movePiece(currentMove);
                model.getMoveHistory().recordMove(currentMove);
                updateMoveHistoryView();
                // Verificare captura fizica pentru rege si terminare joc in cazul acesta
                if (captured instanceof King) {
                    String winner = "Glitch";
                    gameOver(wrongGameOver, winner);
                }

                // Incrementam numarul de mutari pentru piesa curenta
                selectedPiece.incrementMoveCount();


                // Verificam daca piesa este pion si daca trebuie promovata
                if (selectedPiece instanceof Pawn && (row == 0 || row == 7)) {
                    promotePawn((Pawn) selectedPiece, row, col);
                }

                // Setam ultima piesa mutata
                model.setLastMovedPiece(selectedPiece);

                // Schimbam jucatorul curent activ
                model.switchPlayerTurn();
                view.updateTurnLabel(model.getCurrentPlayer());

                // Resetam selectiile
                deselectPiece();
                updateUndoRedoButtons();
                view.refreshBoard();
                if (checkmate(model.getCurrentPlayer(), model)) {
                    String winner = (model.getCurrentPlayer() == PlayerColor.WHITE) ? "Black" : "White";
                    gameOver(gameOver, winner);
                }
                else if (isStalemate(model)) {
                    String winner = "Nobody";
                    gameOver(stalemate, winner);
                }
                else CPUMove();
            } else {
                // Daca mutarea este invalida, deselectam piesa
                deselectPiece();
            }
        }
    }

    public static int getDir(ChessBoardModel model) {
        return (model.getCurrentPlayer() == PlayerColor.WHITE) ? -1 : 1;
    }

    public void gameOver(String gameOver, String winner) {

        JDialog dialog = new JDialog((JFrame) null, "Game Over", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 300);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.setLocationRelativeTo(view);  // Centram relativ cu fereastra principala

        JLabel label = new JLabel(gameOver + winner + " wins!</p></center></html>", SwingConstants.CENTER);
        label.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JButton mainMenuButton = new JButton("Main Menu");
        JButton restartButton = new JButton("Restart");
        JButton quitButton = new JButton("Quit");

        mainMenuButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        restartButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        mainMenuButton.addActionListener(_ -> {
            dialog.dispose();
            AppManager.showMainMenu();
        });

        restartButton.addActionListener(_ -> {
            dialog.dispose();
            model.getMoveHistory().reset();
            updateMoveHistoryView();
            model.resetBoard();
            view.refreshBoard();
            CPUMove();
        });

        quitButton.addActionListener(_ -> System.exit(0));

        dialog.add(Box.createVerticalStrut(10));
        dialog.add(label);
        dialog.add(Box.createVerticalStrut(15));
        dialog.add(mainMenuButton);
        dialog.add(Box.createVerticalStrut(5));
        dialog.add(restartButton);
        dialog.add(Box.createVerticalStrut(5));
        dialog.add(quitButton);
        dialog.add(Box.createVerticalStrut(10));

        dialog.setVisible(true);
    }


    // Cand afisam mutarile legale inseamna ca piesa tocmai a fost selectata deci o selectam, generam mutarile si le afisam
    private void showLegalMoves(Piece clickedPiece) {
        selectedPiece = clickedPiece;
        legalMoves = generateLegalMoves(clickedPiece, model);
        view.highlightTiles(legalMoves);
    }

    private void deselectPiece() {
        selectedPiece = null;
        legalMoves = null;
        view.clearHighlights();
    }

    private boolean canMoveTo(int row, int col) {
        if (legalMoves == null) return false;
        return legalMoves.contains(new Point(row, col));
    }

    // Afisam fereastra de promovare a pionului
    private int showPromotionDialog() {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        return JOptionPane.showOptionDialog(
                view,
                "Choose a piece for promotion",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    // Logica de promovare pion
    private void promotePawn(Pawn pawn, int row, int col) {
        int choice;
        do {
            // Reafisam fereastra atata timp cat promovarea nu a fost facuta corespunzator
            choice = showPromotionDialog();
        } while (choice == -1);  // Keep asking if the dialog was closed or cancelled

        // Procesam alegerea jucatorului
        Piece promotedPiece = switch (choice) {
            case 1 -> new Rook(row, col, pawn.getColor());
            case 2 -> new Bishop(row, col, pawn.getColor());
            case 3 -> new Knight(row, col, pawn.getColor());
            default -> new Queen(row, col, pawn.getColor());  // Implicit Regina
        };
        model.setPiece(row, col, promotedPiece);
        view.refreshBoard();  // Refresh la tabla pentru a afisa promovarea
    }

    public void handleUndo() {
        if (model.canUndo()) {
            model.undoLastFullTurn();
            view.refreshBoard();
            view.updateTurnLabel(model.getCurrentPlayer());
            updateUndoRedoButtons();
            updateMoveHistoryView();
        }
    }

    public void handleRedo() {
        if (model.canRedo()) {
            model.redoNextFullTurn();
            view.refreshBoard();
            view.updateTurnLabel(model.getCurrentPlayer());
            updateUndoRedoButtons();
            updateMoveHistoryView();
        }
    }

    public void updateUndoRedoButtons() {
        view.setUndo(model.canUndo());
        view.setRedo(model.canRedo());
    }


    public void updateMoveHistoryView() {
        view.resetMoveHistory();
        StringBuilder historyText = new StringBuilder();
        for (int i = 0; i < model.getMoveHistory().getCurrentIndex(); i += 2) {
            Move white = model.getMoveHistory().getMoveAt(i);
            Move black = (i + 1 < model.getMoveHistory().getCurrentIndex()) ? model.getMoveHistory().getMoveAt(i + 1) : null;
            historyText.append((i / 2 + 1)).append(". ");
            if (white != null) historyText.append(white).append(" ");
            if (black != null) historyText.append(black);
            historyText.append("\n");
        }
        view.addMoveToHistory(historyText.toString());
    }


}