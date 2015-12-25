import static org.junit.Assert.*;

import org.junit.Test;

public class SpecTeam5Test extends AbstractTeamElimination {

	@Override
	String getTestFilename() {
		return "teams5.txt";
	}

	@Test
	public void test_teams() {
		String filename = getTeamFilePath(getTestFilename());
		 be = new BaseballElimination(filename);

		 assertEquals(5,be.numberOfTeams());
		 
		 boolean[] expected_eliminated = {
				false,
				false,
				false,
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
