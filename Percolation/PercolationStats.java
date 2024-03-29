/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final int numberOfTrials;
    private final double[] meanStats;
    private double meanStdDeviation = -1;
    private double mean = -1;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Trial or size value must be larger than 0");
        }
        numberOfTrials = trials;
        meanStats = new double[numberOfTrials];
        int meanIndex = 0;
        for (int i = 0; i < trials; i++) {
            int[] openedNodes = new int[n*n*2];
            for (int t = 0; t < openedNodes.length; t++) {
                openedNodes[t] = -1;
            }
            Percolation trial = new Percolation(n);
            for (int j = 0; j < openedNodes.length; j++) {
                boolean alreadyOpen = true;
                int row = -1, column = -1;
                while (alreadyOpen) {
                    row = StdRandom.uniform(n);
                    column = StdRandom.uniform(n);
                    alreadyOpen = alreadyOpen(openedNodes, row, column);
                }
                trial.open(row+1,  column+1);
                addToOpenedNodes(openedNodes, row, column);
                if (trial.percolates()) {
                    meanStats[meanIndex++] = (double) j / (double) (n * n);
                    break;
                }
            }
        }

    }

    private void addToOpenedNodes(int[] openedNodes, int row, int column) {
        boolean inserted = false;
        for (int j = 0; j < openedNodes.length && !inserted; j++) {
            if (openedNodes[j] == -1) {
                openedNodes[j] = row;
                openedNodes[j+1] = column;
                inserted = true;
            }
        }
    }

    private boolean alreadyOpen(int[] openedNodes, int row, int column) {
        for (int i = 0; i < openedNodes.length; i += 2) {
            if (row == openedNodes[i] && column == openedNodes[i+1]) {
                return true;
            }
        }
        return false;
    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean == -1) {
             mean = StdStats.mean(meanStats);
        }
        return mean;
        /* double sum = 0;
        for (int i = 0; i < meanStats.length; i++) {
            sum += meanStats[i];
        }
        return sum / numberOfTrials; */
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (meanStdDeviation == -1) {
            meanStdDeviation = StdStats.stddev(meanStats);
        }
        return meanStdDeviation;
        /* double standardDeviation = 0;
        for (int i = 0; i < meanStats.length; i++) {
            standardDeviation += Math.pow(meanStats[i] - mean(), 2);
        }
        standardDeviation /= (numberOfTrials - 1);
        return Math.sqrt(standardDeviation); */
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(numberOfTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(numberOfTrials));
    }

    // test client (see below)
    public static void main(String[] args) {
        // PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        PercolationStats stats = new PercolationStats(10, 100);
        StdOut.println("Mean: " + stats.mean());
        StdOut.println("Simple standard deviation: " + stats.stddev());
        StdOut.println("Low end confidence interval: " + stats.confidenceLo());
        StdOut.println("High end confidence interval: " + stats.confidenceHi());
    }

}