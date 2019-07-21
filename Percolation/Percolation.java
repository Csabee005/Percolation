/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int size;
    private final WeightedQuickUnionUF matrix;
    private final int[][] matrixValues;
    private final boolean[][] matrixOpenings;
    private int openedNodes = 0;

    public Percolation(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size should be larger than 0. Current size is: "+ size);
        }
        this.size = size;
        matrix = new WeightedQuickUnionUF(size*size);
        matrixValues = new int[size][size];
        matrixOpenings = new boolean[size][size];
        int counter = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrixValues[i][j] = counter++;
            }
        }
    }

    public boolean isOpen(int row, int column) {
        return isMatrixPositionOpen(row-1, column-1);
    }

    private boolean isMatrixPositionOpen(int row, int column) {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row or column value must not be negative! Row: " + row + " column: " + column);
        }
        if (row >= size || column >= size) {
            throw new IllegalArgumentException("Row or column value is too big! Row: " + row + " column: " + column);
        }
        return matrixOpenings[row][column];
    }

    public void open(int row, int column) {
        row -= 1;
        column -= 1;
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row or column value must not be negative! Row: " + row + " column: " + column);
        }
        if (row >= size || column >= size) {
            throw new IllegalArgumentException("Row or column value is too big! Row: " + row + " column: " + column);
        }
        if (!matrixOpenings[row][column]) {
            openedNodes += 1;
            matrixOpenings[row][column] = true;
            if (row + 1 < size && matrixOpenings[row + 1][column]) {
                matrix.union(matrixValues[row][column], matrixValues[row + 1][column]);
            }
            if (row - 1 >= 0 && matrixOpenings[row - 1][column]) {
                matrix.union(matrixValues[row][column], matrixValues[row - 1][column]);
            }
            if (column + 1 < size && matrixOpenings[row][column + 1]) {
                matrix.union(matrixValues[row][column], matrixValues[row][column + 1]);
            }
            if (column - 1 >= 0 && matrixOpenings[row][column - 1]) {
                matrix.union(matrixValues[row][column], matrixValues[row][column - 1]);
            }
        }
    }

    public boolean isFull(int row, int column) {
        int columnNumber = 0;
        row -= 1;
        column -= 1;
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row or column value must not be negative! Row: " + row + " column: " + column);
        }
        if (row >= size || column >= size) {
            throw new IllegalArgumentException("Row or column value is too big! Row: " + row + " column: " + column);
        }
        if (row == 0 && matrixOpenings[row][column]) {
            return true;
        }
        while (columnNumber < size) {
            if (matrixOpenings[row][column] && matrixOpenings[0][columnNumber] && matrix.connected(matrixValues[row][column], matrixValues[0][columnNumber])) {
                return true;
            }
            columnNumber += 1;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedNodes;
    }

    public boolean percolates() {
        int[] bottomConnections = new int[size];
        int[] topConnections = new int[size];
        for (int i = 0; i < bottomConnections.length; i++) {
            bottomConnections[i] =  matrix.find((size * size) - bottomConnections.length + i);
            topConnections[i] = matrix.find(i);
            if (bottomConnections[i] == topConnections[i] && numberOfOpenSites() > 0) {
                return true;
            }
        }
        for (int i = 0; i < bottomConnections.length; i++) {
            for (int j = 0; j < bottomConnections.length; j++) {
                if (bottomConnections[i] == topConnections[j] && numberOfOpenSites() > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
