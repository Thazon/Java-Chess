package com.LicentaBTA.app.chess.PC;

import com.LicentaBTA.app.chess.enums.PlayerColor;
import com.LicentaBTA.app.chess.models.ChessBoardModel;
import com.LicentaBTA.app.chess.models.Move;

public interface Algorithm {
    String getAlgorithmType();
    Move findBestMove(ChessBoardModel model, PlayerColor color);
}
