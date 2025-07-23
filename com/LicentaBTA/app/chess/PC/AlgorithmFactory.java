package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.enums.AlgorithmType;

public class AlgorithmFactory {
    public static Algorithm getAlgorithm(AlgorithmType type) {
        return getAlgorithm(type, 3);
    }

    public static Algorithm getAlgorithm(AlgorithmType type, int depth) {
        return switch (type) {
            case MINIMAX -> new Minimax(depth);
            case ALPHA_BETA_PRUNING -> new AlphaBetaMinimax(depth);
            case MONTE_CARLO_TREE_SEARCH -> new MonteCarloTreeSearch(depth * 1250);
            case NEGAMAX -> new Negamax(depth);
            case RANDOM_MOVE -> new RandomMoveAlgorithm();
            default -> null;
        };
    }
}
