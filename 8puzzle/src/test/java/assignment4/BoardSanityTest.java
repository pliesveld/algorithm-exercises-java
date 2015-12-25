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

public class BoardSanityTest
{
    Board b;
    final int[][] grid3_solution = 
    {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 0}
    };

    public static void assertBlocksEqual(int[][] lhs, int[][] rhs)
    {
        int N;
        if( (N=lhs.length) != rhs.length)
        {
            fail("lhs.length != rhs.length");
        }

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

        b = new Board(grid3_solution);
        assertNotNull(b);
    }

    @Test
    public void testDim()
    {
        assertEquals("Should be dim 3",3,b.dimension());
    }

    @Test
    public void testGoal()
    {
        assertTrue("Should be goal for 3 grid",b.isGoal());
    }

    @Test
    public void printAdjacents()
    {
        int i = 1;
        for(Board v : b.neighbors())
        {
            StdOut.printf("neighbor %d\n%s\n",i++,v.toString());
        }
    }
    
}
