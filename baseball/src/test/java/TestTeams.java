import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

import edu.princeton.cs.algs4.StdOut;

public class TestTeams {
	private final String TEST_FILENAME = "teams24.txt";
	
	private String getTestFilename()
	{
		return TEST_FILENAME;
	}

	private String getTeamFilePath(String filepath) {
		URL url = this.getClass().getResource(filepath);
		return url.getPath();
	}
	
	
	@Test
	public void test_teams() {
		String filename = getTeamFilePath(getTestFilename());
		BaseballElimination be = new BaseballElimination(filename);
		
		assertEquals("Should've loaded 24 teams",24,be.numberOfTeams());
		
		int cnt = 0;
		for(String team : be.teams())
		{
			cnt++;
		}
		
		assertEquals("Should've arrayed 24 teams",24,cnt);
	}

}
