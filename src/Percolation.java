import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf;
    private boolean[][] grid;
    private final int size;
    private final int virtualTop;
    private final int virtualBottom;
    private int count;

    // creates n-by-n grid, with all sites initially blocked
    /**
     *
     * @param n initial number to set grid.
     * @throws IllegalArgumentException {@code n < 0} Number must be greater than 0
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be > 0");
        this.grid = new boolean[n][n];
        this.uf = new WeightedQuickUnionUF((n * n) + 2);
        this.size = n;
        this.virtualTop = n * n;
        this.virtualBottom = (n * n) + 1;
        this.count = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        this.validateSite(row, col);
        if (this.isOnGrid(row, col) && !this.isOpen(row, col)) {
            int point = this.getPoint(row, col);
            this.grid[row - 1][col - 1] = true;
            this.count = this.count + 1;
            if (row == 1) {
                this.uf.union(this.virtualTop, point);
            }

            if (row == this.size) {
                this.uf.union(this.virtualBottom, point);
            }

            this.connectVertical(row, col, point);
            this.connectHorizontal(row, col, point);
        }
    }

    private void connectVertical(int row, int col, int point) {
        // connects bottom
        if (this.isOnGrid(row - 1, col) && this.isOpen(row - 1, col)) {
            this.unionize(row - 1, col, point);
        }
        // connects top
        if (this.isOnGrid(row + 1, col) && this.isOpen(row + 1, col)) {
            this.unionize(row + 1, col, point);
        }
    }

    private void unionize(int row, int col, int point) {
        if (this.isOnGrid(row, col)) {
            this.uf.union(this.getPoint(row, col), point);
        }
    }

    private int getPoint(int row, int col) {
        return ((this.size * (row - 1)) + (col - 1));
    }

    private void connectHorizontal(int row, int col, int point) {
        // connects right
        if (this.isOnGrid(row, col + 1) && this.isOpen(row, col + 1)) {
            this.unionize(row, col + 1, point);
        }
        // connects left
        if (this.isOnGrid(row, col - 1) && this.isOpen(row, col - 1)) {
            this.unionize(row, col - 1, point);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        this.validateSite(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        this.validateSite(row, col);
        if (this.isOpen(row, col)) {
            return this.uf.find(this.getPoint(row, col)) == this.uf.find(this.virtualTop);
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.count;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.uf.find(this.virtualTop) == this.uf.find(this.virtualBottom);
    }

    private void validateSite(int row, int col) {
        if (!this.isOnGrid(row, col)) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
    }

    private boolean isOnGrid(int row, int col) {
        return (row > 0 && col > 0 && row <= this.size && col <= this.size);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
