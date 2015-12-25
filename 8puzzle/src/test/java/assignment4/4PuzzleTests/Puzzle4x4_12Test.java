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

public class Puzzle4x4_12Test
{   
    private final String PUZZLE_FILE = "puzzle4x4-12.txt";
    private final int EXPECTED_MOVES = 12;

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

    @Test(timeout = 10000)
    public void should_solve()
    {   
        initial = new Board(grid);
//        StdOut.println("Loading board: " + initial);
        assertNotNull(initial);
        solver = new Solver(initial);
        assertTrue("Board " + PUZZLE_FILE + " should be solvable.", solver.isSolvable());
        assertEquals("Solvable in " + EXPECTED_MOVES + " moves.",EXPECTED_MOVES,solver.moves());

    }

}
