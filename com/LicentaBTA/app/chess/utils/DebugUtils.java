package com.LicentaBTA.app.chess.utils;

import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Piece;

import javax.swing.*;
import java.awt.*;

public class DebugUtils {
    public static void showBoardInWindow(ChessBoardModel model, String title) {
        JFrame frame = new JFrame("Debug: " + title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = model.getPiece(row, col);
                JButton button = new JButton();
                button.setEnabled(false);

                if (piece != null) {
                    button.setText(piece.getClass().getSimpleName().substring(0, 1));
                    button.setForeground(piece.getColor() == PlayerColor.WHITE ? Color.WHITE : Color.BLACK);
                }

                button.setBackground((row + col) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                boardPanel.add(button);
            }
        }

        frame.add(boardPanel);
        frame.setVisible(true);
    }
}
