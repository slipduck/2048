package org.cis1200.twentyfortyeight;

import java.util.*;
import java.awt.*;

//game logic goes here

public class TwentyFortyEight {
    private int[][] board;
    private int score;
    private final Random random;
    private boolean merged;
    private boolean gameOver;
    private LinkedList<GameState> history;

    public TwentyFortyEight() {
        random = new Random();
        merged = false;
        gameOver = false;
        history = new LinkedList<>();
        score = 0;
        reset();
    }

    public void addNewTile() {
        ArrayList<Point> emptyList = new ArrayList<>();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                if (board[r][c] == 0) {
                    emptyList.add(new Point(r, c));
                }
            }
        }

        if (emptyList.isEmpty()) {
            return;
        }

        //determine whether 2 or 4
        int val = random.nextInt(10);
        if (val < 9) {
            val = 2;
        } else {
            val = 4;
        }

        int randomIndex = random.nextInt(emptyList.size());
        Point selection = emptyList.get(randomIndex);

        board[selection.x][selection.y] = val;
        checkGameOver();
    }

    public void moveUp() {
        if (!isGameOver()) {
            rotateBoard(3);
            moveLeft();
            rotateBoard(1);
        }
    }

    public void moveDown() {
        if (!isGameOver()) {
            rotateBoard(1);
            moveLeft();
            rotateBoard(3);
        }
    }

    public void moveLeft() {
        if (!isGameOver()) {
            merged = false;
            for (int r = 0; r < board.length; r++) {
                mergeLeft(r);
            }
        }
    }

    public void moveRight() {
        if (!isGameOver()) {
            rotateBoard(2);
            moveLeft();
            rotateBoard(2);
        }
    }

    public void mergeLeft(int r) {
        //check for nonzero nums
        int[] nonZeroNums = new int[board.length];
        int index = 0;
        for (int c = 0; c < board.length; c++) {
            if (board[r][c] != 0) {
                nonZeroNums[index] = board[r][c];
                index++;
            }
        }

        for (int i = 0; i < nonZeroNums.length - 1; i++) {
            if (nonZeroNums[i] == nonZeroNums[i + 1] && nonZeroNums[i] != 0) {
                nonZeroNums[i] *= 2;
                score += nonZeroNums[i];
                nonZeroNums[i + 1] = -1;
            }
        }

        int[] finalArray = new int[board.length];
        index = 0;
        for (int i = 0; i < finalArray.length; i++) {
            if (nonZeroNums[i] != -1) {
                finalArray[index] = nonZeroNums[i];
                index++;
            }
        }

        for (int c = 0; c < finalArray.length; c++) {
            if (board[r][c] != finalArray[c]) {
                merged = true;
            }
        }

        board[r] = finalArray;
    }

    public void rotateBoard(int times) {
        for (int i = 0; i < times; i++) {
            int[][] rotated = new int[board.length][board.length];
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board.length; c++) {
                    rotated[r][c] = board[board.length - 1 - c][r];
                }
            }
            board = rotated;
        }
    }

    public int getScore() {
        return score;
    }

    //for testing
    public int[][] getBoard() {
        int[][] copy = new int[board.length][board.length];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                copy[r][c] = board[r][c];
            }
        }

        return copy;
    }

    //for testing
    public void setBoard(int[][] b) {
        board = new int[board.length][board.length];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                board[r][c] = b[r][c];
            }
        }
    }

    public void undoMove() {
        if (history.size() != 1) {
            GameState state = history.removeLast();
            score = state.getScore();
            board = state.getBoard();
        }
    }

    public void saveGameState() {
        GameState gs = new GameState(board, score);
        history.add(gs);
    }

    public void loadFromFile(int ns, int[][] nb, boolean go) {
        score = ns;
        board = nb;
        gameOver = go;
        merged = false;
        history.clear();
        saveGameState();
    }

    public boolean isMerged() {
        return merged;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGridFull() {
        boolean isFull = true;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                if (board[r][c] == 0) {
                    return false;
                }
            }
        }
        return isFull;
    }

    public boolean is2048Present() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                if (board[r][c] == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    public void reset() {
        board = new int[GameBoard.BOARD_TILE_LENGTH][GameBoard.BOARD_TILE_LENGTH];
        score = 0;
        gameOver = false;
        addNewTile();
        saveGameState();
    }

    public void checkGameOver() {
        if (is2048Present()) {
            gameOver = true;
            return;
        }

        if (!isGridFull()) {
            gameOver = false;
            return;
        }

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                if (c + 1 < board.length && board[r][c] == board[r][c + 1]) {
                    return;
                }
                if (r + 1 < board.length && board[r][c] == board[r + 1][c]) {
                    return;
                }
            }
        }
        gameOver = true;
    }

    public int getCell(int r, int c) {
        return board[r][c];
    }
}
