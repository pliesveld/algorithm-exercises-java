/* vi: set ts=4 sw=4 expandtab: */
/******************************************************************************
 *  Compilation:  javac PuzzleChecker.java
 *  Execution:    java PuzzleChecker filename1.txt filename2.txt ...
 *  Dependencies: Board.java Solver.java
 *
 *  This program creates an initial board from each filename specified
 *  on the command line and finds the minimum number of moves to
 *  reach the goal state.
 *
 *  % java PuzzleChecker puzzle*.txt
 *  puzzle00.txt: 0
 *  puzzle01.txt: 1
 *  puzzle02.txt: 2
 *  puzzle03.txt: 3
 *  puzzle04.txt: 4
 *  puzzle05.txt: 5
 *  puzzle06.txt: 6
 *  ...
 *  puzzle3x3-impossible: -1
 *  ...
 *  puzzle42.txt: 42
 *  puzzle43.txt: 43
 *  puzzle44.txt: 44
 *  puzzle45.txt: 45
 *
 ******************************************************************************/
import java.io.IOException;
import java.io.File;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class PuzzleChecker {
       // read in the board specified in the filename
    public static int[][] loadBoardFromFile(File filename) throws IOException
    {
        In in = new In(filename);
        if(!in.exists())
            throw new IOException("Couldn't open file: " + filename);

        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        return tiles;
    }

    public static void main(String[] args) throws IOException {
        // for each command-line argument
        for (String filename : args) {
            File file = new File(filename);
            if(!file.exists())
            {
                StdOut.println("Couldn't find file: " + file);
                continue;
            }
            int[][] tiles = loadBoardFromFile(file);
            // solve the slider puzzle
            Board initial = new Board(tiles);
            Solver solver = new Solver(initial);
            if(!solver.isSolvable())
            {
                StdOut.println("No solution possible");
            } else {
                StdOut.println("Minimum number of moves = " + solver.moves());
                for(Board board : solver.solution())
                    StdOut.println(board);
            }
        }
    }
}
