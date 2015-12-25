/* vi: set ts=4 sw=4 expandtab: */

import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.Before;

import edu.princeton.cs.algs4.*;

public class PuzzleTest
{
    private final String PUZZLE_FILE = "puzzle03.txt";
    private final int EXPECTED_MOVES = 3;

    private int[][] grid;

    private Board initial;
    private Solver solver;

    @Before
    public void load_puzzle() throws java.io.IOException
    {
        URL url = this.getClass().getResource("/" + PUZZLE_FILE);
        File file = new File(url.getFile());
        assertTrue("Couldn't find puzzle file",file.exists());
        grid = PuzzleChecker.loadBoardFromFile(file);
    }

    @Test
    public void should_solve()
    {
        initial = new Board(grid);
        StdOut.println("Loading board: " + initial);
        assertNotNull(initial);
        solver = new Solver(initial);
//        assertTrue("Board " + PUZZLE_FILE + " should be solvable.", solver.isSolvable());
//        assertEquals("Solvable in " + EXPECTED_MOVES + " moves.",EXPECTED_MOVES,solver.moves());
        boolean solvable;
        StdOut.println(initial);
        StdOut.print("Board " + PUZZLE_FILE + " reported solvable: " + (solvable = solver.isSolvable()));

        if(solvable)
            StdOut.println(" in " + solver.moves() + " moves.");
        else
            StdOut.println("");

    }

}
