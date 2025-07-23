package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Move;
import com.LicentaBTA.app.chess.models.Piece;
import com.LicentaBTA.app.chess.utils.BoardUtils;
import com.LicentaBTA.app.chess.utils.MoveGenerator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MCTSNode {
    private final MCTSNode parent;
    private final Move move;
    private final ChessBoardModel state;
    private final PlayerColor player;
    private final List<MCTSNode> children = new ArrayList<>();
    private int visits = 0;
    private double score = 0;

    public MCTSNode(MCTSNode parent, Move move, ChessBoardModel state, PlayerColor player) {
        this.parent = parent;
        this.move = move;
        this.state = state;
        this.player = player;
    }

    public void expand() {
        List<Piece> pieces = (player == PlayerColor.WHITE) ? state.getWhitePieces() : state.getBlackPieces();
        for (Piece piece : pieces) {
            List<java.awt.Point> legalMoves = MoveGenerator.generateLegalMoves(piece, state);
            for (java.awt.Point pt : legalMoves) {
                ChessBoardModel cloned = state.cloneBoard();
                Piece originalPiece = cloned.getPiece(piece.getRow(), piece.getCol());
                Piece captured = cloned.getPiece(pt.x, pt.y);
                Move newMove = new Move(piece.getRow(), piece.getCol(), pt.x, pt.y, originalPiece, captured);
                cloned.movePiece(newMove);
                cloned.switchPlayerTurn();
                children.add(new MCTSNode(this, newMove, cloned, cloned.getCurrentPlayer()));
            }
        }
    }

    public MCTSNode select() {
        MCTSNode current = this;
        while (!current.children.isEmpty()) {
            current = Collections.max(current.children, Comparator.comparingDouble(MCTSNode::uctScore));
        }
        return current;
    }

    public int simulate() {
        ChessBoardModel cloned = state.cloneBoard();
        PlayerColor currentPlayer = player;
        int depth = 10; // Limita de adancime

        for (int i = 0; i < depth; i++) {
            List<Piece> pieces = (currentPlayer == PlayerColor.WHITE)
                    ? cloned.getWhitePieces()
                    : cloned.getBlackPieces();

            List<Move> allMoves = new ArrayList<>();
            for (Piece piece : pieces) {
                for (java.awt.Point pt : MoveGenerator.generateLegalMoves(piece, cloned)) {
                    Piece p = cloned.getPiece(piece.getRow(), piece.getCol());
                    Piece target = cloned.getPiece(pt.x, pt.y);
                    allMoves.add(new Move(piece.getRow(), piece.getCol(), pt.x, pt.y, p, target));
                }
            }

            if (allMoves.isEmpty()) break;

            Move randomMove = allMoves.get(ThreadLocalRandom.current().nextInt(allMoves.size()));
            cloned.movePiece(randomMove);
            cloned.switchPlayerTurn();
            currentPlayer = cloned.getCurrentPlayer();
        }

        return BoardUtils.evaluateBoard(cloned, player);
    }

    public void backpropagate(int result) {
        visits++;
        score += result;
        if (parent != null) parent.backpropagate(result);
    }

    private double uctScore() {
        if (visits == 0) return Double.POSITIVE_INFINITY;
        return (score / visits) + Math.sqrt(2 * Math.log(parent.visits + 1) / visits);
    }

    public MCTSNode getBestChild() {
        return children.stream()
                .max(Comparator.comparingInt(c -> c.visits))
                .orElse(null);
    }

    public Move getMove() {
        return move;
    }
}
