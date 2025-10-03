package eu.tavoda;

import java.awt.*;
import javax.swing.*;

/**
 * Java Minesweeper Game
 */

public class Minesweeper extends JFrame {
    private static final int DEFAULT_CELL_SIZE = 30;

    private JTextField statusbar;
//    private int rows = 20;
//    private int cols = 40;
//    private int mines = 80;
//    private int cellSize = 30;
    MineField mineField;

    public Minesweeper() {
        initUI();
    }

    private void initUI() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
        toolbar.add(getNoviceButton());
        toolbar.add(getIntermediateButton());
        toolbar.add(getExpertButton());
        toolbar.add(new JButton("Custom"));
        toolbar.add(new SevenSegment(50, 90));
        toolbar.add(new SevenSegment(50, 90));
        toolbar.add(new SevenSegment(50, 90));
        add(toolbar, BorderLayout.NORTH);

        statusbar = new JTextField("");
        add(statusbar, BorderLayout.SOUTH);

        mineField = new MineField(statusbar, 20, 40, 80, DEFAULT_CELL_SIZE);
        add(mineField);

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(true);
        pack();
        setMinimumSize(getSize());
    }

    private Component getNoviceButton() {
        JButton novice = new JButton("Novice");
        novice.addActionListener(e -> newGame(10, 10, 20, DEFAULT_CELL_SIZE));
        return novice;
    }

    private Component getIntermediateButton() {
        JButton novice = new JButton("Intermediate");
        novice.addActionListener(e -> newGame(10, 20, 40, DEFAULT_CELL_SIZE));
        return novice;
    }

    private Component getExpertButton() {
        JButton novice = new JButton("Expert");
        novice.addActionListener(e -> newGame(20, 40, 80, DEFAULT_CELL_SIZE));
        return novice;
    }

    private void newGame(int rows, int columns, int mines, int cellSize) {
//        setSize(new Dimension(100, 100));
//        setPreferredSize(new Dimension(100, 100));
//        setMinimumSize(new Dimension(100, 100));
        Dimension min = new Dimension(columns * cellSize, rows * cellSize);
        mineField.setParameters(rows, columns, mines, cellSize);
        pack();
        repaint();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var ex = new Minesweeper();
            ex.setVisible(true);
        });
    }
}
