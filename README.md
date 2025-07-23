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
├── Main.java                       # Entry point for the application
├── AppManager.java                 # Initializes the main menu and displays it
│
├── controllers/
│   ├── ChessController.java        # Handles the game logic
│   └── MainMenuController.java     # Handles the main menu logic
│
├── enums/
│   ├── AlgorithmType.java          # Stores the types of Algorithms
│   ├── PlayerColor.java            # Stores the two player colors
│   ├── PlayerType.java             # Stores the types of players
│   └── TileColor.java              # Board color types and their color hex codes
│
├── models/
│   ├── Bishop.java                 # Bishop piece model
│   ├── ChessBoardModel.java       # Board data model with cloning logic
│   ├── King.java                   # King piece model
│   ├── Knight.java                 # Knight piece model
│   ├── Move.java                   # Stores move information
│   ├── Pawn.java                   # Pawn piece model
│   ├── Piece.java                  # Base class for all chess pieces
│   ├── PieceFactory.java           # Factory to create chess pieces
│   ├── PlayerModel.java            # Stores player type and algorithm (AI/human)
│   ├── Queen.java                  # Queen piece model
│   └── Rook.java                   # Rook piece model
│
├── PC/
│   ├── Algorithm.java              # Interface for all AI algorithm classes
│   ├── AlgorithmFactory.java       # Factory for algorithm instantiation
│   ├── AlphaBetaMinimax.java       # Minimax with alpha-beta pruning
│   ├── Minimax.java                # Standard Minimax algorithm
│   ├── MinimaxNode.java            # Node structure for Minimax
│   ├── Negamax.java                # Negamax variant of Minimax
│   ├── MonteCarloTreeSearch.java   # Monte Carlo Tree Search implementation
│   ├── MCTSNode.java               # Node structure for MCTS
│   ├── RandomMoveAlgorithm.java    # AI that picks a random legal move
│   └── MoveScore.java              # Utility to pair a move with its score
│
├── utils/
│   ├── BoardUtils.java             # General board utility functions
│   ├── DebugUtils.java             # Debug board state visualizations
│   ├── MoveGenerator.java          # Logic for generating legal chess moves
│   ├── MoveHistory.java            # Move history for undo/redo
│   └── ScoreUtils.java             # Heuristic evaluation functions
│
└── views/
    ├── ChessBoardView.java         # Renders the visual chess board
    ├── ChessGameView.java          # Sets up and manages the in-game UI
    └── MainMenuView.java           # Layout and design of the main menu

## Future Improvements

- Online Multiplayer support
- Deep Learning-based evaluation
- PGN import/export

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

Email: badoitiberiualexandru@gmail.com
[LinkedIn](www.linkedin.com/in/tiberiu-alexandru-badoi-b5b902224)
