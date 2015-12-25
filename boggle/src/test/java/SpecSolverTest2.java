import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SpecSolverTest2 {
	
	private static final String DICT_FILENAME = "dictionary-algs4.txt";
	private static final String BOARD_FILENAME = "board-q.txt";
	
	public String getDict()
	{
		//InputStream is = SpecSolverTest.class.getResourceAsStream(DICT_FILENAME);
		URL url = this.getClass().getResource(DICT_FILENAME);
		assertNotNull(url);
		assertTrue(new File(url.getPath()).exists());
		return url.getPath();
	}
	
	public String getBoard()
	{
		//InputStream is = SpecSolverTest.class.getResourceAsStream(DICT_FILENAME);
		URL url = this.getClass().getResource(BOARD_FILENAME); 
				
		assertNotNull(url);
		assertTrue(new File(url.getPath()).exists());
		return url.getPath();
	}
	
	@Test
	public void resolve_test_files()
	{
		String dict_filename = getDict();
		String board_filename = getBoard();
		
	}

	@Test(timeout=15000)
	public void test() {
		String dict_filename = getDict();
		String board_filename = getBoard();
		
        In in = new In(dict_filename);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(board_filename);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        assertEquals("Score",84,score);
	}

}
