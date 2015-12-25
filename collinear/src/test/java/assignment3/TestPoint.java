/* vi: set ts=4 sw=4 expandtab: */

import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestPoint
{
	@Test
	public void pointCompare()
	{
		Point p1 = new Point(1,2);
		Point p2 = new Point(1,2);

		assertEquals("Points should equal",0,p1.compareTo(p2));
		assertEquals("Points should equal",0,p2.compareTo(p1));

		Point origin = new Point(0,0);

		assertTrue("origin.compareTo(new Point(1,2)) should be a negative number",origin.compareTo(p1) < 0);

		Comparator<Point> cmp = origin.slopeOrder();
		assertEquals("Comparator to same obj should be zero",0,cmp.compare(p1,p2));

		Comparator<Point> cmp_p1 = p1.slopeOrder();
		assertEquals("Comparator to origin should be zero",0,cmp_p1.compare(origin,origin));

	}

	@Test
	public void twoPointCompare()
	{
		Point origin = new Point(0,0);
		Point p1 = new Point(1,2);
		assertTrue("comparing a point higher should return a value less than zero ",origin.compareTo(p1) < 0);
		assertTrue("comparing a point lower should return a value greater than zero ",p1.compareTo(origin) > 0);
	}

	@Test
	public void slopeCompareLimits()
	{
		Point origin = new Point(0,0);

		Point p_horizontal = new Point(100,0);
		Point p_vertical = new Point(0,100);

		assertEquals("slope of horizontal line segment should be +0",+0.0,origin.slopeTo(p_horizontal),0.0001);
		assertEquals("slope of vertical line segment should be +inf",Double.POSITIVE_INFINITY,origin.slopeTo(p_vertical),0.0001);
		assertEquals("slope of degenerate line segment should be -inf",Double.NEGATIVE_INFINITY,origin.slopeTo(origin),0.0001);
	}

	@Test
	public void slopeCompare()
	{
		Point origin = new Point(0,0);

		Point p1 = new Point(9,7);
		Point p2 = new Point(45,35);

		assertEquals("Slope should be same",origin.slopeTo(p2),origin.slopeTo(p1),0.0001);
		assertEquals("Slope should be same",origin.slopeTo(p1),origin.slopeTo(p2),0.0001);
	}

	@Test
	public void slopeCompareEquality()
	{
		Point origin = new Point(0,0);

		Point p1 = new Point(1,1);
		Point p2 = new Point(2,2);
		Point p3 = new Point(3,3);

        assertTrue("Equality expression of slopes should be true.",origin.slopeTo(p1) == origin.slopeTo(p2));
        assertTrue("Equality expression of slopes should be true.",origin.slopeTo(p1) == origin.slopeTo(p3));
        assertTrue("Equality expression of slopes should be true.",origin.slopeTo(p2) == origin.slopeTo(p3));
	}


}
