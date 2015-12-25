package assignment5;
/******************************************************************************
 *  Compilation:  javac assignment5.KdTreeGenerator.java
 *  Execution:    java assignment5.KdTreeGenerator N
 *  Dependencies: 
 *
 *  Creates N random points in the unit square and print to standard output.
 *
 *  % java assignment5.KdTreeGenerator 5
 *  0.195080 0.938777
 *  0.351415 0.017802
 *  0.556719 0.841373
 *  0.183384 0.636701
 *  0.649952 0.237188
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class KdTreeGenerator {

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        for (int i = 0; i < N; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            StdOut.printf("%8.6f %8.6f\n", x, y);
        }
    }
    
    public static Point2D RandomPoint()
    {
	    return new Point2D(
			    StdRandom.uniform(0.0,1.0),
			    StdRandom.uniform(0.0,1.0));
    }

    public static List<Point2D> ListOfRandomPoints(final int N)
    {
	    List<Point2D> ret = new ArrayList<>(N);
	    for (int i = 0; i < N; i++) 
		    ret.add(RandomPoint());
	    return ret;
    }
}
