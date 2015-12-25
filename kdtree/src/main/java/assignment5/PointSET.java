/* vi: set ts=4 sw=4 expandtab: */
package assignment5;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> treeSet;

    /*
    private static final Comparator<Point2D> ORDER_BY_X = new Comparator<Point2D>()
        {
            public int compare(Point2D lhs, Point2D rhs)
            {
                if(lhs.equals(rhs))
                    return 0;

                return lhs.compareTo(rhs);
            }
        };
   */
    /**
     * Construct an empty set of points
     */
    public PointSET() {
        treeSet = new TreeSet<Point2D>();
    }

    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /** number of points in the set
     */
    public int size() {
        return treeSet.size();
    }

    /**
     * add the point to the set ( if it is not already in the set )
     */
    public void insert(Point2D p) {
        if(p == null)
            throw new NullPointerException("Point p is null");

        treeSet.add(p);
    }

    /**
     * does the set contain point p
     */
    public boolean contains(Point2D p) {
        if(p == null)
            throw new NullPointerException("Point p is null");

        if(isEmpty())
            return false;

        return treeSet.contains(p);

    }

    /**
     * draw all points to StdDraw
     */
    public void draw() {
        for(Point2D p : treeSet)
        {
            p.draw();
        }
    }

    /**
     * all points thare are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if(rect == null)
            throw new NullPointerException("Rect rect is null");

        Queue<Point2D> ret = new Queue<Point2D>();

        for(Point2D x : treeSet)
        {
            if(rect.contains(x))
            {
                ret.enqueue(x);
            }
        }
        return ret;
    }

    /**
     * nearest neighbor in the set to point p
     * returns null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if(p == null)
            throw new NullPointerException("Point p is null");
        if(isEmpty())
            return null;

        Point2D ret = null;
        double distance = Double.POSITIVE_INFINITY;


        for(Point2D x : treeSet)
        {
            double obs_distance = 0.0d;
            if( (obs_distance = p.distanceTo(x)) < distance)
            {
                ret = x;
                distance = obs_distance;
            }
        }
        return ret;

    }

}
