package com.LicentaBTA.app.chess.enums;

public enum PlayerType {
    HUMAN("Human"),
    PC("PC");

    private final String displayName;

    PlayerType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
