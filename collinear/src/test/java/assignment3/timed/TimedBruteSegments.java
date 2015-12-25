/* vi: set ts=4 sw=4 expandtab: */
import java.net.URL;
import java.io.File;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Opens up file at URL location folder/file.txt
 * Computes several trials of Collinear of increasing size
 * and reports results.
 */
public class TimedBruteSegments
{

	public static double timedTest(Point[] points)
    {
        BruteCollinearPoints collinear;
        LineSegment[] segments;

        Stopwatch timer = new Stopwatch();

        collinear = new BruteCollinearPoints(points);
        segments = collinear.segments();

        return timer.elapsedTime();
	}
}
