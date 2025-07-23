package com.LicentaBTA.app.chess.enums;

import java.awt.Color;

public enum TileColor {
    LIGHT(Color.decode("#C8C8C8")),  // Gri deschis
    DARK(Color.decode("#505050"));   // Gri inchis

    private final Color color;

    TileColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
