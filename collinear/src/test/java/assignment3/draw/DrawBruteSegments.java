/* vi: set ts=4 sw=4 expandtab: */
import java.net.URL;
import java.io.File;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Opens up file at URL location folder/file.txt
 * Computes BruteCollinear, and draws output generated from StdDraw to folder/file.gen.txt
 * and logs output and performance statistics to folder/file.gen.png
 */
public class DrawBruteSegments
{
	public static void testFile(URL file)
    {
        File infile = new File(file.getFile());
        String folder = file.getPath();
        folder = folder.substring(0,folder.lastIndexOf('/')+1);
//        StdOut.println("folder: " + folder);

        String filename = infile.getName();
        String filename_root = filename.substring(0,filename.lastIndexOf('.'));
        
        String image_filename = filename_root + ".gen.brute.png";
//        StdOut.println("image_filename: " + image_filename);

        String segments_filename = filename_root + ".gen.brute.txt";
//        StdOut.println("segments_filename: " + segments_filename);

        In in = new In(infile);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        StdDraw.setPenRadius(0.001);
        StdDraw.setPenColor(StdDraw.RED);
         // print and draw the line segments
    
        Stopwatch timer = new Stopwatch();
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        LineSegment[] segments = collinear.segments();
        double time = timer.elapsedTime();

        Out out = new Out(folder + segments_filename);
        for (LineSegment segment : segments) {
            out.println(segment);
            segment.draw();
        }
        out.println("points: " + points.length);
        out.println("segments: " + segments.length);
        out.println("elapsed time: " + time );
        out.close();

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.PINK);
         for (Point p : points) {
            p.draw();
        }
        StdDraw.show();


        StdDraw.save(folder + image_filename);
        StdDraw.clear();
	}
}
