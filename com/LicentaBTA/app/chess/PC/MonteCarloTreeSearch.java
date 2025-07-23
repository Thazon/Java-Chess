package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.enums.AlgorithmType;
import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Move;

import java.util.List;
import java.util.concurrent.*;

public class MonteCarloTreeSearch implements Algorithm {
    private final AlgorithmType algorithmType = AlgorithmType.MONTE_CARLO_TREE_SEARCH;
    private final int simulation_count;
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    public MonteCarloTreeSearch(int simulation_count) {
        this.simulation_count = simulation_count;
    }

    @Override
    public Move findBestMove(ChessBoardModel model, PlayerColor color) {
        MCTSNode root = new MCTSNode(null, null, model.cloneBoard(), color);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Callable<Void>> tasks = new CopyOnWriteArrayList<>();

        for (int i = 0; i < simulation_count; i++) {
            tasks.add(() -> {
                MCTSNode node = root.select();
                node.expand();
                int result = node.simulate();
                node.backpropagate(result);
                return null;
            });
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }

        MCTSNode bestChild = root.getBestChild();
        return bestChild != null ? bestChild.getMove() : null;
    }

    public String getAlgorithmType() {
        return algorithmType.toString();
    }
}
