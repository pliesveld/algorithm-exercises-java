/* vi: set ts=4 sw=4 expandtab: */

import java.util.*;
import java.io.*;
import java.lang.*;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

/**
 * The 8puzzle contains a grid of tiles numbered from 1 on up, with one tile missing.  The player advances the board state by sliding a tile 
 * horizontally or vertically into this empty space.  In this representation, the zero tile is considered to be the 
 * blank tile, and it can be swapped with the tile that is immedaitely adjacent. 
 *
 *   1 |  2 |  3 |  4
 * ------------------
 *   5 |  6 |  7 |  0
 * ------------------
 *   9 | 10 | 11 |  8
 * ------------------
 *  13 | 14 | 15 | 12
 *
 *
 * There are three possible actions, the next board states are:
 *
 *   1 |  2 |  3 |  4
 * ------------------
 *   5 |  6 |  0 |  7
 * ------------------
 *   9 | 10 | 11 |  8
 * ------------------
 *  13 | 14 | 15 | 12
 *
 *   1 |  2 |  3 |  4
 * ------------------
 *   5 |  6 |  7 |  8
 * ------------------
 *   9 | 10 | 11 |  0
 * ------------------
 *  13 | 14 | 15 | 12
 *
 *   1 |  2 |  3 |  0
 * ------------------
 *   5 |  6 |  7 |  4
 * ------------------
 *   9 | 10 | 11 |  8
 * ------------------
 *  13 | 14 | 15 | 12
 *
 *
 * The 8puzzle solution is solved by performing an A* search of the initial board until 
 * a solution is found.  The A* search maintains a priority queue of of search nodes, ordered by
 * their cost. 
 *
 * A SearchNode maintains a state of the particular instance of an 8puzzle board.  The number of moves that
 * it were taken to arrive at this baord from the initial board.  The previous search node, and the priority or cost
 * of a search node.
 *
 * To guide the search space of the solution, a heuristics is used when calculating the prioirty of a search node.
 * A heursitic must satisfy two properties, it must never overestimate the cost of reaching the goal, and satisify
 * the triangle inequality.
 *
 * We never visit the parent's board from the children boards of the current board.
 *
 * We use the manhattan distance, an approximation of the number of moves that would be needed to arrive at the 
 * solution from a given board state, plus the number of moves from the intial state as the priority of the search node.
 *
 * If a tie exists, the search node's hamming distance is used as it's priority.
 *
 * 
 */
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

    /**
     * Maintains leaf node of the search tree, and builds solution of moves  
     * required to reach the goal from the initial board.
     */
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

    /**
     * Utility function for reporting contents of a search node
     */
     private static void report(SearchNode n)
    {
            StdOut.println(
                    "\nPriority:  " + n.priority +
                    "\nMoves:     " + n.moves +
                    "\nManhattan: " + n.board.manhattan() + "\n" +
                    n.board());
    }

    /**
     * Utility function for reporting contents of a priority queue of SearchNodes
     */
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
        /* 
         * If twin board solution is found, then none exists for initial board.
         */
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
                /*
                 * Critial Optimization.  Ignore parent's board from children.
                 */
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
