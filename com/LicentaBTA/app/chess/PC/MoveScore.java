package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.models.Move;

public class MoveScore {
    private Move move;
    private int score;

    MoveScore(Move move, int score) {
        this.move = move;
        this.score = score;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public int getScore() {
        return score;
    }
}