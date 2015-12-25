/* vi: set ts=4 sw=4 expandtab: */

import java.util.*;
import java.io.*;
import java.lang.*;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver
{

    private MinPQ<SearchNode> queue;
    private Solution solution;

    final private class SearchNode
            implements Comparable<SearchNode>
    {
        final private Board board;
        final private int moves;
        final private int priority;
        final private SearchNode previous;

        private SearchNode(Board board)
        {  
            this.board = board;
            this.moves = 0;
            this.previous = null;
            this.priority = 0 + board.manhattan();
        }

        private SearchNode(Board board,SearchNode previous_node)
        {
            this.board = board;
            this.moves = 1 + previous_node.moves;
            this.priority = 0 + moves + board.manhattan();
            this.previous = previous_node;
        }

        private SearchNode parent()
        {
            return previous;
        }

        private Board board()
        {
            return board;
        }

        @Override
        public int compareTo(SearchNode other)
        {
            int cmp = priority - other.priority;
            if(cmp != 0)
                return cmp;
            return board.hamming() - other.board.hamming();
        }

        /**
         * @returns true if @param root is a parent or grandparent of this
         * SearchNode.  Used to detect if a goal conidition is the ancester
         * of the twin board, or the initial board.
         */
        private boolean hasParent(SearchNode root)
        {
            SearchNode tmp = this;
            while(tmp != null)
            {
                if(tmp == root)
                    return true;
                tmp = tmp.previous;
            }
            return false;
        }
    
    }

    private class Solution
    {
        private SearchNode end;
        private int moves;
        private boolean solvable;

        private boolean solvable()
        {
            return solvable;
        }

        private int moves()
        {
            return moves;
        }
        
        private void setEnd(SearchNode end)
        {
            moves = end.moves;
            this.end = end;
        }

        private void setSolvability(boolean solvable)
        {
            this.solvable = solvable;
        }


        private Iterable<Board> build_solution()
        {
            if(!solvable)
                return null;

            java.util.Deque<Board> ret = new java.util.ArrayDeque<Board>();
            SearchNode tmp = end;
            while(tmp != null)
            {
                ret.addFirst(tmp.board());
                tmp = tmp.previous;
            }
            return ret;
        }
    }

    private static void report(SearchNode n)
    {
            StdOut.println(
                    "\nPriority:  " + n.priority +
                    "\nMoves:     " + n.moves +
                    "\nManhattan: " + n.board.manhattan() + "\n" +
                    n.board());
    }

    private static void report(MinPQ<SearchNode> pq)
    {
        for(SearchNode it : pq)
        {
            report(it);
        }
    }
    /**
     * Find a solution to the initial board (using A* algorithm)
     */
    public Solver(Board initial)
    {
        if(initial == null)
            throw new NullPointerException("Invalid initial board");

        queue = new MinPQ<SearchNode>(10);

        SearchNode root = new SearchNode(initial);
        SearchNode root_twin = new SearchNode(initial.twin());

        solution = new Solution();

        SearchNode u_node = null;

        queue.insert(root);
        queue.insert(root_twin);

        while(!queue.isEmpty())
        {
            u_node = queue.delMin();
            Board u_board = u_node.board();

            if(u_board.isGoal())
            {
                solution.setEnd(u_node);
                break;
            }

            for(Board v_board : u_board.neighbors())
            {
                if(u_node.parent() != null)
                    if(u_node.parent().board().equals(v_board))
                        continue;

                SearchNode v_node = new SearchNode(v_board,u_node);
                queue.insert(v_node);
            }
        }

        if(u_node != null && u_node.board().isGoal() && u_node.hasParent(root))
        {
            solution.setSolvability(true);
        }
    }

    /**
     * Is the initial board solvable?
     */
    public boolean isSolvable()
    {
        return solution.solvable();
    }

    /**
     * Min number of moves to solve initial board;
     * @returns -1 if unsolvable
     */
    public int moves()
    {
        if(!solution.solvable())
        {
            return -1;
        }
        return solution.moves();
    }
    
    /**
     * Sequence of boards in shortest solution;
     * @returns null if unsolvable
     */
    public Iterable<Board> solution()
    {
        if(!solution.solvable())
            return null;
        return solution.build_solution();
    }

   /**
     * Unit tests
     */
    public static void main(String[] args)
    {
    }

}
