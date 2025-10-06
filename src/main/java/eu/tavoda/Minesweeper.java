package eu.tavoda;

import eu.tavoda.swing.SegmentDisplay;

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
    private MineField mineField;
    private SegmentDisplay mineDisplay;

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
        mineDisplay = new SegmentDisplay(3, 30, 50, 3, 5, 2);
        toolbar.add(mineDisplay);
        add(toolbar, BorderLayout.NORTH);

        statusbar = new JTextField("");
        add(statusbar, BorderLayout.SOUTH);

        mineField = new MineField(this, this::setSegments);
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
        mineDisplay.setValue(mines);
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
        novice.addActionListener(e -> newGame(20, 40, 140, rand.nextLong()));
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

    public void setStatus(String statusText, boolean showDialog) {
        statusbar.setText(statusText);
        if (showDialog) {
//            JDialog dialog = new JDialog(this, "Status", true);
//            dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
//            dialog.add(new JLabel(statusText));
//            JButton okBtn = new JButton("OK");
//            okBtn.addActionListener(e -> dialog.setVisible(false));
//            dialog.add(okBtn);
//            dialog.setLocationRelativeTo(this);
//            dialog.pack();
//            SwingUtilities.invokeLater(() -> dialog.setVisible(true));
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, statusText, "Status", JOptionPane.INFORMATION_MESSAGE));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Minesweeper ex = new Minesweeper();
            ex.setVisible(true);
        });
    }
}
