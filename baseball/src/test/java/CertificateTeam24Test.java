import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam24Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams24.txt";
	final private static String TEAM_NAME = "Team13";
	final private static String CERT_STRING = "Team14 Team15 Team17 Team20 Team22";

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
