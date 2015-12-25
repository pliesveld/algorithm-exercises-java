import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam32Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams32.txt";
	final private static String TEAM_NAME = "Team25";
	final private static String CERT_STRING = "Team0 Team6 Team8 Team11 Team26";

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