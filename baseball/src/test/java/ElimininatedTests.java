
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

public class ElimininatedTests {

	protected String getTeamFilePath(String filepath) {
		URL url = this.getClass().getResource(filepath);
		assertNotNull("Couldn't find test case: " + filepath,url);
		return url.getPath();
	}
	
	BaseballElimination loadBaseballFile(String filename)
	{
		BaseballElimination be = new BaseballElimination(getTeamFilePath(filename));
		return be;
	}
	
	@Test
	public void test_team8() {
		final String TEST_FILENAME = "teams8.txt";
		final String TEAM_NAME = "Princeton";
		final boolean EXPECTED_ELIMINATED = false;
		
		BaseballElimination be = loadBaseballFile(TEST_FILENAME);
		boolean found = false;
		for(String team : be.teams())
			if(team.equals(TEAM_NAME))
				found = true;
			
		assertTrue("Team not found: "+ TEAM_NAME,found);
		String message = String.format("in schedule %s, team %s",TEST_FILENAME,TEAM_NAME);
		assertEquals(message,EXPECTED_ELIMINATED, be.isEliminated(TEAM_NAME));
	}

	@Test
	public void test_team12() {
		final String TEST_FILENAME = "teams12.txt";
		final String TEAM_NAME = "Japan";
		final boolean EXPECTED_ELIMINATED = true;
		
		BaseballElimination be = loadBaseballFile(TEST_FILENAME);
		boolean found = false;
		for(String team : be.teams())
			if(team.equals(TEAM_NAME))
				found = true;
			
		assertTrue("Team not found: "+ TEAM_NAME,found);
		String message = String.format("in schedule %s, team %s",TEST_FILENAME,TEAM_NAME);
		assertEquals(message,EXPECTED_ELIMINATED, be.isEliminated(TEAM_NAME));
	}

	
	@Test
	public void test_team24() {
		final String TEST_FILENAME = "teams24.txt";
		final String TEAM_NAME = "Team13";
		final boolean EXPECTED_ELIMINATED = true;
		
		BaseballElimination be = loadBaseballFile(TEST_FILENAME);
		boolean found = false;
		for(String team : be.teams())
			if(team.equals(TEAM_NAME))
				found = true;
			
		assertTrue("Team not found: "+ TEAM_NAME,found);
		String message = String.format("in schedule %s, team %s",TEST_FILENAME,TEAM_NAME);
		assertEquals(message,EXPECTED_ELIMINATED, be.isEliminated(TEAM_NAME));
	}

	@Test
	public void test_team29() {
		final String TEST_FILENAME = "teams29.txt";
		final String TEAM_NAME = "Golden_State";
		final boolean EXPECTED_ELIMINATED = true;
		
		BaseballElimination be = loadBaseballFile(TEST_FILENAME);
		boolean found = false;
		for(String team : be.teams())
			if(team.equals(TEAM_NAME))
				found = true;
			
		assertTrue("Team not found: "+ TEAM_NAME,found);
		String message = String.format("in schedule %s, team %s",TEST_FILENAME,TEAM_NAME);
		assertEquals(message,EXPECTED_ELIMINATED, be.isEliminated(TEAM_NAME));
	}

	@Test
	public void test_team30() {
		final String TEST_FILENAME = "teams30.txt";
		final String TEAM_NAME = "Team18";
		final boolean EXPECTED_ELIMINATED = true;
		
		BaseballElimination be = loadBaseballFile(TEST_FILENAME);
		boolean found = false;
		for(String team : be.teams())
			if(team.equals(TEAM_NAME))
				found = true;
			
		assertTrue("Team not found: "+ TEAM_NAME,found);
		String message = String.format("in schedule %s, team %s",TEST_FILENAME,TEAM_NAME);
		assertEquals(message,EXPECTED_ELIMINATED, be.isEliminated(TEAM_NAME));
	}
	

	@Test
	public void test_team32() {
		final String TEST_FILENAME = "teams32.txt";
		final String TEAM_NAME = "Team25";
		final boolean EXPECTED_ELIMINATED = true;
		
		BaseballElimination be = loadBaseballFile(TEST_FILENAME);
		boolean found = false;
		for(String team : be.teams())
			if(team.equals(TEAM_NAME))
				found = true;
			
		assertTrue("Team not found: "+ TEAM_NAME,found);
		String message = String.format("in schedule %s, team %s",TEST_FILENAME,TEAM_NAME);
		assertEquals(message,EXPECTED_ELIMINATED, be.isEliminated(TEAM_NAME));
	}
	
	@Test
	public void test_team36() {
		final String TEST_FILENAME = "teams36.txt";
		final String TEAM_NAME = "Team21";
		final boolean EXPECTED_ELIMINATED = true;
		
		BaseballElimination be = loadBaseballFile(TEST_FILENAME);
		boolean found = false;
		for(String team : be.teams())
			if(team.equals(TEAM_NAME))
				found = true;
			
		assertTrue("Team not found: "+ TEAM_NAME,found);
		String message = String.format("in schedule %s, team %s",TEST_FILENAME,TEAM_NAME);
		assertEquals(message,EXPECTED_ELIMINATED, be.isEliminated(TEAM_NAME));
	}
	
	
	@Test
	public void test_team42() {
		final String TEST_FILENAME = "teams42.txt";
		final String TEAM_NAME = "Team6";
		final boolean EXPECTED_ELIMINATED = true;
		
		BaseballElimination be = loadBaseballFile(TEST_FILENAME);
		boolean found = false;
		for(String team : be.teams())
			if(team.equals(TEAM_NAME))
				found = true;
			
		assertTrue("Team not found: "+ TEAM_NAME,found);
		String message = String.format("in schedule %s, team %s",TEST_FILENAME,TEAM_NAME);
		assertEquals(message,EXPECTED_ELIMINATED, be.isEliminated(TEAM_NAME));
	}
	
}
