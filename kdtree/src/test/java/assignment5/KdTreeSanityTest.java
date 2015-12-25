/* vi: set ts=4 sw=4 expandtab: */
package assignment5;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class KdTreeSanityTest
{
    @Test
    public void boxTest()
    {
        KdTree kdtree = new KdTree();
        RectHV box = new RectHV(0.4,0.3,0.8,0.6);

        Point2D origin = new Point2D(0.0,0.0);
        kdtree.insert(origin);
        Point2D r = new Point2D(0.6,0.5); // inside rect
        kdtree.insert(r);
        Point2D outside = new Point2D(0.1,0.4);
        kdtree.insert(outside);

        assertTrue("KdTree should contain r",kdtree.contains(r));
        assertTrue("KdTree should contain origin",kdtree.contains(origin));
        assertTrue("KdTree should contain outside",kdtree.contains(outside));

        assertFalse("tree should not be empty",kdtree.isEmpty());

        Iterable<Point2D> iterable = kdtree.range(box);
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

        KdTree kdtree = new KdTree();

        assertEquals(kdtree.size(),0);
        kdtree.insert(p1);
        assertEquals(kdtree.size(),1);
        kdtree.insert(p2);
        assertEquals("Inserting a duplicate value should not change a set",kdtree.size(),1);
    }


    @Test
    public void emptyKdTree()
    {
        KdTree kdtree = new KdTree();
        Point2D origin = new Point2D(0.0,0.0);
        assertTrue("tree should be empry",kdtree.isEmpty());
        assertNull(kdtree.nearest(origin));
    }

}
