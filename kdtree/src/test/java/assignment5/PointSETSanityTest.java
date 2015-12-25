/* vi: set ts=4 sw=4 expandtab: */
package assignment5;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class PointSETSanityTest
{
    @Test
    public void boxTest()
    {
        PointSET ps = new PointSET();
        RectHV box = new RectHV(0.4,0.3,0.8,0.6);

        Point2D origin = new Point2D(0.0,0.0);
        ps.insert(origin);
        Point2D r = new Point2D(0.6,0.5); // inside rect
        ps.insert(r);
        Point2D outside = new Point2D(0.1,0.4);
        ps.insert(outside);

        assertTrue("PointSET should contain r",ps.contains(r));
        assertTrue("PointSET should contain origin",ps.contains(origin));
        assertTrue("PointSET should contain outside",ps.contains(outside));

        Iterable<Point2D> iterable = ps.range(box);
        Iterator<Point2D> it = iterable.iterator();
        assertNotNull("iterator should be valid",it);
        assertTrue("Iterator should have atleast one entry",it.hasNext());

        Point2D it_value = it.next();
        assertTrue("The first value from the iterator should be r ",it_value.equals(r));
        assertFalse("No more points should be in the iterator", it.hasNext());

    }

    @Test
    public void setTest()
    {
        Point2D p1 = new Point2D(0.3,0.7);
        Point2D p2 = new Point2D(0.3,0.7);

        PointSET ps = new PointSET();

        assertEquals(ps.size(),0);
        ps.insert(p1);
        assertEquals(ps.size(),1);
        ps.insert(p2);
        assertEquals("Inserting a duplicate value should not change a set",ps.size(),1);
    }


    @Test
    public void emptyPointSET()
    {
        PointSET ps = new PointSET();
        Point2D origin = new Point2D(0.0,0.0);
        assertNull(ps.nearest(origin));
    }

}
