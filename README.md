# Java Chess Engine with Minimax, Alpha-Beta Pruning and MCTS

A Java-based Chess engine that integrates multiple Artificial Intelligence algorithms.
Designed for educational purposes as my bachelor's project.

## Features

- Full chess engine with rule validation, move generation and board state tracking
- AI player implementations:
 - **Minimax**
 - **Alpha-Beta Pruning Minimax**
 - **Monte Carlo Tree Search**
- Modular codebase
- Multithreaded evaluation using Java's ExecutorService
- Board evaluation heuristics (material, mobility, control etc)
- Built entirely in Java

## Algorithm Overview

### Minimax
Classic decision tree algorithm that explores all future game states to a certain depth and returns the best future for
said depth

### Alpha-Beta Pruning Minimax
Optimized version of the Minimax algorithm that avoids evaluating branches that won't influence the final decision

### Monte Carlo Tree Search (MCTS)
An exploration focused algorithm which runs random simulations to determine the statistically best move

## Project Structure

```
src/
└── com/
    └── LicentaBTA/
        └── app/
            └── chess/
                Main.java                         # Entry point for the application  
                AppManager.java                   # Initializes the main menu

                controllers/
                    ChessController.java          # Handles game logic  
                    MainMenuController.java       # Handles main menu logic

                enums/
                    AlgorithmType.java            # AI algorithm types  
                    PlayerColor.java              # Player color enum  
                    PlayerType.java               # Human or AI  
                    TileColor.java                # Board tile colors and hex codes

                models/
                    Piece.java                    # Abstract base class for chess pieces  
                    Bishop.java                   # Bishop implementation  
                    Knight.java                   # Knight implementation  
                    Rook.java                     # Rook implementation  
                    Queen.java                    # Queen implementation  
                    King.java                     # King implementation  
                    Pawn.java                     # Pawn implementation  
                    ChessBoardModel.java          # Represents and clones the board  
                    Move.java                     # Represents a move  
                    PieceFactory.java             # Factory to instantiate pieces  
                    PlayerModel.java              # Holds player info and AI type

                PC/
                    Algorithm.java                # Interface for AI algorithms  
                    AlgorithmFactory.java         # Factory for algorithm instances  
                    Minimax.java                  # Minimax algorithm  
                    AlphaBetaMinimax.java         # Minimax with Alpha-Beta Pruning  
                    Negamax.java                  # Negamax implementation  
                    MonteCarloTreeSearch.java     # Monte Carlo Tree Search  
                    MCTSNode.java                 # MCTS node structure  
                    MinimaxNode.java              # Node structure for Minimax variants  
                    MoveScore.java                # Score evaluation wrapper  
                    RandomMoveAlgorithm.java      # Random move AI

                utils/
                    BoardUtils.java               # General board utilities  
                    MoveGenerator.java            # Move generation logic  
                    ScoreUtils.java               # Board evaluation heuristics  
                    MoveHistory.java              # Tracks move history (undo/redo)  
                    DebugUtils.java               # Debugging utilities

                views/
                    ChessBoardView.java           # UI for the chess board  
                    ChessGameView.java            # UI for chess gameplay  
                    MainMenuView.java             # UI for the main menu
```

## Future Improvements

- Online Multiplayer support
- Deep Learning-based evaluation
- PGN import/export

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

Email: badoitiberiualexandru@gmail.com
[LinkedIn](www.linkedin.com/in/tiberiu-alexandru-badoi-b5b902224)
