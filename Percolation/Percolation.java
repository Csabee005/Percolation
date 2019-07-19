/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private WeightedQuickUnionUF[][] matrix;

    public Percolation(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size should be larger than 0. Current size is: "+ size);
        }
        this.size = size;
        matrix = new WeightedQuickUnionUF[size][size];
    }

    public boolean isOpen(int row, int column) {
        return isMatrixPositionOpen(row-1, column-1);
    }

    private boolean isMatrixPositionOpen(int row, int column) {
       return matrix[row][column];
    }

    public void open(int row, int column) {
        matrix[row-1][column-1] = true;
    }

    public boolean isFull(int row, int column) {
        return matrix[row-1][column-1];
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean percolates() {
        return false;
    }
}
