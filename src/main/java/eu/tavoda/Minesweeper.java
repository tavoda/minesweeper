package eu.tavoda;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

/**
 * Java Minesweeper Game
 */

public class Minesweeper extends JFrame {
    private static final int DEFAULT_CELL_SIZE = 30;

    private JTextField statusbar;
    private Random rand = new Random();
    MineField mineField;
    Color SEGMENT_ON = Color.GREEN.brighter().brighter().brighter().brighter().brighter();
//    Color SEGMENT_OFF = Color.Green.darker().darker().darker().darker();
	Color SEGMENT_OFF = Color.DARK_GRAY;
    SevenSegment SA = new SevenSegment(30, 50, 3, 5, 2, Color.BLACK, SEGMENT_ON, SEGMENT_OFF);
    SevenSegment SB = new SevenSegment(30, 50, 3, 5, 2, Color.BLACK, SEGMENT_ON, SEGMENT_OFF);
    SevenSegment SC = new SevenSegment(30, 50, 3, 5, 2, Color.BLACK, SEGMENT_ON, SEGMENT_OFF);

    public Minesweeper() {
        initUI();
    }

    private void initUI() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
        toolbar.add(Box.createRigidArea(new Dimension(10, 10)));
        toolbar.add(getNoviceButton());
        toolbar.add(Box.createRigidArea(new Dimension(10, 10)));
        toolbar.add(getIntermediateButton());
        toolbar.add(Box.createRigidArea(new Dimension(10, 10)));
        toolbar.add(getExpertButton());
        toolbar.add(Box.createRigidArea(new Dimension(10, 10)));
        JButton createCustomGame = new JButton("Custom");
        createCustomGame.addActionListener(e -> {
            CustomGameDialog customGameDialog = new CustomGameDialog(this);
			customGameDialog.setVisible(true);
		});
        toolbar.add(createCustomGame);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(SA);
        toolbar.add(SB);
        toolbar.add(SC);
        add(toolbar, BorderLayout.NORTH);

        statusbar = new JTextField("");
        add(statusbar, BorderLayout.SOUTH);

        mineField = new MineField(statusbar, this::setSegments);
        mineField.newGame(20, 40, 80, rand.nextLong(), DEFAULT_CELL_SIZE);
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
        novice.addActionListener(e -> newGame(10, 10, 20, rand.nextLong()));
        return novice;
    }

    private Component getIntermediateButton() {
        JButton novice = new JButton("Intermediate");
        novice.addActionListener(e -> newGame(10, 20, 40, rand.nextLong()));
        return novice;
    }

    private Component getExpertButton() {
        JButton novice = new JButton("Expert");
        novice.addActionListener(e -> newGame(20, 40, 100, rand.nextLong()));
        return novice;
    }

    public void newGame(int rows, int columns, int mines, long randomStart) {
        newGame(rows, columns, mines, randomStart, DEFAULT_CELL_SIZE);
    }

    private void newGame(int rows, int columns, int mines, long randomStart, int cellSize) {
        Dimension min = new Dimension(columns * cellSize, rows * cellSize);
        mineField.newGame(rows, columns, mines, randomStart, cellSize);
        pack();
        repaint();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Minesweeper ex = new Minesweeper();
            ex.setVisible(true);
        });
    }
}
