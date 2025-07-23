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
├── Main.java                            # Entry point for the application
├── AppManager.java                      # Initializes the main menu and displays it

├── controllers/
│   ├── ChessController.java             # Handles the game logic
│   └── MainMenuController.java          # Handles the main menu logic

├── enums/
│   ├── AlgorithmType.java               # Stores the types of algorithms
│   ├── PlayerColor.java                 # Stores the two player colors
│   ├── PlayerType.java                  # Stores the types of players
│   └── TileColor.java                   # Board color types and their hex codes

├── models/
│   ├── Bishop.java                      # Bishop piece model
│   ├── ChessBoardModel.java             # Board model with cloning and data handling
│   ├── King.java                        # King piece model
│   ├── Knight.java                      # Knight piece model
│   ├── Move.java                        # Stores move information
│   ├── Pawn.java                        # Pawn piece model
│   ├── Piece.java                       # Base class for all chess pieces
│   ├── PieceFactory.java                # Factory class for creating pieces
│   ├── PlayerModel.java                 # Player model (type + algorithm)
│   ├── Queen.java                       # Queen piece model
│   └── Rook.java                        # Rook piece model

├── PC/
│   ├── Algorithm.java                   # Interface for all AI algorithms
│   ├── AlgorithmFactory.java            # Factory to create algorithm instances
│   ├── AlphaBetaMinimax.java            # Alpha-Beta Pruning algorithm
│   ├── MCTSNode.java                    # Node class for MCTS
│   ├── Minimax.java                     # Minimax algorithm
│   ├── MinimaxNode.java                 # Node structure used by Minimax
│   ├── MonteCarloTreeSearch.java        # Monte Carlo Tree Search algorithm
│   ├── MoveScore.java                   # Holds move and score pair
│   ├── Negamax.java                     # Negamax algorithm
│   └── RandomMoveAlgorithm.java         # Basic random move AI

├── utils/
│   ├── BoardUtils.java                  # Common board operations
│   ├── DebugUtils.java                  # Debug tools for board inspection
│   ├── MoveGenerator.java               # Move generation logic
│   ├── MoveHistory.java                 # Redo/undo move history tracker
│   └── ScoreUtils.java                  # Heuristic scoring utilities

├── views/
│   ├── ChessBoardView.java              # Renders the chess board
│   ├── ChessGameView.java               # Manages chess game GUI
│   └── MainMenuView.java                # GUI for the main menu

## Future Improvements

- Online Multiplayer support
- Deep Learning-based evaluation
- PGN import/export

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

Email: badoitiberiualexandru@gmail.com
[LinkedIn](www.linkedin.com/in/tiberiu-alexandru-badoi-b5b902224)
