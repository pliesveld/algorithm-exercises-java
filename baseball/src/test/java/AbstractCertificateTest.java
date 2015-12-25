

import static org.junit.Assert.*;


import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public abstract class AbstractCertificateTest {


	protected String getTeamFilePath(String filepath) {
		URL url = this.getClass().getResource(filepath);
		assertNotNull("Couldn't find test case: " + filepath,url);
		return url.getPath();
	}
	
	protected BaseballElimination loadBaseballFile(String filename)
	{
		BaseballElimination be = new BaseballElimination(getTeamFilePath(filename));
		return be;
	}
	
	abstract String getTeamFile();
	abstract String getTeamEliminated();
	abstract String getCertificateOfEliminationString();
	
	
	@Test
	public void test_team_certificate() {
		final String TEST_FILENAME = getTeamFile();
		final String TEAM_NAME = getTeamEliminated();
		final boolean EXPECTED_ELIMINATED = true;
		
		final String CERT_STRING = getCertificateOfEliminationString();
		Set<String> EXPECTED_CERT = new LinkedHashSet<>();
		
		String[] CERTS_ARRAY = CERT_STRING.split(" ");
		EXPECTED_CERT.addAll(Arrays.asList(CERTS_ARRAY));
		EXPECTED_CERT.remove(" "); // remove space, just incase
		
		assertEquals("Expected Cert set has wrong length.",CERTS_ARRAY.length,EXPECTED_CERT.size());
		
		BaseballElimination be = loadBaseballFile(TEST_FILENAME);
		boolean found = false;
		
		List<String> all_teams = new ArrayList<>(be.numberOfTeams());
		for(String team : be.teams())
		{
			if(team.equals(TEAM_NAME))
				found = true;
			
			all_teams.add(team);
		}
		
		assertTrue("Team not found: "+ TEAM_NAME,found);
		
		for(String team : EXPECTED_CERT)
		{
			if(!all_teams.contains(team))
			{
				String message = String.format("in schedule %s, expected to see team '%s'",TEST_FILENAME,team);
				fail(message);
			}
		}

		{
			String message = String.format("in schedule %s, team %s eliminated ",TEST_FILENAME,TEAM_NAME);
			assertEquals(message,EXPECTED_ELIMINATED,be.isEliminated(TEAM_NAME));
		}
		
		Set<String> CERT = new LinkedHashSet<>();
		for(String t : be.certificateOfElimination(TEAM_NAME))
		{
			CERT.add(t);
		}
		
		if(!EXPECTED_CERT.equals(CERT))
		{
			String expected = String.join(" ", EXPECTED_CERT);
			String actual = String.join(" ", CERT);
			String message = String.format("in schedule %s, team %s expected certificate of elimination { %s } Actual certificate of elimination { %s }",
					TEST_FILENAME,TEAM_NAME,
					expected,
					actual);
			fail(message);
			
		}

	}

}
