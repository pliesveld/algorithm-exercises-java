/* vi: set ts=4 sw=4 expandtab: */
/* algs4partI @ coursera
 * Patrick Liesveld
 */
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;

/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments.  
 * To check whether the 5 points p, q, r, and s are collinear, check whether the three slopes between p and q, between p and r, and between p and s are all equal.
 *
 * The problem can be solved faster than the brute-force solution.
 *
 * Given a point p, the following method determines whether p participates in a set of 4 or more collinear points.
 *
 * 1)  Think of p as the origin.
 * 2)  For each other point q, determine the slope it makes with p.
 * 3)  Sort the points according to the slopes they make with p.
 * 4)  Check if any 3 ( or more) adjacent points in the sorted order have equal slopes with respect to p.  If so, these points together with p, are collinear.
 */
public class FastCollinearPoints 
{
    
    private List<LineSegment> collinear;
    /**
     * Finds all line segments containing 4 points
     */
    public FastCollinearPoints(Point[] arg_points)
    {
        if(arg_points == null)
            throw new java.lang.NullPointerException("Invalid arg_pointsument points[] array was passed null arg_pointsument");


        /*
         * Sort the initial array by the implemented interface java.util.Comparable
         * Point.compareTo(Point rhs)
         *
         * The beginning of the array will be ordered by the lowest y-coordinate, then the lowest x-coordinate.
         * */
        Point[] points = arg_points.clone();
        Arrays.sort(points);

        /*
         * Identical point values will be next to each other, so a linear scan detects
         * if there are duplicates points in the input array.
         */
        if(points[0] == null)
            throw new java.lang.NullPointerException("Points array contains a null reference.");

        for(int i = 0; i < points.length - 1;i++)
        {
            int j = i + 1;
            if(points[j] == null)
                throw new java.lang.NullPointerException("Points array contains a null reference.");

            if(points[i].compareTo(points[j]) == 0)
                throw new java.lang.IllegalArgumentException("Array contains a duplicate point: " + points[i]);
        }

        collinear = new LinkedList<LineSegment>();

        if(arg_points.length < 4)
            return;


        /*
         * To determine if a sequences of points is part of a point segment of atleast 4 points,
         * we first exaiming each point as being the origin point.
         */
        for(int i = 0;i < points.length;++i)
        {
            Point origin = points[i];

            /*
             * Once an origin point is selected, a copy of the points is made
             * and sorted.  This array is already sorted by the order defined by
             * the Point.compareTo.  
             *
             * The Point class provides a method that returns a Comparator<Point>
             * object that provides an ordering of Points based on the slope from the 
             * originating point to each point.
             *
             * We use the Collections.sort() method because mergesort provides a stable
             * sorting algorithm that will maintain the prior ordering of by y-coordinate,
             * then by x-coordinate when slopes are the same value.
             *
             * This property insures when a candidate segment is found, origin will be 
             * the smallest point of the segment.  Allowing us to discard a segment if
             * a point in the segment is less than the origin point.  Because it is
             * determined to be a partial segment.
             *
             */
            ArrayList<Point> points_array = new ArrayList<Point>(Arrays.asList(points));
            Collections.sort(points_array,origin.slopeOrder());

            /*
             * Find sequences of points that have the same slope from the origin.
             *
             * k    : index into slope-sorted array of points
             * j    : index of current sequence of same-sloped points from origin
             * slope_current : last observed slope from origin
             * slope_last : updates when the last observed slope value changes to a different value
             * len : tracks current sequence length.
             *
             */
            double slope_last = Double.NEGATIVE_INFINITY;
            int len = 0;
            int j;

            for(int k = 0;k < points_array.size() - 1;k = j)
            {
                double slope_current = 0.0d;
                j = k + 1;
                len = 0;

                do {
                    Point next = points_array.get(j);
                    slope_current = origin.slopeTo(next);
                } while(slope_last == slope_current && ++len > 0 && ++j < points_array.size());

                /*
                 * At this point, a sequence of points indexed from k to k + len
                 * has been found, each having the same slope from the origin point.
                 *
                 * Again, because a stable sort is used, the prior ordering of by 
                 * y-coordinate and then by x-coordinate is maintained.  Meaning the point
                 * at points_array.get(k) is the lowest point in the line segment.
                 *
                 * Because all these points have the same slope value, origin is part of the
                 * line segment these points lay on.  And because origin starts at the lowest value 
                 * of all points, origin is less than the low point of the line segment, then this
                 * segment is a full segment.  If it is not, then it must be a partial segment.
                 */
                if(len >= 2)
                {
                    Point low = points_array.get(k);
                    Point high = points_array.get(k + len);

                    if(origin.compareTo(low) < 0)
                    {
                        addingSegment(origin,high);
                    }
                }
                slope_last = slope_current;
            }
        }

    }


    private void addingSegment(Point p, Point q)
    {
//      StdOut.printf("adding segment %s -> %s\n",p,q);
        LineSegment seg = new LineSegment(p,q);
        collinear.add(seg);
    }


    /**
     * the number of line segments
     */
    public int numberOfSegments()
    {
        return collinear.size();
    }

    /**
     * the line segments
     */
    public LineSegment[] segments()
    {
        // should include each line segment containing 4 points exactly once.
        //
        // if 4 points appear on a line segment in the order p -> q -> r -> s, then
        // the line segment should be either p -> s or s -> p, but not both.
        //
        // and it should not include subsegments such as p -> r, q -> r.
        return collinear.toArray(new LineSegment[collinear.size()]);
    }
}
