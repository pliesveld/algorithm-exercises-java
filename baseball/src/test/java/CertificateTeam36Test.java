import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam36Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams36.txt";
	final private static String TEAM_NAME = "Team21";
	final private static String CERT_STRING = "Team18 Team20 Team22 Team23";

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
