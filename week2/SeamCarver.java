import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private final Picture picture;

   // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

   // current picture
    public Picture picture() {
        return null;
    }

   // width of current picture
    public int width() {
        return 0;
    }

   // height of current picture
    public int height() {
        return 0;
    }

   // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (isEdge(x, y))
            return 1000;

        Color left = picture.get(x-1, y);
        Color right = picture.get(x+1, y);
        
        double xrGradient = (right.getRed() - left.getRed()) ^ 2;
        double xgGradient = (right.getGreen() - left.getGreen()) ^ 2;
        double xbGradient = (right.getBlue() - left.getBlue()) ^ 2;

        Color above = picture.get(x, y-1);
        Color below = picture.get(x, y+1);
        
        double yrGradient = (below.getRed() - above.getRed()) ^ 2;
        double ygGradient = (below.getGreen() - above.getGreen()) ^ 2;
        double ybGradient = (below.getBlue() - above.getBlue()) ^ 2;

        return Math.pow(xrGradient + xgGradient + xbGradient +
                        yrGradient + ygGradient + ybGradient,
                        0.5);
    }

    private boolean isEdge(int x, int y) {
        if (x == 0 ||
            x == width()-1 ||
            y == 0 ||
            y == height()-1)
            return true;

        return false;
    }
    
   // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return new int[]{};
    }

   // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return new int[]{};        
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
