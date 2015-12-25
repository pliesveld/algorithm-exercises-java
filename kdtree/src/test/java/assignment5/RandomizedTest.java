/* vi: set ts=4 sw=4 expandtab: */
package assignment5;
import edu.princeton.cs.algs4.Point2D;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

public class RandomizedTest
{
    List<Point2D> points;
    KdTree kdtree;
    PointSET pset;
    StringBuilder sb;

    @Before
    public void initializeTest()
    {
        sb = new StringBuilder();
        points = KdTreeGenerator.ListOfRandomPoints(3);
        kdtree = new KdTree();
        pset = new PointSET();

        for( Point2D p : points )
        {
            kdtree.insert(p);
            pset.insert(p);
            sb.append(p);
            sb.append("\n");
        }
    }


    void assertNearest(Point2D query)
    {
        Point2D expected = pset.nearest(query);
        Point2D actual = kdtree.nearest(query);

        if( (Math.abs( actual.x() - expected.x() ) > 0.000001)
                || (Math.abs( actual.y() - expected.y() ) > 0.000001))
        {
            String msg = "Query " + query +  "failed.  Expected: " + expected + " Actual:" + actual + "\n" + sb.toString();

            fail(msg);
        }
    }

    @Test
    public void lowerLeft()
    {
        Point2D q = new Point2D(0.0,0.0);
        assertNearest(q);
    }

    @Test
    public void lowerRight()
    {
        Point2D q = new Point2D(1.0,0.0);
        assertNearest(q);
    }

    @Test
    public void upperLeft()
    {
        Point2D q = new Point2D(0.0,1.0);
        assertNearest(q);
    }

    @Test
    public void upperRight()
    {
        Point2D q = new Point2D(0.0,1.0);
        assertNearest(q);
    }


}
