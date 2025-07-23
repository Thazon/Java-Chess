package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.enums.AlgorithmType;
import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Move;
import com.LicentaBTA.app.chess.utils.MoveGenerator;

import java.util.*;
import java.util.concurrent.*;

import static com.LicentaBTA.app.chess.utils.BoardUtils.evaluateBoard;


public class AlphaBetaMinimax extends Minimax {

    private final AlgorithmType algorithmType = AlgorithmType.ALPHA_BETA_PRUNING;

    public AlphaBetaMinimax(int depth) {
        super(depth);
    }

    @Override
    public Move findBestMove(ChessBoardModel model, PlayerColor color) {
        Move lastMove = model.getMoveHistory().getLastMove();

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
                int score = alphabeta(childNode, getMaxDepth() - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, color, false);
                return new MoveScore(move, score);
            };
            futures.add(getExecutor().submit(task));
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

        if (bestMove != null) {
            ChessBoardModel nextBoard = rootNode.getBoard().cloneBoard();
            nextBoard.movePiece(bestMove);
            nextBoard.switchPlayerTurn();
            rootNode = new MinimaxNode(nextBoard, bestMove);
        }

        return bestMove;
    }

    private int alphabeta(MinimaxNode node, int depth, int alpha, int beta, PlayerColor maxColor, boolean isMaximizing) {
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
            int score = alphabeta(child, depth - 1, alpha, beta, maxColor, !isMaximizing);

            if (isMaximizing) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, score);
            } else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, score);
            }

            if (beta <= alpha) {
                break; // Nu continuam in cazul in care scorul este prea mic
            }
        }

        return bestScore;
    }

    public String getAlgorithmType() {
        return algorithmType.toString();
    }
}