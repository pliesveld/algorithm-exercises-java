package assignment5;/* vi: set ts=4 sw=4 expandtab: */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

import java.io.File;
import java.net.URL;


public class DrawKdTree
{
	final String INPUT_FILE = "spec.txt";

    static KdTree ReadFromFile(File infile)
    {
        In in = new In(infile);
        KdTree ret = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            StdDraw.setPenRadius(0.005);
            StdDraw.setPenColor(StdDraw.BLACK);
            p.draw();
            ret.insert(p);
        }
        return ret;
    }

    public static void testFile(URL file)
    {
        File infile = new File(file.getFile());
        String folder = file.getPath();
        folder = folder.substring(0,folder.lastIndexOf('/')+1);
        String filename = infile.getName();
        String filename_root = filename.substring(0,filename.lastIndexOf('.'));
        String image_filename = filename_root + ".png";

        StdDraw.show(0);
        StdDraw.setXscale(0.0,1.0);
        StdDraw.setYscale(0.0,1.0);

        KdTree kdtree = ReadFromFile(infile);
        kdtree.draw();

        StdDraw.show();
        StdDraw.save(folder + image_filename);
        StdDraw.clear();
    }



}

