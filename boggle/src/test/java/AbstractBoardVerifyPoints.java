import java.net.URISyntaxException;
import java.net.URL;
import static org.junit.Assert.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;



import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public abstract class AbstractBoardVerifyPoints {

	abstract String getBoardFilename();
	abstract int getExpectedPoints();
	
	static private String[] dict_words;
	static private BoggleSolver bs;
	
	public String getDictionaryFilename()
	{
		return "dictionary-yawl.txt";
	}

	private Path loadTestResource(String filename)
	{
		URL url = this.getClass().getClassLoader().getResource(filename);
		assertNotNull(url);
		try {
			return Paths.get(url.toURI());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}
		return null;
	}
	
	final void loadDictionaryBoard()
	{
		String dict_filename = getDictionaryFilename();
		Path path_dict = loadTestResource(dict_filename);
		
		In in = new In(path_dict.toAbsolutePath().toString());
		assertTrue(in.exists());
		assertTrue(!in.isEmpty());
		dict_words = in.readAllLines();
		bs = new BoggleSolver(dict_words);
	}
	
	@Test
	public void verify_board_point_accumulation()
	{
		if(bs == null)
		{
			loadDictionaryBoard();
		}
		assertNotNull(bs);
		
		
		String board_name = getBoardFilename();
		Path board = loadTestResource(board_name);
		assertTrue(board.toFile().exists());
		BoggleBoard bb = new BoggleBoard(board.toString());

		int points = 0;
		
		for(String word : bs.getAllValidWords(bb))
		{
			points += bs.scoreOf(word);
		}
		int expected_points = getExpectedPoints();
		assertEquals("Expected point count for " + board.getFileName(),expected_points,points);
	}
	
}
