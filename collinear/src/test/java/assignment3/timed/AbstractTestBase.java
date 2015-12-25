/* vi: set ts=4 sw=4 expandtab: */
import java.net.URL;
import java.io.File;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;


public abstract class AbstractTestBase
{
    
    protected static Point[] loadPointsFromFile(File infile)
    {
        In in = new In(infile);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        return points;
    }

}
