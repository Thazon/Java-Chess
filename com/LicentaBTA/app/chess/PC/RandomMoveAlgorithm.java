package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.enums.AlgorithmType;
import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Move;

import java.util.List;
import java.util.Random;

import static com.LicentaBTA.app.chess.utils.MoveGenerator.generateAllMoves;

public class RandomMoveAlgorithm implements Algorithm {
    private final AlgorithmType algorithmType = AlgorithmType.RANDOM_MOVE;
    private final Random random = new Random();

    @Override
    public Move findBestMove(ChessBoardModel model, PlayerColor color) {

        List<Move> allMoves = generateAllMoves(model, color);

        if (allMoves.isEmpty()) return null;
        return allMoves.get(random.nextInt(allMoves.size()));
    }

    public String getAlgorithmType() {
        return algorithmType.toString();
    }
}
