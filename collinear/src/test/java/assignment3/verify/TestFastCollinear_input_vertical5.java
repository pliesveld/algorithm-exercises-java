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

public class TestFastCollinear_input_vertical5 extends AbstractTestBaseCollinear
{
    static final String INPUT_FILE = "vertical5.txt";
    LineSegment[] answer = 
    {
        new LineSegment(new Point(14407,10367),new Point(14407,19953)),
        new LineSegment(new Point(15976,3370),new Point(15976,9945)),
        new LineSegment(new Point(2088,6070),new Point(2088,16387)),
        new LineSegment(new Point(5757,3426),new Point(5757,20856)),
        new LineSegment(new Point(8421,1829),new Point(8421,18715))
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
