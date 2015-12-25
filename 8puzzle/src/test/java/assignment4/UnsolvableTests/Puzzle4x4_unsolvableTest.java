import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.Before;

import edu.princeton.cs.algs4.*;

public class Puzzle4x4_unsolvableTest
{   
    private final String PUZZLE_FILE = "puzzle4x4-unsolvable.txt";
    private final int EXPECTED_MOVES = -1;

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

    @Test(timeout = 40000)
    public void should_solve()
    {   
        initial = new Board(grid);
//        StdOut.println("Loading board: " + initial);
        assertNotNull(initial);
        solver = new Solver(initial);
        assertFalse("Board " + PUZZLE_FILE + " should be solvable.", solver.isSolvable());
        assertEquals("Solvable in " + EXPECTED_MOVES + " moves.",EXPECTED_MOVES,solver.moves());
	assertNull("Solution of unsolvable board should be null",solver.solution());

    }

}
