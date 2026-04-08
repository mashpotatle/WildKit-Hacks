package com.wildkits;

public class TicTacToeAI {
    public static final int EMPTY = 0;
    public static final int PLAYER = 1;
    public static final int SERVER_AI = 2;

    public static int getBestMove(int[] board) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) {
                board[i] = PLAYER;
                int score = minimax(board, 0, false);
                board[i] = EMPTY;
                if (score > bestScore) { bestScore = score; bestMove = i; }
            }
        }
        return bestMove;
    }

    private static int minimax(int[] board, int depth, boolean isMaximizing) {
        int winner = checkWinner(board);
        if (winner == PLAYER) return 10 - depth;
        if (winner == SERVER_AI) return depth - 10;
        if (isBoardFull(board)) return 0;

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == EMPTY) {
                    board[i] = PLAYER;
                    best = Math.max(best, minimax(board, depth + 1, false));
                    board[i] = EMPTY;
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == EMPTY) {
                    board[i] = SERVER_AI;
                    best = Math.min(best, minimax(board, depth + 1, true));
                    board[i] = EMPTY;
                }
            }
            return best;
        }
    }

    public static int checkWinner(int[] b) {
        int[][] lines = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
        for (int[] l : lines) {
            if (b[l[0]] != EMPTY && b[l[0]] == b[l[1]] && b[l[1]] == b[l[2]]) return b[l[0]];
        }
        return EMPTY;
    }

    public static boolean isBoardFull(int[] board) {
        for (int cell : board) if (cell == EMPTY) return false;
        return true;
    }
}
