package com.LicentaBTA.app.chess.enums;

public enum PlayerColor {
    WHITE("White"),
    BLACK("Black");

    private final String displayName;

    PlayerColor(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
