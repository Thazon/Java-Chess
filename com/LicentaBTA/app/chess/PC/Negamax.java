package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.enums.AlgorithmType;
import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.*;
import com.LicentaBTA.app.chess.utils.BoardUtils;
import com.LicentaBTA.app.chess.utils.MoveGenerator;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.*;

public class Negamax implements Algorithm {
    private final AlgorithmType algorithmType = AlgorithmType.NEGAMAX;
    private final int maxDepth;
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    public Negamax(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public Move findBestMove(ChessBoardModel model, PlayerColor color) {
        List<Future<MoveEvaluation>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Piece> pieces = (color == PlayerColor.WHITE) ? model.getWhitePieces() : model.getBlackPieces();

        for (Piece piece : pieces) {
            List<Point> legalMoves = MoveGenerator.generateLegalMoves(piece, model);
            for (Point to : legalMoves) {
                Callable<MoveEvaluation> task = () -> {
                    ChessBoardModel cloned = model.cloneBoard();
                    Piece moving = cloned.getPiece(piece.getRow(), piece.getCol());
                    Piece target = cloned.getPiece(to.x, to.y);
                    Move move = new Move(piece.getRow(), piece.getCol(), to.x, to.y, moving, target);

                    cloned.movePiece(move);
                    cloned.switchPlayerTurn();

                    int value = -negamax(cloned, maxDepth - 1, -Integer.MAX_VALUE, Integer.MAX_VALUE, getOpponent(color));
                    return new MoveEvaluation(move, value);
                };
                futures.add(executor.submit(task));
            }
        }

        executor.shutdown();

        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (Future<MoveEvaluation> future : futures) {
            try {
                MoveEvaluation result = future.get();
                if (result.evaluation > bestValue) {
                    bestValue = result.evaluation;
                    bestMove = result.move;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return bestMove;
    }

    private int negamax(ChessBoardModel model, int depth, int alpha, int beta, PlayerColor color) {
        if (depth == 0) {
            return BoardUtils.evaluateBoard(model, color);
        }

        int maxEval = Integer.MIN_VALUE;
        List<Piece> pieces = (color == PlayerColor.WHITE) ? model.getWhitePieces() : model.getBlackPieces();

        for (Piece piece : pieces) {
            List<Point> legalMoves = MoveGenerator.generateLegalMoves(piece, model);
            for (Point to : legalMoves) {
                ChessBoardModel cloned = model.cloneBoard();
                Piece moving = cloned.getPiece(piece.getRow(), piece.getCol());
                Piece target = cloned.getPiece(to.x, to.y);
                Move move = new Move(piece.getRow(), piece.getCol(), to.x, to.y, moving, target);

                cloned.movePiece(move);
                cloned.switchPlayerTurn();

                int eval = -negamax(cloned, depth - 1, -beta, -alpha, getOpponent(color));
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (alpha >= beta) return alpha;  // Alpha-Beta pruning
            }
        }

        return maxEval;
    }

    private PlayerColor getOpponent(PlayerColor color) {
        return (color == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    public String getAlgorithmType() {
        return algorithmType.toString();
    }

    private static class MoveEvaluation {
        final Move move;
        final int evaluation;

        MoveEvaluation(Move move, int evaluation) {
            this.move = move;
            this.evaluation = evaluation;
        }
    }
}
