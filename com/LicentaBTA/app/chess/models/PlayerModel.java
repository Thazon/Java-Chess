package com.LicentaBTA.app.chess.models;

import com.LicentaBTA.app.chess.enums.AlgorithmType;
import com.LicentaBTA.app.chess.enums.PlayerType;

public class PlayerModel {
    private PlayerType playerType;
    private AlgorithmType algorithm;

    public PlayerModel() {
        this.playerType = PlayerType.HUMAN;
        this.algorithm = AlgorithmType.MINIMAX;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void togglePlayerType() {
        if (playerType == PlayerType.HUMAN) playerType = PlayerType.PC;
        else playerType = PlayerType.HUMAN;
    }

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmType algorithm) {
        this.algorithm = algorithm;
    }
}
