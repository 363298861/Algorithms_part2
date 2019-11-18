
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;

/******************************************************************************
 *  Author:  Huang Zhiyuan
 *  Start Date: 23th Jan, 2018
 *  End Date:
 *
 *  What do I learn after I finish:
 ******************************************************************************/


public class SeamCarver {

    private Picture p;

    public SeamCarver(Picture picture) {
        if(picture == null)
            throw new IllegalArgumentException();
        p = new Picture(picture);
    }  // create a seam carver object based on the given picture


    public Picture picture()
    {
        return p;
    }  // current picture


    public int width()
    {
        return p.width();
    }  // width of current picture


    public int height()
    {
        return p.height();
    }  // height of current picture


    public double energy(int x, int y) {
        if(x < 0 || x >= p.width() || y < 0 || y >= p.height())
            throw new IllegalArgumentException();
        if(x == 0 || x == p.width() - 1 || y == 0 || y == p.height() - 1)
            return 1000;
        else
            return Math.sqrt((double) getX(x, y) + getY(x, y));
    }  // energy of pixel at column x and row y

    private int getX(int x, int y)
    {
        int rgb1 = p.getRGB(x - 1, y);
        int rgb2 = p.getRGB(x + 1, y);
        int r = (rgb1 >> 16) & 0xFF;
        int g = (rgb1 >>  8) & 0xFF;
        int b = (rgb1 >>  0) & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >>  8) & 0xFF;
        int b2= (rgb2 >>  0) & 0xFF;
        return (r2 - r) * (r2 - r) + (g2 - g) * (g2 - g) + (b2 - b) * (b2 - b);

    }

    private int getY(int x, int y)
    {
        int rgb1 = p.getRGB(x, y + 1);
        int rgb2 = p.getRGB(x, y - 1);
        int r = (rgb1 >> 16) & 0xFF;
        int g = (rgb1 >>  8) & 0xFF;
        int b = (rgb1 >>  0) & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >>  8) & 0xFF;
        int b2= (rgb2 >>  0) & 0xFF;
        return (r2 - r) * (r2 - r) + (g2 - g) * (g2 - g) + (b2 - b) * (b2 - b);
    }


    public int[] findHorizontalSeam() {
        Picture picture = transpose(p);
        SeamCarver sc = new SeamCarver(picture);
        return sc.findVerticalSeam();
    }// sequence of indices for horizontal seam


    public int[] findVerticalSeam() {
        int x = p.height();
        int y = p.width();
        if(y == 1)
            return new int[x];
        if(x == 1)
            return new int[] {0};
        double max = 0;
        int end = 0;
        double[][] energy = new double[x][y];
        for(int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                energy[i][j] = energy(j, i);
            }
        }
        double[] dist = new double[x * y];
        for(int i = 0; i < x * y; i++){
            if(i < y)
                dist[i] = 0;
            else
                dist[i] = Double.POSITIVE_INFINITY;
        }
        int[] verx = new int[x * y];
        for(int i = y; i < y * x; i++){
            if(i % y == 0){
                if(dist[i] > dist[i - y] + energy[i / y][i % y]){
                    dist[i] = dist[i - y] + energy[i / y][i % y];
                    verx[i] = i - y;
                }
                if(dist[i] > dist[i - y + 1] + energy[i / y][i % y]){
                    dist[i] = dist[i - y + 1] + energy[i / y][i % y];
                    verx[i] = i - y + 1;
                }
            }else if(i % y == y - 1){
                if(dist[i] > dist[i - y] + energy[i / y][i % y]){
                    dist[i] = dist[i - y] + energy[i / y][i % y];
                    verx[i] = i - y;
                }
                if(dist[i] > dist[i - y - 1] + energy[i / y][i % y]){
                    dist[i] = dist[i - y - 1] + energy[i / y][i % y];
                    verx[i] = i - y - 1;
                }
            }else{
                if(dist[i] > dist[i - y] + energy[i / y][i % y]){
                    dist[i] = dist[i - y] + energy[i / y][i % y];
                    verx[i] = i - y;
                }
                if(dist[i] > dist[i - y - 1] + energy[i / y][i % y]){
                    dist[i] = dist[i - y - 1] + energy[i / y][i % y];
                    verx[i] = i - y - 1;
                }if(dist[i] > dist[i - y + 1] + energy[i / y][i % y]){
                    dist[i] = dist[i - y + 1] + energy[i / y][i % y];
                    verx[i] = i - y + 1;
                }
            }
        }
        for(int i = 0; i < dist.length; i++){
            //System.out.println("The shortest distance to vertex " + i + " is: "+ dist[i]);
            //System.out.println("The previous vertex to " + i + " is " + verx[i]);
        }

        for(int i = y * (x - 1); i < x * y; i++){
            if(max == 0 || max > dist[i]){
                max = dist[i];
                end = i;
            }
        }
        int[] verticalSeam = new int[x];
        verticalSeam[x - 1] = end % y;
        for(int i = x - 2; i > 0; i--){
            verticalSeam[i] = verx[end] % y;
            end = verx[end];
        }
        verticalSeam[0] = verticalSeam[1];
        return verticalSeam;
        /*double[][] dist = new double[y][x * y];
        int[][] edge = new int[y][x * y];
        int[] h = new int[x];
        for(int i = 0; i < y; i++){
            Queue<Integer> queue = new Queue<>();
            queue.enqueue(i);
            while(!queue.isEmpty()){
                int vertex = queue.dequeue();
                if(vertex < y * (x - 1)){
                    if(vertex % y == 0){
                        queue.enqueue(vertex + y);
                        queue.enqueue(vertex + y + 1);
                        if(dist[i][vertex + y] == 0 || dist[i][vertex + y] > dist[i][vertex] + energy[vertex / y + 1][vertex % y]){
                            dist[i][vertex + y] = dist[i][vertex] + energy[vertex / y + 1][vertex % y];
                            edge[i][vertex + y] = vertex;
                        }
                        if(dist[i][vertex + y + 1] == 0 || dist[i][vertex + y + 1] > dist[i][vertex] + energy[vertex / y + 1][vertex % y + 1]){
                            dist[i][vertex + y + 1] = dist[i][vertex] + energy[vertex / y + 1][vertex % y + 1];
                            edge[i][vertex + y + 1] = vertex;
                        }
                    }else if(vertex % y == y - 1){
                        queue.enqueue(vertex + y);
                        queue.enqueue(vertex + y - 1);
                        if(dist[i][vertex + y] == 0 || dist[i][vertex + y] > dist[i][vertex] + energy[vertex / y + 1][vertex % y]){
                            dist[i][vertex + y] = dist[i][vertex] + energy[vertex / y + 1][vertex % y];
                            edge[i][vertex + y] = vertex;
                        }
                        if(dist[i][vertex + y - 1] == 0 || dist[i][vertex + y - 1] > dist[i][vertex] + energy[vertex / y + 1][vertex % y - 1]){
                            dist[i][vertex + y - 1] = dist[i][vertex] + energy[vertex / y + 1][vertex % y - 1];
                            edge[i][vertex + y - 1] = vertex;
                        }
                    }else{
                        queue.enqueue(vertex + y);
                        queue.enqueue(vertex + y + 1);
                        queue.enqueue(vertex + y - 1);
                        if(dist[i][vertex + y] == 0 || dist[i][vertex + y] > dist[i][vertex] + energy[vertex / y + 1][vertex % y]){
                            dist[i][vertex + y] = dist[i][vertex] + energy[vertex / y + 1][vertex % y];
                            edge[i][vertex + y] = vertex;
                        }
                        if(dist[i][vertex + y - 1] == 0 || dist[i][vertex + y - 1] > dist[i][vertex] + energy[vertex / y + 1][vertex % y - 1]){
                            dist[i][vertex + y - 1] = dist[i][vertex] + energy[vertex / y + 1][vertex % y - 1];
                            edge[i][vertex + y - 1] = vertex;
                        }
                        if(dist[i][vertex + y + 1] == 0 || dist[i][vertex + y + 1] > dist[i][vertex] + energy[vertex / y + 1][vertex % y + 1]){
                            dist[i][vertex + y + 1] = dist[i][vertex] + energy[vertex / y + 1][vertex % y + 1];
                            edge[i][vertex + y + 1] = vertex;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < dist.length; i++){
            for(int j = 0; j < dist[i].length; j++) {
                //System.out.println("The start number is " + i + " and vertex is " + j + ", the distant so far is: " + dist[i][j]);
                //System.out.println("The start number is " + i + " and vertex is " + j + ", the vertex to this number is: " + edge[i][j]);
            }
        }
        //System.out.println("The sum before is: " + sum);
        for(int i = 0; i < y; i++){
            for(int j = y * (x - 1); j < x * y; j++){
                if((sum == 0 || dist[i][j] < sum) && dist[i][j] != 0) {
                    sum = dist[i][j];
                    //System.out.println("The sum is: " + sum);
                    start = i;
                    end = j;
                }
            }
        }
        //System.out.println("Then starting number is: " + start);
        //System.out.println("Then ending number is: " + end);
        h[x - 1] = end % y;
        h[0] = start;
        for(int i = x - 2; i >= 0; i--){
            h[i] = edge[start][end] % y;
            end = edge[start][end];
        }
        return h;*/
    }// sequence of indices for vertical seam


    public void removeHorizontalSeam(int[] seam) {
        if(seam == null)
            throw new IllegalArgumentException("Invalid input");
        if(seam.length != p.width())
            throw new IllegalArgumentException("The length doesn't match");
        if(p.height() < 2)
            throw new IllegalArgumentException();
        Picture newp = new Picture(p.width(), p.height() - 1);
        for(int i = 0; i < p.width(); i++){
            for(int j = 0; j < p.height() - 1; j++){
                if(j < seam[i])
                    newp.set(i, j, p.get(i, j));
                else
                    newp.set(i, j, p.get(i, j + 1));
            }
        }
        p = newp;

    }  // remove horizontal seam from current picture


    public void removeVerticalSeam(int[] seam) {
        if(seam.length > 1) {
            for (int i = 1; i < seam.length - 1; i++){
                if(Math.abs(seam[i] - seam[i - 1]) > 1)
                    throw new IllegalArgumentException();
            }
        }

        p = transpose(p);
        removeHorizontalSeam(seam);
        p = transpose(p);
    }// remove vertical seam from current picture

    private Picture transpose(Picture p){
        Picture n = new Picture(p.height(), p.width());

        for(int i = 0; i < n.width(); i++){
            for(int j = 0; j < n.height(); j++){
                n.set(i, j, p.get(j, i));
            }
        }
        return n;
    }
}
