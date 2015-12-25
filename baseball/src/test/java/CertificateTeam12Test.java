import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam12Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams12.txt";
	final private static String TEAM_NAME = "Japan";
	final private static String CERT_STRING = "Poland Russia Brazil Iran";

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