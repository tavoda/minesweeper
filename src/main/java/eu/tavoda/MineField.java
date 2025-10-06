package eu.tavoda;

import java.io.IOException;
import java.io.InputStream;
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
    private int cellSize = 30;

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

    private int numMines;
    private int numRows;
    private int numCols;
    private long seed;

    private int[] field;
    private boolean inGame;
    private int minesLeft;
    Consumer<Integer> minesCallback;
    private Image[] img;

    int xOffset;
    int yOffset;

    private int allCells;
    private final Minesweeper mainWindow;

    public MineField(Minesweeper mainWindow, Consumer<Integer> minesCallback) {
        initBoard();
        this.mainWindow = mainWindow;
        this.minesCallback = minesCallback;
    }

    public void newGame(int rows, int columns, int mines, long randomStart, int cellSize) {
        numRows = rows;
        numCols = columns;
        numMines = mines;
        this.cellSize = cellSize;
        seed = randomStart;
        setPreferredSize(new Dimension(columns * cellSize, rows * cellSize));
        setSize(new Dimension(columns * cellSize, rows * cellSize));
        setMinimumSize(new Dimension(columns * cellSize, rows * cellSize));
        mainWindow.setStatus("", false);
        newGame();
    }

    private void initBoard() {
        img = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = i + ".png";
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(path)) {
				if (is != null) {
                    byte[] imageBytes = is.readAllBytes();
                    img[i] = (new ImageIcon(imageBytes)).getImage();
                } else {
                    throw new RuntimeException("Can't read image: " + path);
                }
            } catch (IOException e) {
				throw new RuntimeException(e);
			}
        }

        addMouseListener(new MinesAdapter());
    }

    private void newGame() {
        int cell;
        var random = new Random(seed);
        inGame = true;
        minesLeft = numMines;

        allCells = numRows * numCols;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {
            field[i] = COVER_FOR_CELL;
        }

        minesCallback.accept(minesLeft);

        int j = 0;
        while (j < numMines) {
            int position = (int) (allCells * random.nextDouble());
            if (position < allCells && field[position] != COVERED_MINE_CELL) {
                int current_col = position % numCols;
                field[position] = COVERED_MINE_CELL;
                j++;

                if (current_col > 0) {
                    cell = position - 1 - numCols;
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

                    cell = position + numCols - 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - numCols;
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position + numCols;
                if (cell < allCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (numCols - 1)) {
                    cell = position - numCols + 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + numCols + 1;
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
        int current_col = j % numCols;
        int cell;
        int emptyCells = 0;

        if (current_col > 0) {
            cell = j - numCols - 1;
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

            cell = j + numCols - 1;
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

        cell = j - numCols;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    emptyCells++;
                    emptyCells += findEmptyCells(cell);
                }
            }
        }

        cell = j + numCols;
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    emptyCells++;
                    emptyCells += findEmptyCells(cell);
                }
            }
        }

        if (current_col < (numCols - 1)) {
            cell = j - numCols + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        emptyCells++;
                        emptyCells += findEmptyCells(cell);
                    }
                }
            }

            cell = j + numCols + 1;
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
        cellSize = (int) Math.min(size.getWidth() / numCols, size.getHeight() / numRows);
        xOffset = (size.width  - cellSize * numCols) / 2;
        yOffset = (size.height - cellSize * numRows) / 2;

        int uncover = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int cell = field[(i * numCols) + j];
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
                g.drawImage(img[cell], (j * cellSize) + xOffset, (i * cellSize) + yOffset, cellSize, cellSize, this);
            }
        }

        if (uncover == 0 && inGame) {
            inGame = false;
            mainWindow.setStatus("Game won", true);
        } else if (!inGame) {
            mainWindow.setStatus("Game lost", true);
        }
    }

    private int[] getPatchCell(int cellOffset, boolean includeOrigin) {
        List<Integer> result = new ArrayList<>();
        add3(result, cellOffset - numCols, true);
        add3(result, cellOffset         , includeOrigin);
        add3(result, cellOffset + numCols, true);

        return result.stream().mapToInt(i -> i).toArray();
    }

    private void add3(List<Integer> patch, int origin, boolean includeOrigin) {
        int row = origin / numCols;
        for (int i = -1; i <= 1; i++) {
            if (i != 0 || includeOrigin) {
                checkPoint(patch, origin + i, row);
            }
        }
    }

    private void checkPoint(List<Integer> patch, int cell, int row) {
        if (cell >= 0 && cell < field.length && (cell / numCols) == row) {
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
                return;
            }

            int cCol = x / cellSize;
            int cRow = y / cellSize;

            boolean doRepaint = false;

            if ((x < numCols * cellSize) && (y < numRows * cellSize)) {
                int cell = (cRow * numCols) + cCol;
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (field[cell] > MINE_CELL) {
                        doRepaint = true;
                        if (field[cell] <= COVERED_MINE_CELL) {
                            if (minesLeft > 0) {
                                field[cell] += MARK_FOR_CELL;
                                minesLeft--;
                                minesCallback.accept(minesLeft);
                            } else {
                                mainWindow.setStatus("No marks left", false);
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
