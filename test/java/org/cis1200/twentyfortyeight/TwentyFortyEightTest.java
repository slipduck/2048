package org.cis1200.twentyfortyeight;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TwentyFortyEightTest {
    TwentyFortyEight t;

    @BeforeEach
    public void setUp() {
        t = new TwentyFortyEight();
    }

    @Test
    public void testMergeLeftNothing() {
        int[][] testBoard = new int[4][4];
        int[] testRow = new int[]{0,0,0,0};
        testBoard[0] = testRow;

        t.setBoard(testBoard);
        t.mergeLeft(0);

        int[] expected = new int[]{0,0,0,0};

        assertTrue(Arrays.equals(expected,t.getBoard()[0]));
    }

    @Test
    public void testMergeLeftSingle() {
        int[][] testBoard = new int[4][4];
        int[] testRow = new int[]{2,0,0,0};
        testBoard[0] = testRow;

        t.setBoard(testBoard);
        t.mergeLeft(0);

        int[] expected = new int[]{2,0,0,0};

        assertTrue(Arrays.equals(expected,t.getBoard()[0]));
    }

    @Test
    public void testMergeLeftSingleShift() {
        int[][] testBoard = new int[4][4];
        int[] testRow = new int[]{0,0,2,0};
        testBoard[0] = testRow;

        t.setBoard(testBoard);
        t.mergeLeft(0);

        int[] expected = new int[]{2,0,0,0};

        assertTrue(Arrays.equals(expected,t.getBoard()[0]));
    }

    @Test
    public void testMergeLeftDouble() {
        int[][] testBoard = new int[4][4];
        int[] testRow = new int[]{2,0,0,2};
        testBoard[0] = testRow;

        t.setBoard(testBoard);
        t.mergeLeft(0);

        int[] expected = new int[]{4,0,0,0};

        assertTrue(Arrays.equals(expected,t.getBoard()[0]));
    }

    @Test
    public void testMergeLeftDoubleDouble() {
        int[][] testBoard = new int[4][4];
        int[] testRow = new int[]{2,2,2,2};
        testBoard[0] = testRow;

        t.setBoard(testBoard);
        t.mergeLeft(0);

        int[] expected = new int[]{4,4,0,0};

        assertTrue(Arrays.equals(expected,t.getBoard()[0]));
    }

    @Test
    public void testRotate() {
        int[][] testBoard = new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,16}};

        t.setBoard(testBoard);
        t.rotateBoard(1);

        int[][] expected = new int[][]{
                {13,9,5,1},
                {14,10,6,2},
                {15,11,7,3},
                {16,12,8,4}};

        assertTrue(Arrays.deepEquals(expected,t.getBoard()));
    }

    @Test
    public void testLoadFromFile() {
        int[][] testBoard = new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,16}};

        t.loadFromFile(50,testBoard,false);
        assertEquals(50,t.getScore());
        assertTrue(Arrays.deepEquals(testBoard,t.getBoard()));
        assertFalse(t.isGameOver());
    }

    @Test
    public void testCheckGameOverBy2048() {
        int[][] testBoard = new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,2048}};

        t.setBoard(testBoard);
        t.checkGameOver();
        assertTrue(t.isGameOver());
    }

    @Test
    public void testCheckGameOverByLoss() {
        int[][] testBoard = new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,16}};

        t.setBoard(testBoard);
        t.checkGameOver();
        assertTrue(t.isGameOver());
    }

    @Test
    public void testCheckGameNotOver() {
        int[][] testBoard = new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,0}};

        t.setBoard(testBoard);
        t.checkGameOver();
        assertFalse(t.isGameOver());
    }
}
