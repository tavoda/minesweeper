package eu.tavoda;

import java.util.List;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.*;

public class MineField extends JPanel {

    private final int NUM_IMAGES = 13;
    private final int MIN_CELL_SIZE = 15;
    private int CELL_SIZE = 30;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private int N_MINES = 40;
    private int N_ROWS = 16;
    private int N_COLS = 16;

    private int[] field;
    private boolean inGame;
    private int minesLeft;
    Consumer<Integer> minesCallback;
    private Image[] img;

    int xOffset;
    int yOffset;

    private int allCells;
    private final JTextField statusbar;

    public MineField(JTextField statusbar, Consumer<Integer> minesCallback, int rows, int columns, int mines, int cellSize) {
        initBoard();
        this.statusbar = statusbar;
        this.minesCallback = minesCallback;
        setParameters(rows, columns, mines, cellSize);
    }

    public void setParameters(int rows, int columns, int mines, int cellSize) {
        N_ROWS = rows;
        N_COLS = columns;
        N_MINES = mines;
        CELL_SIZE = cellSize;
        setPreferredSize(new Dimension(columns * cellSize, rows * cellSize));
        setSize(new Dimension(columns * cellSize, rows * cellSize));
        setMinimumSize(new Dimension(columns * cellSize, rows * cellSize));
        newGame();
    }

    private void initBoard() {
        img = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
    }

    public void newGame() {
        int cell;
        var random = new Random();
        inGame = true;
        minesLeft = N_MINES;

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {
            field[i] = COVER_FOR_CELL;
        }

        minesCallback.accept(minesLeft);

        int j = 0;
        while (j < N_MINES) {
            int position = (int) (allCells * random.nextDouble());
            if (position < allCells && field[position] != COVERED_MINE_CELL) {
                int current_col = position % N_COLS;
                field[position] = COVERED_MINE_CELL;
                j++;

                if (current_col > 0) {
                    cell = position - 1 - N_COLS;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + N_COLS - 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - N_COLS;
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position + N_COLS;
                if (cell < allCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (N_COLS - 1)) {
                    cell = position - N_COLS + 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + N_COLS + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }

        int maxEmpty = -1;
        int maxArea = -1;
        for (int i = 0; i < field.length; i++) {
            if (field[i] == COVER_FOR_CELL) {
                int foundEmpty = findEmptyCells(i);
                if (foundEmpty > maxArea) {
                    maxArea = foundEmpty;
                    maxEmpty = i;
                }
            }
        }
        for (int i = 0; i < field.length; i++) {
            if (field[i] < COVER_FOR_CELL) {
                field[i] += COVER_FOR_CELL;
            }
        }
        findEmptyCells(maxEmpty);
    }

    private int findEmptyCells(int j) {
        int current_col = j % N_COLS;
        int cell;
        int emptyCells = 0;

        if (current_col > 0) {
            cell = j - N_COLS - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        emptyCells++;
                        emptyCells += findEmptyCells(cell);
                    }
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        emptyCells++;
                        emptyCells += findEmptyCells(cell);
                    }
                }
            }

            cell = j + N_COLS - 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        emptyCells++;
                        emptyCells += findEmptyCells(cell);
                    }
                }
            }
        }

        cell = j - N_COLS;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    emptyCells++;
                    emptyCells += findEmptyCells(cell);
                }
            }
        }

        cell = j + N_COLS;
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    emptyCells++;
                    emptyCells += findEmptyCells(cell);
                }
            }
        }

        if (current_col < (N_COLS - 1)) {
            cell = j - N_COLS + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        emptyCells++;
                        emptyCells += findEmptyCells(cell);
                    }
                }
            }

            cell = j + N_COLS + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        emptyCells++;
                        emptyCells += findEmptyCells(cell);
                    }
                }
            }

            cell = j + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        emptyCells++;
                        emptyCells += findEmptyCells(cell);
                    }
                }
            }
        }

        return emptyCells;
    }

    @Override
    public void paintComponent(Graphics g) {
        Rectangle size = g.getClipBounds();
        CELL_SIZE = (int) Math.min(size.getWidth() / N_COLS, size.getHeight() / N_ROWS);
        xOffset = (size.width  - CELL_SIZE * N_COLS) / 2;
        yOffset = (size.height - CELL_SIZE * N_ROWS) / 2;

        int uncover = 0;
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                int cell = field[(i * N_COLS) + j];
                if (inGame && cell == MINE_CELL) {
                    inGame = false;
                }

                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }
                } else {
                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }
                g.drawImage(img[cell], (j * CELL_SIZE) + xOffset, (i * CELL_SIZE) + yOffset, CELL_SIZE, CELL_SIZE, this);
            }
        }

        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
    }

    private int[] getPatchCell(int cellOffset, boolean includeOrigin) {
        List<Integer> result = new ArrayList<>();
        add3(result, cellOffset - N_COLS, true);
        add3(result, cellOffset         , includeOrigin);
        add3(result, cellOffset + N_COLS, true);

        return result.stream().mapToInt(i -> i).toArray();
    }

    private void add3(List<Integer> patch, int origin, boolean includeOrigin) {
        int row = origin / N_COLS;
        for (int i = -1; i <= 1; i++) {
            if (i != 0 || includeOrigin) {
                checkPoint(patch, origin + i, row);
            }
        }
    }

    private void checkPoint(List<Integer> patch, int cell, int row) {
        if (cell >= 0 && cell < field.length && (cell / N_COLS) == row) {
            patch.add(cell);
        }
    }

    private class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX() - xOffset;
            int y = e.getY() - yOffset;

            if (x < 0 || y < 0) {
                return;
            }
            if (!inGame) {
                newGame();
                repaint();
                return;
            }

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            boolean doRepaint = false;

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {
                int cell = (cRow * N_COLS) + cCol;
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (field[cell] > MINE_CELL) {
                        doRepaint = true;
                        if (field[cell] <= COVERED_MINE_CELL) {
                            if (minesLeft > 0) {
                                field[cell] += MARK_FOR_CELL;
                                minesLeft--;
                                minesCallback.accept(minesLeft);
                            } else {
                                statusbar.setText("No marks left");
                            }
                        } else {
                            field[cell] -= MARK_FOR_CELL;
                            minesLeft++;
                            minesCallback.accept(minesLeft);
                        }
                    }
                } else {
                    if (field[cell] > COVERED_MINE_CELL) {
                        return;
                    }

                    if (field[cell] < MINE_CELL && field[cell] > 0) {
                        int[] checkCells = getPatchCell(cell, false);
                        // Count mines around
                        int mineCount = 0;
						for (int checkCell : checkCells) {
							if (field[checkCell] > COVERED_MINE_CELL) {
								mineCount++;
							}
						}
                        if (mineCount == field[cell]) {
                            // Marked mines matches number -> Uncover surround
                            doRepaint = true;
							for (int checkCell : checkCells) {
								if (field[checkCell] >= COVER_FOR_CELL && field[checkCell] < MARKED_MINE_CELL) {
									field[checkCell] -= COVER_FOR_CELL;
                                    if (field[checkCell] == EMPTY_CELL) {
                                        findEmptyCells(checkCell);
                                    }
								}
							}
                        }
                    } else if (field[cell] > MINE_CELL && field[cell] < MARKED_MINE_CELL) {
                        field[cell] -= COVER_FOR_CELL;
                        doRepaint = true;

                        if (field[cell] == MINE_CELL) {
                            inGame = false;
                        }

                        if (field[cell] == EMPTY_CELL) {
                            findEmptyCells(cell);
                        }
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
}
