package eu.tavoda;

import java.awt.*;
import javax.swing.*;

/**
 * Java Minesweeper Game
 */

public class Minesweeper extends JFrame {
    private static final int DEFAULT_CELL_SIZE = 30;

    private JTextField statusbar;
    MineField mineField;
    SevenSegment SA = new SevenSegment(30, 50, 3, 5, 2, Color.BLACK, Color.GREEN);
    SevenSegment SB = new SevenSegment(30, 50, 3, 5, 2, Color.BLACK, Color.GREEN);
    SevenSegment SC = new SevenSegment(30, 50, 3, 5, 2, Color.BLACK, Color.GREEN);

    public Minesweeper() {
        initUI();
    }

    private void initUI() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
        toolbar.add(SA);
        toolbar.add(SB);
        toolbar.add(SC);
        toolbar.add(getNoviceButton());
        toolbar.add(getIntermediateButton());
        toolbar.add(getExpertButton());
        toolbar.add(new JButton("Custom"));
        add(toolbar, BorderLayout.NORTH);

        statusbar = new JTextField("");
        add(statusbar, BorderLayout.SOUTH);

        mineField = new MineField(statusbar, this::setSegments, 20, 40, 80, DEFAULT_CELL_SIZE);
        add(mineField);

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(true);
        pack();
        setMinimumSize(getSize());
    }

    private void setSegments(Integer mines) {
        int hundrets = mines / 100;
        int tenths = mines % 100 / 10;
        int items = mines % 10;
        SA.setNumber(hundrets);
        SB.setNumber(tenths);
        SC.setNumber(items);
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
        novice.addActionListener(e -> newGame(20, 40, 100, DEFAULT_CELL_SIZE));
        return novice;
    }

    private void newGame(int rows, int columns, int mines, int cellSize) {
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
