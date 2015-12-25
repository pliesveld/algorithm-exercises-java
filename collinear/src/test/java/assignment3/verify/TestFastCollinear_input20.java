/* vi: set ts=4 sw=4 expandtab: */
import java.net.URL;
import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.In;

public class TestFastCollinear_input20 extends AbstractTestBaseCollinear
{
    static final String INPUT_FILE = "input20.txt";
    LineSegment[] answer = 
    {
        new LineSegment(new Point (4096, 20992),new Point (8128, 20992)),
        new LineSegment(new Point (4096, 20992),new Point (4096, 25088)),
        new LineSegment(new Point (4096, 25088),new Point (8192, 25088)),
        new LineSegment(new Point (8192, 25088),new Point (8192, 29184)),
        new LineSegment(new Point (4160, 29184),new Point (8192, 29184))
    };

    Point[] points;
    FastCollinearPoints collinear;

    @Override
    protected String getTestFilename()
    {
        return INPUT_FILE;
    }

    @Override
    protected LineSegment[] getAnswer()
    {
        return answer;
    }

    @Override
    protected LineSegment[] getCollinearSegments()
    {
        assertNotNull(collinear);
        return collinear.segments();
    }

    @Before
    public void loadFile()
    {
        points = loadFromFile(INPUT_FILE);
        collinear = new FastCollinearPoints(points);
    }

}