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

public class TestFastCollinear_input8 extends AbstractTestBaseCollinear
{
    static final String INPUT_FILE = "input8.txt";
    LineSegment[] answer = 
    {
        new LineSegment(new Point(10000,0),new Point(0,10000)),
        new LineSegment(new Point(3000,4000),new Point(20000,21000))
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
