/* vi: set ts=4 sw=4 expandtab: */

import java.util.*;
import java.io.*;
import java.lang.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;

import edu.princeton.cs.algs4.*;

public class BoardHeuristicsTest
{
    Board b;
    final int[][] test_grid = 
    {
        {8, 1, 3},
        {4, 0, 2},
        {7, 6, 5}
    };

    public static void assertBlocksEqual(int[][] lhs, int[][] rhs)
    {
        int N;
        N= lhs.length;
        assertEquals("Arrays should be equal in length.",lhs.length,rhs.length);

        for(int row = 0; row < N; ++row)
        {
            assertEquals("lhs[row] should have length " + N,N,lhs[row].length);
            assertEquals("rhs[row] should have length " + N,N,rhs[row].length);
            assertArrayEquals(lhs[row],rhs[row]);
        }
    }

    @Before
    public void initialization()
    {
        b = new Board(test_grid);
        assertNotNull(b);
    }

    @Test
    public void sanity_check_length()
    {
        assertEquals(test_grid.length,3);
    }

    @Test
    public void test_hamming()
    {
        assertEquals("Hamming of test board",5,b.hamming());
    }
    
    @Test
    public void test_manhattan()
    {
        assertEquals("Manhattan of test board",10,b.manhattan());
    }
}
