import static org.junit.Assert.*;

import org.junit.Test;

public class Team12AllGamesTest extends AbstractTeamElimination {

	@Override
	String getTestFilename() {
		return "teams12-allgames.txt";
	}

	@Test
	public void test_teams() {
		String filename = getTeamFilePath(getTestFilename());
		 be = new BaseballElimination(filename);

		 assertEquals(12,be.numberOfTeams());
		 
		 boolean[] expected_eliminated = {

		true,
		true,
		true,
		true,
		
		false,
		true,
		true,
		true,
		
		true,
		true,
		true,
		false

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
