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

com.LicentaBTA.app.chess/
├── Main.java                     # Entry point for the application
├── AppManager.java               # Initializes the main menu and displays it
├── controllers/
│   ├── ChessController.java         # Handles the game logic
│   └── MainMenuController.java      # Handles the main menu logic
├── enums/
│   ├── AlgorithmType.java           # Stores the types of Algorithms
│   ├── PlayerColor.java             # Stores the two player colors
│   ├── PlayerType.java              # Stores the types of players
│   └── TileColor.java               # Board color types and their hex codes
├── models/
│   ├── Piece.java                   # Base class for all pieces
│   ├── Bishop.java                  # Bishop piece model
│   ├── Knight.java                  # Knight piece model
│   ├── Rook.java                    # Rook piece model
│   ├── Queen.java                   # Queen piece model
│   ├── King.java                    # King piece model
│   ├── Pawn.java                    # Pawn piece model
│   ├── ChessBoardModel.java         # Board data and cloning logic
│   ├── Move.java                    # Stores move information
│   ├── PieceFactory.java            # Factory to create pieces
│   └── PlayerModel.java             # Player type and AI association
├── PC/
│   ├── Algorithm.java               # Interface for AI algorithms
│   ├── AlgorithmFactory.java        # Factory for creating algorithms
│   ├── Minimax.java                 # Minimax algorithm implementation
│   ├── AlphaBetaMinimax.java        # Minimax with Alpha-Beta Pruning
│   ├── Negamax.java                 # Negamax algorithm implementation
│   ├── MonteCarloTreeSearch.java    # MCTS algorithm implementation
│   ├── MCTSNode.java                # Node class for MCTS
│   ├── MinimaxNode.java             # Node class for Minimax-based algorithms
│   ├── MoveScore.java               # Represents a move and its score
│   └── RandomMoveAlgorithm.java     # Random legal move generator
├── utils/
│   ├── BoardUtils.java              # Utility functions for board handling
│   ├── MoveGenerator.java           # Logic for generating valid moves
│   ├── ScoreUtils.java              # Heuristic evaluation functions
│   ├── MoveHistory.java             # History tracking for undo/redo
│   └── DebugUtils.java              # Debugging tools
├── views/
│   ├── ChessBoardView.java          # Responsible for displaying the chessboard
│   ├── ChessGameView.java           # Game screen layout and initialization
│   └── MainMenuView.java            # UI for the main menu

## Future Improvements

- Online Multiplayer support
- Deep Learning-based evaluation
- PGN import/export

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

Email: badoitiberiualexandru@gmail.com
[LinkedIn](www.linkedin.com/in/tiberiu-alexandru-badoi-b5b902224)
