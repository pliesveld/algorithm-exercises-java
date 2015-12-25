/* vi: set ts=4 sw=4 expandtab: */
package assignment5;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCircle10
{
    static final String INPUT_FILE = "circle10.txt";

    KdTree kdtree;
    PointSET pset;

    void ReadFromFile(File infile)
    {
        In in = new In(infile);
        kdtree = new KdTree();
        pset = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            pset.insert(p);
        }
    }

    @Before
    public void loadFiles()
    {
        URL url = this.getClass().getResource(INPUT_FILE);
        File testFile = new File(url.getFile());
        assertTrue("Couldnt find " + INPUT_FILE,testFile.exists());

        ReadFromFile(testFile);
        assertTrue("pset should not be empty",!pset.isEmpty());
        assertTrue("kdtree should not be empty",!kdtree.isEmpty());
    }

    @Test
    public void psetNearest()
    {
        Point2D query = new Point2D(0.81, 0.30);
        Point2D response = pset.nearest(query);
        assertEquals(0.975528,response.x(),0.000001);
        assertEquals(0.345492,response.y(),0.000001);
        //Point2D correct 0.975528 0.345492
    }

    @Test
    public void kdtreeNearest()
    {
        Point2D query = new Point2D(0.81, 0.30);
        Point2D response = kdtree.nearest(query);
        assertEquals(0.975528,response.x(),0.000001);
        assertEquals(0.345492,response.y(),0.000001);
        //Point2D correct 0.975528 0.345492
    }


}

