import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

import edu.princeton.cs.algs4.StdOut;

public class SingleTeamTest {
	private final String TEST_FILENAME = "teams1.txt";
	
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
		
		assertEquals("Should've loaded 1 teams",1,be.numberOfTeams());
		
		int cnt = 0;
		for(String team : be.teams())
		{
			StdOut.println("|" + team + "|");
			cnt++;
		}

		assertEquals("Iterations through teams() should've been same as reported from numberOfTeams",be.numberOfTeams(),cnt);
		assertEquals("Number of teams iterated",1,cnt);

	}

}
