import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam42Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams42.txt";
	final private static String TEAM_NAME = "Team6";
	final private static String CERT_STRING = "Team0 Team18 Team19 Team21";

	@Override
	String getTeamFile() {
		return TEST_FILENAME;
	}

	@Override
	String getTeamEliminated() {
		return TEAM_NAME;
	}

	@Override
	String getCertificateOfEliminationString() {
		return CERT_STRING;
	}

}
