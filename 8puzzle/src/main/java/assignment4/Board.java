/* vi: set ts=4 sw=4 expandtab: */

import java.util.*;
import java.io.*;
import java.lang.*;

import edu.princeton.cs.algs4.*;

/**
 * Represents a Search node in an A* search
 * algorithm.
 *
 * A Tree structure mashortains the board state, the cost 
 * associated to reach this state from the initial state.  The previous
 * state that lead to this state, coupled with the action taken that arrived 
 * at this state, and the available actions that lead to further states.
 *
 */
public class Board
{
    private short N;           // Board dimension
    private short[][] grid;    // 2-Dimension array representing 8puzzle
    
    // Location of the Zero tile is cached so subsequent operations generating new boards
    private final int zero_row;
    private final int zero_col;

    // Cached values for determing if the board is in a goal state, the hamming sum, 
    // and the manhattan sum.
    private final boolean is_goal;
    private final int hammingSum;
    private final int manhattanSum;


   /** 
     * Construct a baord from an N-by-N array of blocks
     */
    public Board(int[][] blocks)
    {
        int N = blocks.length;

        boolean matches_goal = true;

        short manhattan_sum = 0;
        short hamming_sum = 0;
        int zero_row = -1, zero_col = -1;

        short[][] board_state = new short[N][N];

        // Copy the array and cache is_goal, hamming, and manhattan distance
        for(int row = 0; row < N; row++)
        {
            for(int col = 0; col < N; col++)
            {
                board_state[row][col] = (short)blocks[row][col];
                short should_be = (short) (1 + col + (row * N));

                if(col == N - 1 && row == N - 1)
                    should_be = 0;

                short val = board_state[row][col];

                if(val == 0)
                {
                    zero_row = row;
                    zero_col = col;
                }

                if(val != should_be)
                {
                    if(val != 0)
                    {
                        hamming_sum++;

                        int row_goal = (val-1)/N;
                        int col_goal = (val-1)%N;

                        manhattan_sum += Math.abs(row - row_goal) + Math.abs(col - col_goal);
                    }

                    if(matches_goal)
                        matches_goal = false;
                }
            }
        }

        hammingSum = hamming_sum;
        manhattanSum = manhattan_sum;
        is_goal = matches_goal;
        this.zero_row = zero_row;
        this.zero_col = zero_col;
        assert zero_row != -1;
        assert zero_col != -1;
        grid = board_state;
        this.N = (short)N;
    }


    /**
     * Constructor used from neighrbor takes ownership 
     * of the supplied aargument.
     */
    private Board(short[][] board_state)
    {
        int N = board_state.length;

        boolean matches_goal = true;

        short manhattan_sum = 0;
        short hamming_sum = 0;
        int zero_row = -1, zero_col = -1;

        for(int row = 0; row < N; row++)
        {
            for(int col = 0; col < N; col++)
            {
                short should_be = (short) (1 + col + (row * N));

                if(col == N - 1 && row == N - 1)
                    should_be = 0;

                // Copy the array and cache is_goal, hamming, and manhattan distance
                short val = board_state[row][col];

                if(val == 0)
                {
                    zero_row = row;
                    zero_col = col;
                }

                if(val != should_be)
                {
                    if(val != 0)
                    {
                        hamming_sum++;

                        int row_goal = (val-1)/N;
                        int col_goal = (val-1)%N;

                        manhattan_sum += Math.abs(row - row_goal) + Math.abs(col - col_goal);
                    }

                    if(matches_goal)
                        matches_goal = false;
                }
            }
        }

        hammingSum = hamming_sum;
        manhattanSum = manhattan_sum;
        is_goal = matches_goal;
        this.zero_row = zero_row;
        this.zero_col = zero_col;
        assert zero_row != -1;
        assert zero_col != -1;
        grid = board_state;
        this.N = (short)N;
    }


    private boolean isValid(short p_row, short p_col)
    {
        return p_row >= 0 && p_row < N
                && p_col >= 0 && p_col < N;
    }


    /**
     * Swap elements positioned in the multidemsional array
     * @param board, a 2D array of shorts, to manipulate
     * @param lhs_row first position's row
     * @param lhs_col first position's column
     * @param rhs_row second position's row
     * @param rhs_col second position's column
     */
    private static void swap_pos(short[][] board, int lhs_row, int lhs_col, int rhs_row, int rhs_col)
    {
        short temp = board[lhs_row][lhs_col];
        board[lhs_row][lhs_col] = board[rhs_row][rhs_col];
        board[rhs_row][rhs_col] = temp;
    }

    /**
      * Swap elements in an integer array.  Used by twin()
      * @param board, a 2D array of shorts, to manipulate
      * @param lhs_row first position's row
      * @param lhs_col first position's column
      * @param rhs_row second position's row
      * @param rhs_col second position's column
      */
    private static void swap_pos(int[][] board, int lhs_row, int lhs_col, int rhs_row, int rhs_col)
    {
        int temp = board[lhs_row][lhs_col];
        board[lhs_row][lhs_col] = board[rhs_row][rhs_col];
        board[rhs_row][rhs_col] = temp;
    }


    /** 
     * Board dimension N
     */
    public int dimension()
    {
        return N;
    }

    /**
     * Number of blocks out of place
     */
    public int hamming()
    {
        return hammingSum;
    }

    /**
     * Computes manhattan distance of the board.
     */
    public int manhattan()
    {
        return manhattanSum;
    }

    /**
     * Is this board the goal board?
     */
    public boolean isGoal()
    {
        return is_goal;
    }

    /**
     * A board that is obtained by exchanging any pair of blocks
     * 0 is not considered a block.
     * 
     * A twin board is constructed for determining the feasability of a finding a solution for a given board.
     */
    public Board twin()
    {
        int num = StdRandom.uniform(1,N*N);
        int num2 = 0;
       
        do {
            num2 = StdRandom.uniform(1,N*N);
        } while(num2 == num);

        int[][] new_board = new int[N][N];

        int n_row = -1, n_col = -1;
        int n2_row = -1, n2_col = -1;

        for(int row = 0; row < N; ++row)
        {
            for(int col = 0; col < N; ++col)
            {
                int val = grid[row][col];
                new_board[row][col] = val;

                if(val == num)
                {
                    n_row = row;
                    n_col = col;
                }
                else if(val == num2)
                {
                    n2_row = row;
                    n2_col = col;
                }
            }
        }

        assert n_row != -1;
        assert n_col != -1;
        assert n2_row != -1;
        assert n2_col != -1;

        swap_pos(new_board,n_row,n_col,n2_row,n2_col);
        
        Board twin = new Board(new_board);
        return twin;
    }

    /**
     * Does this board equal y
     */
    @Override
    public boolean equals(Object y)
    {
        if(y == this) return true;
        if(y == null) return false;
        if(y.getClass() != this.getClass()) return false;
        if(y instanceof Board)
        {
            Board b = (Board) y;
            if(N != b.N)
                return false;

            for(int row = 0; row < N;row++)
                for(int col = 0; col < N;col++)
                {
                    if(b.grid[row][col] != grid[row][col])
                        return false;
                }
            return true;
        }
        return false;
    }



    /**
     * List of neighboring boards, remembers the last action and
     * does not return that board.
     *
     * @returns An iterator over the the neighboring boards.  A neighboring board
     * is a board with the zero tile swapped with an adjacent tile.  W / N / E / S
     */
    public Iterable<Board> neighbors()
    {
        ArrayList<Board> ret = new ArrayList<Board>(4);

        int n_row = -1, n_col = -1;

        final int[][] offsets = {
             {-1,0},
             {1,0},
             {0,-1},
             {0,1}
        };


        for(int[] offset : offsets ) {
            int drow = offset[0];
            int dcol = offset[1];
            n_row = zero_row + drow;
            n_col = zero_col + dcol;

            if(n_row >= 0 && n_row < N
                && n_col >= 0 && n_col < N)
            {
                short[][] next_grid = cloneArray(grid);
                swap_pos(next_grid,zero_row,zero_col,n_row,n_col);
                Board neighbor = new Board(next_grid);
                ret.add(neighbor);
            }

        }
        return ret;
    }

    /**
     * String representation of this board
     */
    public String toString() {
        StringBuilder s = new StringBuilder(N*N + 4*N);
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", grid[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Copy the shorternal board
     * representation.
     */
    private static short[][] cloneArray(short[][] src) {
        int length = src.length;
        short[][] target = new short[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    private static int[][] cloneArray(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }


    /**
     * Unit tests
     */
    public static void main(String[] args)
    {

    }
}
