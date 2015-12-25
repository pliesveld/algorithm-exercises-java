import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

import edu.princeton.cs.algs4.StdOut;

public abstract class AbstractTeamElimination {

	BaseballElimination be;
	
	abstract String getTestFilename();


	protected String getTeamFilePath(String filepath) {
		URL url = this.getClass().getResource(filepath);
		return url.getPath();
	}

}
