    package com.LicentaBTA.app.chess.utils;

    import com.LicentaBTA.app.chess.enums.PlayerColor;
    import com.LicentaBTA.app.chess.models.ChessBoardModel;
    import com.LicentaBTA.app.chess.models.Pawn;
    import com.LicentaBTA.app.chess.models.Piece;

    import static com.LicentaBTA.app.chess.utils.BoardUtils.*;
    import static com.LicentaBTA.app.chess.utils.MoveGenerator.generateAllMoves;

    public class ScoreUtils {

        // Oglindim tabelele pentru jucatorul negru
        private static int mirror(int row) {
            return 7 - row;
        }

        static final int[][] PAWN_TABLE = {
            { 0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            { 5,  5, 10, 25, 25, 10,  5,  5},
            { 0,  0,  0, 20, 20,  0,  0,  0},
            { 5, -5,-10,  0,  0,-10, -5,  5},
            { 5, 10, 10,-20,-20, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}
        };

        static final int[][] KNIGHT_TABLE = {
                {-50,-40,-30,-30,-30,-30,-40,-50},
                {-40,-20,  0,  0,  0,  0,-20,-40},
                {-30,  0, 10, 15, 15, 10,  0,-30},
                {-30,  5, 15, 20, 20, 15,  5,-30},
                {-30,  0, 15, 20, 20, 15,  0,-30},
                {-30,  5, 10, 15, 15, 10,  5,-30},
                {-40,-20,  0,  5,  5,  0,-20,-40},
                {-50,-40,-30,-30,-30,-30,-40,-50}
        };

        static final int[][] BISHOP_TABLE = {
                {-20,-10,-10,-10,-10,-10,-10,-20},
                {-10,  5,  0,  0,  0,  0,  5,-10},
                {-10, 10, 10, 10, 10, 10, 10,-10},
                {-10,  0, 10, 10, 10, 10,  0,-10},
                {-10,  5,  5, 10, 10,  5,  5,-10},
                {-10,  0,  5, 10, 10,  5,  0,-10},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-20,-10,-10,-10,-10,-10,-10,-20}
        };

        static final int[][] ROOK_TABLE = {
                {  0,  0,  0,  0,  0,  0,  0,  0},
                {  5, 10, 10, 10, 10, 10, 10,  5},
                { -5,  0,  0,  0,  0,  0,  0, -5},
                { -5,  0,  0,  0,  0,  0,  0, -5},
                { -5,  0,  0,  0,  0,  0,  0, -5},
                { -5,  0,  0,  0,  0,  0,  0, -5},
                { -5,  0,  0,  0,  0,  0,  0, -5},
                {  0,  0,  0,  5,  5,  0,  0,  0}
        };

        static final int[][] QUEEN_TABLE = {
                {-20,-10,-10, -5, -5,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5,  5,  5,  5,  0,-10},
                { -5,  0,  5,  5,  5,  5,  0, -5},
                {  0,  0,  5,  5,  5,  5,  0, -5},
                {-10,  5,  5,  5,  5,  5,  0,-10},
                {-10,  0,  5,  0,  0,  0,  0,-10},
                {-20,-10,-10, -5, -5,-10,-10,-20}
        };

        static final int[][] KING_MID_TABLE = {
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-20,-30,-30,-40,-40,-30,-30,-20},
                {-10,-20,-20,-20,-20,-20,-20,-10},
                { 20, 20,  0,  0,  0,  0, 20, 20},
                { 20, 30, 10,  0,  0, 10, 30, 20}
        };

        static final int[][] KING_END_TABLE = {
                {-50,-40,-30,-20,-20,-30,-40,-50},
                {-30,-20,-10,  0,  0,-10,-20,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-30,  0,  0,  0,  0,-30,-30},
                {-50,-30,-30,-30,-30,-30,-30,-50}
        };

        static int getMaterialScore(ChessBoardModel board, PlayerColor color) {
            int white = board.getWhiteMaterial();
            int black = board.getBlackMaterial();
            return color == PlayerColor.WHITE ? (white - black) : (black - white);
        }

        static int getMobilityScore(ChessBoardModel board, PlayerColor color) {
            int player1moves = generateAllMoves(board, color).size();
            int player2moves = generateAllMoves(board, board.getOpponent(color)).size();
            return 10 * (player1moves - player2moves); // Weight: 10
        }

        static int getKingSafetyScore(ChessBoardModel model, PlayerColor color) {
            Piece king = model.getKing(color);
            if (king == null) return -10000;
            if (checkmate(color, model)) return -10000;
            if (isStalemate(model)) return 0;

            int row = king.getRow();
            int col = king.getCol();
            int score = 0;

            // Bonus pentru rocada
            boolean hasCastled = (color == PlayerColor.WHITE)
                    ? row == 7 && (col == 6 || col == 2)
                    : row == 0 && (col == 6 || col == 2);

            if (hasCastled) {
                score += 50;
            } else if (col >= 3 && col <= 4) {
                // Puncte in minus pentru rege atata timp cat acesta nu a facut rocada
                score -= 30;
            }

            // Verificam daca exista un scut de pioni pentru rege
            int pawnShield = 0;
            int direction = (color == PlayerColor.WHITE) ? -1 : 1;

            for (int dc = -1; dc <= 1; dc++) {
                int shieldRow = row + direction;
                int shieldCol = col + dc;
                if (isInsideBoard(shieldRow, shieldCol)) {
                    Piece p = model.getPiece(shieldRow, shieldCol);
                    if (p instanceof Pawn && p.getColor() == color) {
                        pawnShield += 1;
                    }
                }
            }

            score += pawnShield * 20;
            score -= (3 - pawnShield) * 15; // More penalty if exposed

            // Penalizare in plus daca regele este descoperit
            int openFilesPenalty = 0;
            for (int dc = -1; dc <= 1; dc++) {
                int file = col + dc;
                if (file >= 0 && file < 8) {
                    boolean hasFriendlyPawn = false;
                    for (int r = 0; r < 8; r++) {
                        Piece p = model.getPiece(r, file);
                        if (p instanceof Pawn && p.getColor() == color) {
                            hasFriendlyPawn = true;
                            break;
                        }
                    }
                    if (!hasFriendlyPawn) openFilesPenalty += 10;
                }
            }

            score -= openFilesPenalty;

            return score;
        }

        static int getPawnStructureScore(ChessBoardModel model, PlayerColor color) {
            int[] fileCounts = new int[8];

            int penalty = 0;
            int bonus = 0;

            for (Piece p : model.getAllPieces(color)) {
                if (p instanceof Pawn) {
                    int r = p.getRow();
                    int c = p.getCol();
                    fileCounts[c]++;

                    // Verificare daca pionul a fost depasit
                    boolean isPassed = true;
                    for (int dc = -1; dc <= 1; dc++) {
                        int checkFile = c + dc;
                        if (checkFile >= 0 && checkFile < 8) {
                            for (int r2 = r + (color == PlayerColor.WHITE ? 1 : -1);
                                 r2 >= 0 && r2 < 8;
                                 r2 += (color == PlayerColor.WHITE ? 1 : -1)) {
                                Piece opp = model.getPiece(r2, checkFile);
                                if (opp instanceof Pawn && opp.getColor() != color) {
                                    isPassed = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (isPassed) bonus += 20;
                }
            }

            for (int i = 0; i < 8; i++) {
                if (fileCounts[i] > 1) penalty += 10; // Penalizare pentru 2 pioni pe aceeasi coloana
                if (fileCounts[i] > 0 &&
                        (i == 0 || fileCounts[i - 1] == 0) &&
                        (i == 7 || fileCounts[i + 1] == 0)) {
                    penalty += 15; // Penalizare pentru pion izolat
                }
            }

            return bonus - penalty;
        }


        // Game phase detection: simple heuristic
        private static boolean isEndgame(ChessBoardModel board) {
            return board.getWhiteMaterial() + board.getBlackMaterial() < 1400; // ~Queens traded + few minors
        }

        // Piece-square evaluation
        static int getPieceSquareScore(ChessBoardModel board, PlayerColor color) {
            int score = 0;
            boolean endgame = isEndgame(board);

            for (Piece piece : board.getAllPieces(color)) {
                int row = piece.getRow();
                int col = piece.getCol();
                int r = (color == PlayerColor.WHITE) ? row : mirror(row);

                switch (piece.getClass().getSimpleName()) {
                    case "Pawn":
                        score += PAWN_TABLE[r][col];
                        break;
                    case "Knight":
                        score += KNIGHT_TABLE[r][col];
                        break;
                    case "Bishop":
                        score += BISHOP_TABLE[r][col];
                        break;
                    case "Rook":
                        score += ROOK_TABLE[r][col];
                        break;
                    case "Queen":
                        score += QUEEN_TABLE[r][col];
                        break;
                    case "King":
                        score += endgame ? KING_END_TABLE[r][col] : KING_MID_TABLE[r][col];
                        break;
                }
            }

            return score;
        }



    }
