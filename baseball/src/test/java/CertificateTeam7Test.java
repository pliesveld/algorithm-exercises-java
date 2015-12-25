import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam7Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams7.txt";
	final private static String TEAM_NAME = "Ireland";
	final private static String CERT_STRING = "U.S.A. France Germany";

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
