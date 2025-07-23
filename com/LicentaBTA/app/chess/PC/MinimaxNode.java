package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Move;

import java.util.ArrayList;
import java.util.List;

public class MinimaxNode {
    private ChessBoardModel board;
    private Move move;
    private List<MinimaxNode> children = new ArrayList<>();

    MinimaxNode(ChessBoardModel board, Move move) {
        this.board = board;
        this.move = move;
    }

    MinimaxNode findChild(Move move) {
        for (MinimaxNode child : children) {
            if (child.move != null && child.move.equals(move)) {
                return child;
            }
        }
        return null;
    }

    public ChessBoardModel getBoard() {
        return board;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }
}