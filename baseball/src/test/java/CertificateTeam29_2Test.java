import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam29_2Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams29.txt";
	final private static String TEAM_NAME = "Golden_State";
	final private static String CERT_STRING = "Atlanta Boston Chicago Cleveland Dallas Denver";

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
