import static org.junit.Assert.*;

import org.junit.Test;

public class CertificateTeam8Test extends AbstractCertificateTest {
	
	final private static String TEST_FILENAME = "teams8.txt";
	final private static String TEAM_NAME = "Harvard";
	final private static String CERT_STRING = "Brown Columbia Cornell Dartmouth Penn Princeton";

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
