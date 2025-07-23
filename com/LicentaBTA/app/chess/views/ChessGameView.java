package com.LicentaBTA.app.chess.views;

import com.LicentaBTA.app.chess.controllers.ChessController;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.PlayerModel;

import javax.swing.*;
import java.awt.*;

public class ChessGameView extends JFrame {
    private ChessBoardModel model;
    private ChessBoardView boardView;
    private ChessController controller;
    private PlayerModel whitePlayer;
    private PlayerModel blackPlayer;

    public ChessGameView(PlayerModel whitePlayer, PlayerModel blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new ChessBoardModel(false, whitePlayer, blackPlayer);
        boardView = new ChessBoardView(model);
        controller = new ChessController(model, boardView);

        boardView.setController(controller);

        add(boardView, BorderLayout.CENTER);

        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        boardView.refreshBoard();
    }

    public void resetGame() {
        model = new ChessBoardModel(false, whitePlayer, blackPlayer);
        boardView = new ChessBoardView(model);
        controller = new ChessController(model, boardView);
        boardView.setController(controller);

        getContentPane().removeAll();
        add(boardView, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
