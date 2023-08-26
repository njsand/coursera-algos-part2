// Part of https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php

import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private final Picture picture;

    private final double[][] energies;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;

        energies = new double[height()][width()];
        
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energies[i][j] = energy(j, i);
            }
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (isEdge(x, y))
            return 1000;

        Color left = picture.get(x-1, y);
        Color right = picture.get(x+1, y);
        
        double xrDiff = right.getRed() - left.getRed();
        double xgDiff = right.getGreen() - left.getGreen();
        double xbDiff = right.getBlue() - left.getBlue();

        Color above = picture.get(x, y-1);
        Color below = picture.get(x, y+1);
        
        double yrDiff = below.getRed() - above.getRed();
        double ygDiff = below.getGreen() - above.getGreen();
        double ybDiff = below.getBlue() - above.getBlue();

        return Math.sqrt(xrDiff * xrDiff +
                         xgDiff * xgDiff +
                         xbDiff * xbDiff +
                         yrDiff * yrDiff +
                         ygDiff * ygDiff +
                         ybDiff * ybDiff);
    }

    private boolean isEdge(int col, int row) {
        if (col == 0 || col == width()-1 ||
            row == 0 || row == height()-1)
            return true;

        return false;
    }
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // TODO
        
        return new int[]{};
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int edgeTo[][] = new int[height()][width()];
        double distTo[] = new double[width()];
        double prevDistTo[] = new double[width()];

        // Init prevDistTo to the first row's energies.
        for (int i = 0; i < width(); i++)
            prevDistTo[i] = energies[0][i];

        // Starting from the second row, for each pixel, choose the above
        // (connected) pixel with the shortest path.
        for (int i = 1; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                int prev = choosePath(prevDistTo, j);

                distTo[j] = prevDistTo[prev] + energies[i][j];
                edgeTo[i][j] = prev;
            }

            // Swap 'em.
            double[] tmp = distTo;
            distTo = prevDistTo;
            prevDistTo = tmp;
            
        }

        // Find the pixel at the end of the seam (the one with the lowest
        // overall path.)
        int pos = findMin(distTo);

        int[] path = new int[height()];

        path[path.length-1] = pos;

        // Now construct the rest of the path, starting from the row second from bottom.
        for (int i = height()-2; i >=0; i--) {
            path[i] = pos = edgeTo[i+1][pos];
        }
        
        return path;
    }

    // Return the index of the smallest value in a.
    private int findMin(double []a) {
        double min = a[0];
        int minPos = 0;
        
        for (int i = 1; i < a.length; i++)
            if (a[i] < min) {
                min = a[i];
                minPos = i;
            }

        return minPos;
    }

    private int choosePath(double[] prevDistTo, int j) {
        int left = j == 0 ? j : j-1;
        int middle = j;
        int right = j == width()-1 ? j : j+1;

        if (prevDistTo[left] < prevDistTo[middle])
            return prevDistTo[left] < prevDistTo[right] ? left : right;
        else
            return prevDistTo[middle] < prevDistTo[right] ? middle : right;
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        
    }

}
