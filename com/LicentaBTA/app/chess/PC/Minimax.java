package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.enums.AlgorithmType;
import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Move;
import com.LicentaBTA.app.chess.utils.MoveGenerator;

import java.util.*;
import java.util.concurrent.*;

import static com.LicentaBTA.app.chess.utils.BoardUtils.evaluateBoard;

public class Minimax implements Algorithm {
    private final AlgorithmType algorithmType = AlgorithmType.MINIMAX;
    private final int maxDepth;
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors() - 2)
    );

    MinimaxNode rootNode = null;

    public Minimax(int depth) {
        this.maxDepth = depth;
    }

    @Override
    public Move findBestMove(ChessBoardModel model, PlayerColor color) {
        Move lastMove = model.getMoveHistory().getLastMove();

        // Updatam rootNode sa fie ramura pentru ultima mutare
        if (rootNode != null && lastMove != null) {
            rootNode = rootNode.findChild(lastMove);
        }

        if (rootNode == null || rootNode.getBoard() == null) {
            rootNode = new MinimaxNode(model.cloneBoard(), null);
        }

        List<Move> legalMoves = MoveGenerator.generateAllMoves(rootNode.getBoard(), color);
        if (legalMoves.isEmpty()) return null;

        List<Future<MoveScore>> futures = new ArrayList<>();

        for (Move move : legalMoves) {
            Callable<MoveScore> task = () -> {
                ChessBoardModel boardCopy = rootNode.getBoard().cloneBoard();
                boardCopy.movePiece(move);
                boardCopy.switchPlayerTurn();

                MinimaxNode childNode = new MinimaxNode(boardCopy, move);
                int score = minimax(childNode, maxDepth - 1, color, false);
                return new MoveScore(move, score);
            };
            futures.add(executor.submit(task));
        }

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        try {
            for (Future<MoveScore> future : futures) {
                MoveScore result = future.get();
                if (result != null && result.getScore() > bestScore) {
                    bestScore = result.getScore();
                    bestMove = result.getMove();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error computing best move", e);
        }

        // Update rootNode to the chosen move's child node
        if (bestMove != null) {
            ChessBoardModel nextBoard = rootNode.getBoard().cloneBoard();
            nextBoard.movePiece(bestMove);
            nextBoard.switchPlayerTurn();
            rootNode = new MinimaxNode(nextBoard, bestMove); // Scapam de ramurile nefolosite
        }

        return bestMove;
    }

    private int minimax(MinimaxNode node, int depth, PlayerColor maxColor, boolean isMaximizing) {
        if (depth == 0) {
            return evaluateBoard(node.getBoard(), maxColor);
        }

        PlayerColor currentPlayer = isMaximizing ? maxColor : node.getBoard().getOpponent(maxColor);
        List<Move> moves = MoveGenerator.generateAllMoves(node.getBoard(), currentPlayer);
        if (moves.isEmpty()) {
            return evaluateBoard(node.getBoard(), maxColor);
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Move move : moves) {
            ChessBoardModel boardCopy = node.getBoard().cloneBoard();
            boardCopy.movePiece(move);
            boardCopy.switchPlayerTurn();

            MinimaxNode child = new MinimaxNode(boardCopy, move);
            int score = minimax(child, depth - 1, maxColor, !isMaximizing);

            bestScore = isMaximizing
                    ? Math.max(bestScore, score)
                    : Math.min(bestScore, score);
        }

        return bestScore;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public String getAlgorithmType() {
        return algorithmType.toString();
    }
}
