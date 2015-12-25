/* vi: set ts=4 sw=4 expandtab: */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class TestBruteCollinear_corner
{

    @Test(expected=java.lang.IllegalArgumentException.class)
    public void checkInvalidDuplicate()
    {
        Point[] points = new Point[5];

        for(int i = 0;i < 5;++i)
        {
            points[i] = new Point(i,i);
        }

        points[4] = new Point(0,0);

        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    }

    @Test
    public void checkInvalidNumberOfPoints()
    {
        Point[] points = new Point[3];
        BruteCollinearPoints collinear;

        for(int i = 0;i < points.length;++i)
        {
            points[i] = new Point(i,i);
        }

        collinear = new BruteCollinearPoints(points);
        assertEquals(0,collinear.segments().length);
        
    }


}
