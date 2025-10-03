package eu.tavoda;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Java Minesweeper Game
 */

public class Minesweeper extends JFrame {

    private JLabel statusbar;
    private int rows = 16;
    private int cols = 16;
    private int mines = 40;
    private int cellSize = 30;

    public Minesweeper() {

        initUI();
    }

    private void initUI() {
        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);

        MineField b = new MineField(statusbar, rows, cols, mines, cellSize);
        add(b);

        setResizable(true);
        Dimension min = new Dimension(cols * cellSize, rows * cellSize);
        b.setPreferredSize(min);
        pack();
        setMinimumSize(getSize());

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new Minesweeper();
            ex.setVisible(true);
        });
    }
}
