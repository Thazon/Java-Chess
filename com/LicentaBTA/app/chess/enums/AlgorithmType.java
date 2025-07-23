package com.LicentaBTA.app.chess.enums;

public enum AlgorithmType {
    MINIMAX("Minimax"),
    ALPHA_BETA_PRUNING("Alpha-Beta Pruning"),
    MONTE_CARLO_TREE_SEARCH("Monte Carlo Tree Search"),
    NEGAMAX("Negamax"),
    RANDOM_MOVE("Random Move");

    private final String displayName;

    AlgorithmType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
