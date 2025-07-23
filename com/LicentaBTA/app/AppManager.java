package com.LicentaBTA.app;

import com.LicentaBTA.app.chess.controllers.MainMenuController;
import com.LicentaBTA.app.chess.views.MainMenuView;

import javax.swing.*;

public class AppManager {
    private static JFrame mainFrame;

    public static void start() {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Chess");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(400, 400);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setResizable(false);
            showMainMenu();
            mainFrame.setVisible(true);
        });
    }

    public static void showMainMenu() {
        MainMenuView menuView = new MainMenuView();
        new MainMenuController(menuView);
        mainFrame.setContentPane(menuView);
        mainFrame.revalidate();
    }
}
