#Java Chess Engine with Minimax, Alpha-Beta Pruning and MCTS

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
├── Main.java                                   #Entry point for the application
├── AppManager.java                             #Initializes the main menu and displays it
│
├── controllers
│   └── ChessController                         #Handles the game logic
│   └── MainMenuController                      #Handles the main menu logic
├── enums
│   └── AlgorithmType                           #Stores the types of Algorithms
│   └── PlayerColor                             #Stores the two player colors
│   └── PlayerType                              #Stores the types of players
│   └── TileColor                               #Stores the board color types as well as their color hex codes
├── models
│   └── Bishop                                  #Bishop piece model
│   └── ChessBoardModel                         #Chess board model used for storing data as well as handling board cloning
│   └── King                                    #King piece model
│   └── Knight                                  #Knight piece model
│   └── Move                                    #Stores move information
│   └── Pawn                                    #Pawn piece model
│   └── Piece                                   #Base Piece class inherited by all the other piece models
│   └── PieceFactory                            #Piece Factory to create pieces
│   └── PlayerModel                             #Player model to hold type of player and algorithm if player is algorithm controlled
│   └── Queen                                   #Queen piece model
│   └── Rook                                    #Rook piece model
├── PC
│   └── Algorithm                               #Interface inherited by all the algorithm classes
│   └── AlgorithmFactory                        #Factory to create the algorithm objects
│   └── AlphaBetaMinimax                        #Alpha Beta Minimax algorithm class
│   └── MCTSNode                                #Monte Carlo Tree Search class to hold node data
│   └── Minimax                                 #Minimax algorithm class
│   └── MinimxNode                              #Minimax class to hold node data
│   └── MonteCarloTreeSearch                    #Monte Carlo Tree Search algorithm class
│   └── MoveScore                               #Holds the score of a Move
│   └── Negamax                                 #Negamax algorithm class
│   └── RandomMoveAlgorithm                     #Random move algorithm class
├── utils
│   └── BoardUtils                              #Contains functions used by multiple classes
│   └── DebugUtils                              #Contains a debug function to showcase all boards checked by a move for debugging purposes
│   └── MoveGenerator                           #Holds move generation logic
│   └── MoveHistory                             #Logging class for all Moves made, used for Redo and Undo functions as well
│   └── ScoreUtils                              #Contains board score heuristic functions
├── views
│   └── ChessBoardView                          #Contains logic for showing the Chess Board
│   └── ChessGameView                           #Contains logic for initializing the Chess game
│   └── MainMenuView                            #Contains logic for the Main Menu design

## Future Improvements

- Online Multiplayer support
- Deep Learning-based evaluation
- PGN import/export

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

Email: badoitiberiualexandru@gmail.com
[LinkedIn](www.linkedin.com/in/tiberiu-alexandru-badoi-b5b902224)