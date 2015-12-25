/* vi: set ts=4 sw=4 expandtab: */
package assignment5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;


/**
 * KdTree is a BST data structure the search space is split among multiple dimensions.
 * Given a set S of n points or more complicated geometric objects in k dimensions.  Construct
 * a tree that partitions space by half-planes such that each object contained in its own box-shaped region.
 */
public class KdTree {

    private static final Comparator<Point2D> ORDER_BY_X = new Comparator<Point2D>()
        {
            public int compare(Point2D lhs, Point2D rhs)
            {
                if(lhs.x() == rhs.x())
                    return +0;
                double cmp = lhs.x() - rhs.x();
                if(cmp < 0)
                    return -1;
                else
                    return +1;
            }
        };

    private static final Comparator<Point2D> ORDER_BY_Y = new Comparator<Point2D>()
        {
            public int compare(Point2D lhs, Point2D rhs)
            {
                if(lhs.y() == rhs.y())
                    return +0;
                double cmp = lhs.y() - rhs.y();
                if(cmp < 0)
                    return -1;
                else
                    return +1;
            }
        };

    private enum NodeType {
        HORIZONTAL
        {
            NodeType next()
            {
                return NodeType.VERTICAL;
            }

            int compare(Point2D lhs, Point2D rhs)
            {
                return ORDER_BY_Y.compare(lhs,rhs);
            }
        },
        VERTICAL
        {
            NodeType next()
            {
                return NodeType.HORIZONTAL;
            }

            int compare(Point2D lhs, Point2D rhs)
            {
                return ORDER_BY_X.compare(lhs,rhs);
            }
        };

        abstract NodeType next();
        abstract int compare(Point2D lhs, Point2D rhs);
        int compare(RectHV lhs, Point2D rhs)
        {
            int ret = this.compare(new Point2D(lhs.xmin(),lhs.ymin()),rhs)
                    + this.compare(new Point2D(lhs.xmax(),lhs.ymax()),rhs);

            if(ret == -2)
                return -1;
            if(ret == 2)
                return +1;
            return +0;
        }

    }

    private static class Node
    {
        private Point2D key;
        private Node parent = null;
        private Node left = null;
        private Node right = null;
    }

    private int count = 0;
    private Node root = null;
    /**
     * Construct an empty set of points
     */
    public KdTree() {
    }

    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return (root == null);
    }

    /** number of points in the set
     */
    public int size() {
        return count;
    }

    /**
     * add the point to the set ( if it is not already in the set )
     */
    public void insert(Point2D p) {
        if(p == null)
            throw new NullPointerException("Point p is null");

        int cmp = 0;
        Node x = root;
        Node y = null;
        NodeType node_type = NodeType.VERTICAL;

        while( x != null)
        {
            y = x;
            if(x.key.equals(p))
                return;

            cmp = node_type.compare(p,x.key);

            if(cmp < 0)
            {
                x = x.left;
            } else {
                x = x.right;
            }
            node_type = node_type.next();
        }

        Node temp = new Node();
        temp.parent = y;
        temp.key = p;

        if(y == null) {
            root = temp;
        } else if(cmp < 0) {
            y.left = temp;
        } else {
            y.right = temp;
        }

        ++count;
    }

    /**
     * does the set contain point p
     */
    public boolean contains(Point2D p) {
        if(p == null)
            throw new NullPointerException("Point p is null");

        if(isEmpty())
            return false;

        int cmp = 0;
        Node x = root;
        NodeType node_type = NodeType.VERTICAL;

        while( x != null)
        {
            if(p.equals(x.key))
                return true;

            cmp = node_type.compare(p,x.key);

            if(cmp < 0)
            {
                x = x.left;
            } else {
                x = x.right;
            }
            node_type = node_type.next();
        }
        return false;
    }

    private void draw_helper(Node n, NodeType ntype, double xmin, double xmax, double ymin, double ymax)
    {
        if(n == null)
            return;


        Point2D pt = n.key;

        double pt_x = pt.x();
        double pt_y = pt.y();

        if(ntype == NodeType.VERTICAL)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(pt_x,ymin,pt_x,ymax);

            NodeType ntype_next = ntype.next();

            if(n.left != null)
            {
                draw_helper(n.left,ntype_next,xmin,pt_x,ymin,ymax);
            }

            if(n.right != null)
            {
                draw_helper(n.right,ntype_next,pt_x,xmax,ymin,ymax);
            }

        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xmin,pt_y,xmax,pt_y);

            NodeType ntype_next = ntype.next();

            if(n.left != null)
            {
                draw_helper(n.left,ntype_next,xmin,xmax,ymin,pt_y);
            }

            if(n.right != null)
            {
                draw_helper(n.right,ntype_next,xmin,xmax,pt_y,ymax);
            }

        }



    }

    /**
     * A 2d-tree divides the unit square in a simple way:  all the points to the left of the root
     * go in the left subtree; all those to the right go in the right subtree; and so forth, recursively.
     * draw all points in black, and subdivisions in red( for vertical splits ), and blue (for horizontal splits)
     */
    public void draw() {
        if(isEmpty())
            return;

        Queue<Node> q = new Queue<>();

        double xmin = 0.0, ymin = 0.0, xmax = 1.0, ymax = 1.0;
        StdDraw.setPenRadius(0.001);
        draw_helper(root,NodeType.VERTICAL,xmin,xmax,ymin,ymax);

        q.enqueue(root);

        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        while(!q.isEmpty())
        {
            Node t = q.dequeue();
            t.key.draw();

            if(t.left != null)
                q.enqueue(t.left);
            if(t.right != null)
                q.enqueue(t.right);
        }
    }

    private void findPointsHelper(Node node,RectHV rect, Queue<Point2D> queue)
    {
        Queue<Node> fringe = new Queue<>();
        fringe.enqueue(node);
        while(!fringe.isEmpty())
        {
            Node x = fringe.dequeue();
            if(rect.contains(x.key))
                queue.enqueue(x.key);

            if(x.left != null)
            {
                fringe.enqueue(x.left);
            }
            if(x.right != null)
            {
                fringe.enqueue(x.right);
            }

        }
    }

    /**
     * Find all points contained in a given query rectangle.
     */
    public Iterable<Point2D> range(RectHV rect) {
        if(rect == null)
            throw new NullPointerException("Rect rect is null");

        Queue<Node> fringe = new Queue<>();
        Queue<Point2D> ret = new Queue<>();


        if(isEmpty())
            return ret;


        if(rect.contains(root.key))
        {
            findPointsHelper(root,rect,ret);
            return ret;
        }

        Node x = root;
        NodeType ntype = NodeType.VERTICAL;


        do
        {
            int cmp = ntype.compare(rect,x.key);

            if(cmp < 0)
            {
                x = x.left;
            } else if(cmp > 0) {
                x = x.right;
            } else {
                findPointsHelper(x,rect,ret);
                break;
            }

            ntype = ntype.next();

        } while(x != null);

        return ret;
    }

    private Point2D nearest_helper(Node x,double distance_best,NodeType ntype,Point2D p)
    {
        if(x == null)
            return null;

        double distance = p.distanceTo(x.key);


        Node next_x = x;
        int cmp = ntype.compare(p,x.key);
        if(cmp < 0)
        {
            next_x = x.left;
        } else {
            next_x = x.right;
        }

        Point2D candidate = nearest_helper(next_x,distance_best,ntype.next(),p);

        if(candidate == null)
        {

            candidate = nearest_helper(next_x,distance_best,ntype.next(),p);
        }


        return candidate;
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

        double distance = Double.POSITIVE_INFINITY;
        NodeType ntype = NodeType.VERTICAL;

        Node x = root;
        Point2D ret = x.key;

        distance = p.distanceTo(x.key);

        do
        {
            double distance_x = p.distanceTo(x.key);

            if(distance_x < distance)
            {
                distance = distance_x;
                ret = x.key;
            }

            int cmp = ntype.compare(p,x.key);
            if(cmp < 0)
            {
                x = x.left;
            } else {
                x = x.right;
            }

            ntype = ntype.next();
        } while( x != null );

        return ret;
    }
}
