import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam5Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams5.txt";
	final private static String TEAM_NAME = "Detroit";
	final private static String CERT_STRING = "New_York Baltimore Boston Toronto";

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
