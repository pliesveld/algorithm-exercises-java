/* vi: set ts=4 sw=4 expandtab: */
import java.net.URL;
import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.In;

public class TestBruteCollinear_input_horizontal5 extends AbstractTestBaseCollinear
{
    static final String INPUT_FILE = "horizontal5.txt";
    LineSegment[] answer = 
    {
        new LineSegment(new Point(2682,14118),new Point(7821,14118)),
        new LineSegment(new Point(4750,4652),new Point(16307,4652)),
        new LineSegment(new Point(8934,7996),new Point(20547,7996)),
        new LineSegment(new Point(1888,7657),new Point(13832,7657)),
        new LineSegment(new Point(10375,12711),new Point(20385,12711))
    };

    Point[] points;
    BruteCollinearPoints collinear;

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
        collinear = new BruteCollinearPoints(points);
    }


}
