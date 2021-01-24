import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trials;
    private final double mean;
    private final double stdev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        this.trials = trials;
        double[] threshholds = new double[trials];
        int itr = 0;

        while (itr < trials) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }

            threshholds[itr] = perc.numberOfOpenSites() / Math.pow(n, 2);
            itr++;
        }

        this.mean = StdStats.mean(threshholds);
        this.stdev = StdStats.stddev(threshholds);
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stdev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean - this.getConfidenceInt();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean + this.getConfidenceInt();
    }

    private double getConfidenceInt() {
        // according to 95% deviation calculation
        double zVal = 1.960;

        return zVal * (this.stdev / Math.sqrt(this.trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int grid = 10;
        int trials = 50;
        if (args.length >= 2) {
            grid = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }
        PercolationStats stats = new PercolationStats(grid, trials);
        System.out.println("mean " + stats.mean());
        System.out.println("deviation " + stats.stddev());
        System.out.println("low interval " + stats.confidenceLo());
        System.out.println("High interval " + stats.confidenceHi());
        System.out.println("high endpoint " + stats.mean());
    }
}
