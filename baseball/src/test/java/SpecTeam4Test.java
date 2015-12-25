import static org.junit.Assert.*;

import org.junit.Test;

public class SpecTeam4Test extends AbstractTeamElimination {

	@Override
	String getTestFilename() {
		return "teams4.txt";
	}

	@Test
	public void test_teams() {
		String filename = getTeamFilePath(getTestFilename());
		 be = new BaseballElimination(filename);

		 assertEquals(4,be.numberOfTeams());
		 
		 boolean[] expected_eliminated = {
				 false,
				 true,
				 false,
				 true
		 };
		 
		 int i = 0;
		 for(String team : be.teams())
		 {
			 String message = String.format("Team %s eliminated", team);
			 assertEquals(message,expected_eliminated[i],be.isEliminated(team));
			 i++;
		 }
		
	}

}
