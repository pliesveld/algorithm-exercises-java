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


public class BoardTwinTest
{

    int[][] test_grid = 
    {
        {1,0},
        {2,3}
    };
    Board b;

    @Before
    public void initialization()
    {
        b = new Board(test_grid);
    }

    @Test
    public void zero_shouldnt_move()
    {
        String out_case1 = "2\n 2  0 \n 1  3 \n";
        String out_case2 = "2\n 1  0 \n 3  2 \n";
        String out_case3 = "2\n 3  0 \n 2  1 \n";

        int zero_loc;
        for(int i = 0;i < 100;++i)
        {
            Board twin = b.twin();
            String twin_out = twin.toString();

            if(twin_out.equals(out_case1))
                continue;

            if(twin_out.equals(out_case2))
                continue;

            if(twin_out.equals(out_case3))
                continue;

            StdOut.println("Twin can be only one of three states:");
            StdOut.println(out_case1);
            StdOut.println(out_case2);
            StdOut.println(out_case3);
            StdOut.println(twin_out);
            fail("Invalid twin state");


        }
    }
}
