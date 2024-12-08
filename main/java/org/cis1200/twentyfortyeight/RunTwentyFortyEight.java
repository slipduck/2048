package org.cis1200.twentyfortyeight;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

//layout stuff goes here

public class RunTwentyFortyEight implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("2048");
        frame.setLocation(300, 300);

        JLabel score = new JLabel("Score: 0");
        final JPanel top_panel = new JPanel();
        frame.add(top_panel, BorderLayout.NORTH);
        top_panel.add(score);

        // Game board
        final GameBoard board = new GameBoard(score,"files/color_config_normal.txt");
        frame.add(board, BorderLayout.CENTER);

        final JPanel bottom_panel = new JPanel();
        frame.add(bottom_panel, BorderLayout.SOUTH);

        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> {
            board.addInstructions();
        });
        bottom_panel.add(instructions);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        bottom_panel.add(reset);

        //undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        bottom_panel.add(undo);

        //save button
        final JButton save = new JButton("Save");
        save.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Game");
            chooser.setFileFilter(new FileNameExtensionFilter("2048 Save File (.2048)", "2048"));
            int result = chooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (!f.getName().endsWith(".2048")) {
                    f = new File(f.getAbsolutePath() + ".2048");
                }
                board.saveGame(f);
            } else if (result == JFileChooser.CANCEL_OPTION) {
                board.requestFocusInWindow();
            }
        });
        bottom_panel.add(save);

        //load button
        final JButton load = new JButton("Load");
        load.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Load Game");
            chooser.setFileFilter(new FileNameExtensionFilter("2048 Load File (.2048)", "2048"));
            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                board.loadGame(f);
            } else if (result == JFileChooser.CANCEL_OPTION) {
                board.requestFocusInWindow();
            }
        });
        bottom_panel.add(load);

        // render game
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}