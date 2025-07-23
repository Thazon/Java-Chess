package com.LicentaBTA.app.chess.views;

import com.LicentaBTA.app.chess.enums.AlgorithmType;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JPanel {
    private final JButton player1Button, player2Button;
    private final JComboBox<AlgorithmType> player1AlgoBox, player2AlgoBox;
    private final JButton playButton;

    public MainMenuView() {
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Player 1
        JLabel p1Label = new JLabel("White", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(p1Label, gbc);

        player1Button = new JButton("Player");
        gbc.gridy = 1;
        centerPanel.add(player1Button, gbc);

        player1AlgoBox = new JComboBox<>(AlgorithmType.values());
        gbc.gridy = 2;
        centerPanel.add(player1AlgoBox, gbc);
        player1AlgoBox.setVisible(false);

        // Player 2
        JLabel p2Label = new JLabel("Black", SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(p2Label, gbc);

        player2Button = new JButton("Player");
        gbc.gridy = 1;
        centerPanel.add(player2Button, gbc);

        player2AlgoBox = new JComboBox<>(AlgorithmType.values());
        gbc.gridy = 2;
        centerPanel.add(player2AlgoBox, gbc);
        player2AlgoBox.setVisible(false);

        // Play Button
        playButton = new JButton("Play");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        centerPanel.add(playButton, gbc);

        setVisible(true);
    }

    public JButton getPlayerButton(int playerNum) {
        return playerNum == 1 ? player1Button : player2Button;
    }

    public JComboBox<AlgorithmType> getAlgoBox(int playerNum) {
        return playerNum == 1 ? player1AlgoBox : player2AlgoBox;
    }

    public JButton getPlayButton() {
        return playButton;
    }
}
