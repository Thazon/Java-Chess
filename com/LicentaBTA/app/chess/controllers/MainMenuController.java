package com.LicentaBTA.app.chess.controllers;

import com.LicentaBTA.app.chess.models.PlayerModel;
import com.LicentaBTA.app.chess.enums.PlayerType;
import com.LicentaBTA.app.chess.enums.AlgorithmType;
import com.LicentaBTA.app.chess.views.MainMenuView;

import javax.swing.*;

public class MainMenuController {
    private final MainMenuView view;
    private final PlayerModel whitePlayer = new PlayerModel();
    private final PlayerModel blackPlayer = new PlayerModel();

    public MainMenuController(MainMenuView view) {
        this.view = view;

        view.getPlayerButton(1).addActionListener(_ -> togglePlayer(1));
        view.getPlayerButton(2).addActionListener(_ -> togglePlayer(2));

        view.getAlgoBox(1).addActionListener(_ -> {
            if (whitePlayer.getPlayerType() == PlayerType.PC) {
                AlgorithmType selected = (AlgorithmType) view.getAlgoBox(1).getSelectedItem();
                whitePlayer.setAlgorithm(selected);
            }
        });

        view.getAlgoBox(2).addActionListener(_ -> {
            if (blackPlayer.getPlayerType() == PlayerType.PC) {
                AlgorithmType selected = (AlgorithmType) view.getAlgoBox(2).getSelectedItem();
                blackPlayer.setAlgorithm(selected);
            }
        });

        view.getPlayButton().addActionListener(_ -> startGame());
    }

    private void togglePlayer(int playerNum) {
        PlayerModel model = playerNum == 1 ? whitePlayer : blackPlayer;
        JButton button = view.getPlayerButton(playerNum);
        JComboBox<AlgorithmType> algoBox = view.getAlgoBox(playerNum);

        model.togglePlayerType();

        if (model.getPlayerType() == PlayerType.PC) {
            button.setText("PC");
            algoBox.setVisible(true);
            model.setAlgorithm((AlgorithmType) algoBox.getSelectedItem());
        } else {
            button.setText("Player");
            algoBox.setVisible(false);
        }

        view.revalidate();
        view.repaint();
    }

    private void startGame() {
        System.out.println("Starting game...");
        System.out.print("Player 1: " + whitePlayer.getPlayerType());
        if (whitePlayer.getPlayerType() == PlayerType.PC) System.out.println(", Algo: " + whitePlayer.getAlgorithm());
        else System.out.print("\n");
        System.out.print("Player 2: " + blackPlayer.getPlayerType());
        if (blackPlayer.getPlayerType() == PlayerType.PC) System.out.println(", Algo: " + blackPlayer.getAlgorithm());
        else System.out.print("\n");

        // Incepem jocul de sah cu parametrii setati
        new com.LicentaBTA.app.chess.views.ChessGameView(whitePlayer, blackPlayer);
    }


}
