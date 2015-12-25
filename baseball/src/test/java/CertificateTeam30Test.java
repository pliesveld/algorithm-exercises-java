import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam30Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams30.txt";
	final private static String TEAM_NAME = "Team7";
	final private static String CERT_STRING = "Team10";

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