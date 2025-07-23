package com.LicentaBTA.app.chess.views;

import com.LicentaBTA.app.chess.controllers.ChessController;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Piece;
import com.LicentaBTA.app.chess.enums.TileColor;  // Import the TileColor enum
import com.LicentaBTA.app.chess.enums.PlayerColor; // Import Color enum for checking current player

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChessBoardView extends JPanel {
    private ChessBoardModel model;
    private ChessController controller;
    private JButton[][] tiles;
    private JLabel turnLabel; // Label pentru afisarea turei
    private JLabel CPUStatusLabel; // Label pentru tura unui CPU
    private JButton undoButton;
    private JButton redoButton;
    private JTextArea moveHistory;


    public ChessBoardView(ChessBoardModel model) {
        this.model = model;
        initializeBoard();
    }

    public void setController(ChessController controller) {
        this.controller = controller;
    }

    private void initializeBoard() {
        setLayout(new BorderLayout());

        // Creem panoul pentru Label-ul de tura
        JPanel topPanel = new JPanel(new BorderLayout());

        turnLabel = new JLabel("White's Turn", JLabel.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(turnLabel, BorderLayout.CENTER);

        CPUStatusLabel = new JLabel("", JLabel.CENTER);
        CPUStatusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        topPanel.add(CPUStatusLabel, BorderLayout.SOUTH);

        // Cream o zona pentru istoric
        moveHistory = new JTextArea(20, 20);
        moveHistory.setEditable(false);
        moveHistory.setFont(new Font("Arial", Font.PLAIN, 14));
        moveHistory.setLineWrap(true);

        // Cream un scroll pentru istoric
        JScrollPane moveHistoryScroll = new JScrollPane(moveHistory);
        moveHistoryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Adaugam istoricul
        add(moveHistoryScroll, BorderLayout.EAST);

        // Adaugam butoanele de  Undo/Redo
        JPanel buttonPanel = new JPanel();
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");

        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

        undoButton.addActionListener(e -> {
            if (controller != null) controller.handleUndo();
        });

        redoButton.addActionListener(e -> {
            if (controller != null) controller.handleRedo();
        });

        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);

        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);


        // Cream tabla de sah
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        tiles = new JButton[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

                // Folosim culorile din TileColor pentru a initializa culorile tablei
                button.setBackground((row + col) % 2 == 0 ? TileColor.LIGHT.getColor() : TileColor.DARK.getColor());

                button.addActionListener(new TileButtonListener(row, col));
                tiles[row][col] = button;
                boardPanel.add(button);
            }
        }

        // Adaugam panoul tablei
        add(boardPanel, BorderLayout.CENTER);

        refreshBoard();
    }

    public void refreshBoard() {
        int tileSize = Math.min(getWidth(), getHeight()) / 8;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = model.getPiece(row, col);
                JButton button = tiles[row][col];
                button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, tileSize / 2));
                if (piece != null) {
                    button.setText(piece.getSymbol());
                    if (piece.getColor() == PlayerColor.WHITE) {
                        button.setForeground(Color.WHITE);
                    } else {
                        button.setForeground(Color.BLACK);
                    }
                } else {
                    button.setText("");
                }
                // Resetam background-ul la normal folosind culorile potrivite
                button.setBackground((row + col) % 2 == 0 ? TileColor.LIGHT.getColor() : TileColor.DARK.getColor());
            }
        }
        if (model.isPlayerChecked()) highlightCheckingPieces(model.getCheckingPieces());
    }

    public void highlightTiles(List<Point> points) {
        if (model.isPlayerChecked()) highlightCheckingPieces(model.getCheckingPieces());
        for (Point p : points) {
            JButton button = tiles[p.x][p.y];
            button.setBackground(Color.GREEN);
        }
    }

    public void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                tiles[row][col].setBackground((row + col) % 2 == 0 ? TileColor.LIGHT.getColor() : TileColor.DARK.getColor());
            }
        }
        if (model.isPlayerChecked()) highlightCheckingPieces(model.getCheckingPieces());
    }

    public void highlightCheckingPieces(List<Piece> checkingPieces) {
        for (Piece piece : checkingPieces) {
            JButton button = tiles[piece.getRow()][piece.getCol()];
            button.setBackground(Color.RED);
        }
    }

    // Method to update the turn label
    public void updateTurnLabel(PlayerColor currentPlayer) {
        String playerTurn = (currentPlayer == PlayerColor.WHITE) ? "White's Turn" : "Black's Turn";
        turnLabel.setText(playerTurn);
    }

    private class TileButtonListener implements ActionListener {
        private final int row;
        private final int col;

        public TileButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (controller != null) {
                controller.onTileClicked(row, col);
            }
        }
    }

    public void setUndo(boolean enabled) {
        undoButton.setEnabled(enabled);
    }

    public void setRedo(boolean enabled) {
        redoButton.setEnabled(enabled);
    }

    public void addMoveToHistory(String moveText) {
        moveHistory.append(moveText + "\n");
        moveHistory.setCaretPosition(moveHistory.getDocument().getLength());
    }

    public void resetMoveHistory() {
        moveHistory.setText("");
    }

    public void showAIThinking(String algorithmName) {
        CPUStatusLabel.setText(algorithmName + " is thinking...");
    }
    public void clearAIThinking() {
        CPUStatusLabel.setText("");
    }

}
