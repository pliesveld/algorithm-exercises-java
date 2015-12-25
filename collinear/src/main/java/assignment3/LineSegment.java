/* vi: set ts=4 sw=4 expandtab: */
/*************************************************************************
 *  Compilation:  javac LineSegment.java
 *  Execution:    none
 *  Dependencies: Point.java
 *
 *  An immutable datat ype for Line segments in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 *  DO NOT MODIFY THIS CODE.
 *
 *************************************************************************/

public class LineSegment implements Comparable<LineSegment> {
    private final Point p;   // one endpoint of this line segment
    private final Point q;   // the other endpoint of this line segment

    /**
     * Initializes a new line segment.
     *
     * @param  p one endpoint
     * @param  q the other endpoint
     * @throws NullPointerException if either <tt>p</tt> or <tt>q</tt>
     *         is <tt>null</tt>
     */
    public LineSegment(Point p, Point q) {
        if (p == null || q == null) {
            throw new NullPointerException("argument is null");
        }
        this.p = p;
        this.q = q;
    }

    
    /**
     * Draws this line segment to standard draw.
     */
    public void draw() {
        p.drawTo(q);
    }

    /**
     * Returns a string representation of this line segment
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this line segment
     */
    public String toString() {
        return this.min() + " -> " + this.max();
    }

    /**
     * Throws an exception if called. The hashCode() method is not supported because
     * hashing has not yet been introduced in this course. Moreover, hashing does not
     * typically lead to good *worst-case* performance guarantees, as required on this
     * asssignment.
     *
     * @throws UnsupportedOperationException if called
     */
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public Point min()
    {
        if(p.compareTo(q) <= 0)
            return p;
        return q;
    }

    public Point max()
    {
        if(p.compareTo(q) < 0)
            return q;
        return p;
    }


    /**
     * Line segments have two points.  They match, that is compareTo
     * returns 0 when they both min points and max points are equal.
     *
     * Otherwise the order returned is the minPoint(), and if those
     * are the same, maxPoint is used.
     *
     * might want to consider ordering by vector, minpoint + distance
     */
    public int compareTo(LineSegment that) {
        int ret;
        if((ret = this.min().compareTo(that.min())) == 0)
        {
            if((ret = this.max().compareTo(that.max())) == 0)
            {
                return 0;
            }
        }

        return ret;
    }



}

