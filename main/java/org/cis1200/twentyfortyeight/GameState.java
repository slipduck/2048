package org.cis1200.twentyfortyeight;

public class GameState {
    private final int[][] board;
    private final int score;

    public GameState(int[][] board, int score) {
        this.board = new int[board.length][board.length];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                this.board[r][c] = board[r][c];
            }
        }
        this.score = score;
    }

    public int[][] getBoard() {
        int[][] returnCopy = new int[board.length][board.length];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                returnCopy[r][c] = board[r][c];
            }
        }
        return returnCopy;
    }

    public int getScore() {
        return score;
    }
}
