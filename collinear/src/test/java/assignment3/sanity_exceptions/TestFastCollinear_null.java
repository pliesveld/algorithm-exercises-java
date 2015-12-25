/* vi: set ts=4 sw=4 expandtab: */

import org.junit.Before;
import org.junit.Test;


public class TestFastCollinear_null
{
    Point[] points;
    FastCollinearPoints collinear;

    @Before
    public void loadPoints()
    {
        points = new Point[5];

        for(int i = 0;i < 5;++i)
        {
            if(i == 3)
                continue;

            points[i] = new Point(i,i);
        }
    }

    @Test(expected=java.lang.NullPointerException.class)
    public void checkInvalid()
    {
        collinear = new FastCollinearPoints(points);
    }

    @Test(expected=java.lang.NullPointerException.class)
    public void checkInvalidArg()
    {
        collinear = new FastCollinearPoints(null);
    }

}
