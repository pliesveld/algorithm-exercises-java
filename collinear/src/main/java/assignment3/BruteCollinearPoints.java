/* vi: set ts=4 sw=4 expandtab: */
/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments.  
 * To check whether the 5 points p, q, r, and s are collinear, check whether the three slopes between p and q, between p and r, and between p and s are all equal.
 */

           
import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.StringBuilder;

public class BruteCollinearPoints 
{
    private ArrayList<LineSegment> collinear;

    /**
     * Finds all line segments containing 4 points
     */
    public BruteCollinearPoints(Point[] arg_points)
    {

        if(arg_points == null)
            throw new java.lang.NullPointerException("Invalid arg_pointsument points[] array was passed null arg_pointsument");

        Point[] points = Arrays.copyOf(arg_points,arg_points.length);
        Arrays.sort(points);

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

        collinear = new ArrayList<LineSegment>();

        if(arg_points.length < 4)
            return;



        /*
         * Permutate through all combinations of 4 points from array.
         *
         * Array indexes: 1_0.. i_3 map state of permutation.
         * 
         * Array indexes are initiallized to the first four array indexes.
         *  -- -- -- -- -- -- 
         * |i0|i1|i2|i3|  |  |
         * |  |  |  |  |  |  |
         *  -- -- -- -- -- -- 
         *  
         *
         * The next iteration moves the last array index towards the end of the array
         *  -- -- -- -- -- -- 
         * |i0|i1|i2|  |i3|  |
         * |  |  |  |  |  |  |
         *  -- -- -- -- -- -- 
         *
         * After the last iterator reaches the end, the second to last iterator is advanced.
         *  -- -- -- -- -- -- 
         * |i0|i1|i2|  |  |i3|
         * |  |  |  |  |  |  |
         *  -- -- -- -- -- -- 
         *
         * Then the last iterator i3 is set to be i2 + 1
         *  -- -- -- -- -- -- 
         * |i0|i1|  |i2|i3|  |
         * |  |  |  |  |  |  |
         *  -- -- -- -- -- -- 
         *
         *  -- -- -- -- -- -- 
         * |i0|i1|  |  |i2|i3|
         * |  |  |  |  |  |  |
         *  -- -- -- -- -- -- 
          *
          * */
        int i_0 = 0;
        int i_1 = 1;
        int i_2 = 2;
        int i_3 = 3;

        int len = points.length;


        for(i_0 = 0;i_0 < len - 3;++i_0)
        {
            for(i_1 = i_0 + 1;i_1 < len - 2;++i_1)
            {
                for(i_2 = i_1 + 1;i_2 < len - 1;++i_2)
                {
                    /* skip if three points don't match */
                    if(!check3Points(points,i_0,i_1,i_2))
                        continue;

                    for(i_3 = i_2 + 1;i_3 < len;++i_3)
                    {
                        Point[] pts = {
                            points[i_0],
                            points[i_1],
                            points[i_2],
                            points[i_3]
                        };
/*

                        StringBuilder sb = new StringBuilder("Checking points: ");
                        for(Point pt : pts)
                        {
                            sb.append(pt);
                            sb.append(" ");

                        }
                        StdOut.println(sb);
*/
                        if(check4Points(pts))
                        {
                            addingSegment(pts[0],pts[3]);
                        }
                    }
                }
            }
        }
         
    }

    /*
     * Check if three points match, four points will match.
     */
    private boolean check3Points(Point[] points,int i0, int i1, int i2)
    {
        Point low;
        Point mid;
        Point high;

        if(points[i0].compareTo(points[i1]) < 0 && points[i0].compareTo(points[i2]) < 0)
        {
            low = points[i0];
            mid = points[i1];
            high = points[i2];
        } else if(points[i1].compareTo(points[i0]) < 0 && points[i1].compareTo(points[i2]) < 0) {
            low = points[i1];
            mid = points[i0];
            high = points[i2];
        } else {
            low = points[i2];
            mid = points[i0];
            high = points[i1];
        }

        return low.slopeTo(mid) == low.slopeTo(high);
    }

    private boolean check4Points(Point[] points)
    {

        Arrays.sort(points);


        Point min = points[0];
        Point mid_l = points[1];
        Point mid_h = points[2];
        Point max = points[3];

        double slope;
        if( (slope = min.slopeTo(mid_l)) == min.slopeTo(mid_h) 
                && slope == min.slopeTo(max))
        {
            return true;
        }

        return false;
        // y = mx + b
        // (min.y)  = (slope) * (min.x) + b
        //
        // (min.y) - (slope) * (min.x) = b
        // b = (min.y) - (slope) * (min.x)
        //double y_inter = min.y - slope*min.x;


    }

    private void addingSegment(Point p, Point q)
    {
        LineSegment seg = new LineSegment(p,q);
//        StdOut.println("Adding segment: " + seg);
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
