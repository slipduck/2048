package org.cis1200.twentyfortyeight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

//setting up gameboard
@SuppressWarnings("serial")
public class GameBoard extends JPanel {
    private final TwentyFortyEight tfe;
    private final JLabel score;
    private final TreeMap<Integer,Color> colors;

    public static final Color BACKGROUND_COLOR = new Color(156,139,124);
    public static final int BORDER_PADDING = 15;
    public static final int CELL_PADDING = 15;
    public static final int CELL_SIZE = 100;
    public static final int BOARD_TILE_LENGTH = 4;
    public static final int BOARD_WIDTH = 2 * BORDER_PADDING + CELL_SIZE * BOARD_TILE_LENGTH
            + CELL_PADDING * (BOARD_TILE_LENGTH - 1);
    public static final int BOARD_HEIGHT = BOARD_WIDTH;

    public GameBoard(JLabel initScore,String fileName) {
        setBackground(BACKGROUND_COLOR);
        setFocusable(true);

        tfe = new TwentyFortyEight();
        score = initScore;
        colors = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] nums = line.split(",");
                int value = Integer.parseInt(nums[0]);
                int r = Integer.parseInt(nums[1]);
                int g = Integer.parseInt(nums[2]);
                int b = Integer.parseInt(nums[3]);
                colors.put(value, new Color(r,g,b));
            }
        } catch (IOException e) {
            System.out.println("Colors were unable to be loaded.");
        }

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    tfe.saveGameState();
                    tfe.moveUp();
                    if (tfe.isMerged()) {
                        tfe.addNewTile();
                    }
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tfe.saveGameState();
                    tfe.moveDown();
                    if (tfe.isMerged()) {
                        tfe.addNewTile();
                    }
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    tfe.saveGameState();
                    tfe.moveLeft();
                    if (tfe.isMerged()) {
                        tfe.addNewTile();
                    }
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    tfe.saveGameState();
                    tfe.moveRight();
                    if (tfe.isMerged()) {
                        tfe.addNewTile();
                    }
                    repaint();
                }
            }
        });
    }

    public void addInstructions() {
        JOptionPane.showMessageDialog(this,
                "This is a game of 2048. Use the arrow keys to shift tiles. " +
                        "\nThe game ends when you create a 2048 tile or when the grid " +
                        "\nis filled with no possible moves left. Restarting the game \nis " +
                        "possible with the reset button. If you want to undo a move, \nhit the " +
                        "undo button. If desired, you can save your game \nstate into a file " +
                        "and load it back later using the save/load buttons."
                        ,
                "Instructions",
                JOptionPane.INFORMATION_MESSAGE);
        requestFocusInWindow();
    }

    public void reset() {
        tfe.reset();
        repaint();
        requestFocusInWindow();
    }

    public void undo() {
        tfe.undoMove();
        repaint();
        requestFocusInWindow();
    }

    //file format looks like this
    //score
    //row1
    //row2
    //row3
    //row4
    //game over

    public void saveGame(File f) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(f))) {
            writer.println(tfe.getScore());

            for (int r = 0; r < BOARD_TILE_LENGTH; r++) {
                for (int c = 0; c < BOARD_TILE_LENGTH; c++) {
                    writer.print(tfe.getCell(r,c));
                    if (c < BOARD_TILE_LENGTH - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }

            writer.println(tfe.isGameOver());

            JOptionPane.showMessageDialog(this,"Game saved to " + f.getName(),
                    "Save complete!",JOptionPane.INFORMATION_MESSAGE);
            requestFocusInWindow();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving game: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            requestFocusInWindow();
        }
    }

    public void loadGame(File f) {
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String scoreLine = br.readLine();
            if (scoreLine == null) {
                throw new IOException();
            }

            int score = Integer.parseInt(scoreLine);

            int[][] newBoard = new int[BOARD_TILE_LENGTH][BOARD_TILE_LENGTH];
            for (int r = 0; r < BOARD_TILE_LENGTH; r++) {
                String[] row = br.readLine().split(",");
                for (int c = 0; c < BOARD_TILE_LENGTH; c++) {
                    newBoard[r][c] = Integer.parseInt(row[c]);
                }
            }

            boolean gameOver = br.readLine().equals("true");

            tfe.loadFromFile(score,newBoard,gameOver);
            repaint();

            JOptionPane.showMessageDialog(this,"Game loaded",
                    "Game Loaded!",JOptionPane.INFORMATION_MESSAGE);
            requestFocusInWindow();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Error loaded game",
                    "Error",JOptionPane.ERROR_MESSAGE);
            requestFocusInWindow();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (tfe.isGameOver()) {
            score.setText("Game Over!");
        } else {
            score.setText("Score: " + tfe.getScore());
        }

        for (int r = 0; r < BOARD_TILE_LENGTH; r++) {
            for (int c = 0; c < BOARD_TILE_LENGTH; c++) {
                int xPos = BORDER_PADDING + c * (CELL_SIZE + CELL_PADDING);
                int yPos = BORDER_PADDING + r * (CELL_SIZE + CELL_PADDING);
                int cellValue = tfe.getCell(r,c);

                g.setColor(getTileColor(cellValue));
                g.fillRoundRect(xPos,yPos,CELL_SIZE,CELL_SIZE ,12,12);

                if (cellValue != 0) {
                    g.setFont(fetchFont(cellValue));
                    g.setColor(getTextColor(cellValue));

                    String v = String.valueOf(cellValue);
                    int w = g.getFontMetrics().stringWidth(v);
                    int h = g.getFontMetrics().getHeight();

                    g.setColor(getTextColor(cellValue));
                    g.drawString(v,xPos + CELL_SIZE / 2 - w / 2,yPos + CELL_SIZE / 2 + h / 2 -
                            g.getFontMetrics().getDescent());
                }
            }
        }
    }

    private Font fetchFont(int value) {
        int fontSize;
        if (value >= 1000) {
            fontSize = 32;
        } else if (value >= 100) {
            fontSize = 34;
        } else {
            fontSize = 36;
        }
        return new Font(Font.SANS_SERIF,Font.BOLD,fontSize);
    }

    public Color getTileColor(int value) {
        return colors.get(value);
        /*
        if (value == 0){
            return new Color(202,192,180);
        } else if (value == 2){
            return new Color(236,228,219);
        } else if (value == 4){
            return new Color(235,224,203);
        } else if (value == 8) {
            return new Color(232,180,129);
        } else if (value == 16) {
            return new Color(232,154,108);
        } else if (value == 32) {
            return new Color(230,131,103);
        } else if (value == 64) {
            return new Color(228,104,71);
        } else if (value == 128) {
            return new Color(232,208,128);
        } else if (value == 256) {
            return new Color(232,205,114);
        } else if (value == 512) {
            return new Color(231,201,101);
        } else if (value == 1024) {
            return new Color(231,199,89);
        } else if (value == 2048) {
            return new Color(230,196,79);
        } else {
            throw new IllegalArgumentException();
        }*/
    }

    public Color getTextColor(int value) {
        if (value == 0) {
            //for testing
            return Color.BLACK;
        } else if (value == 2 || value == 4) {
            return new Color(130,120,108);
        } else {
            return new Color(249,246,242);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
