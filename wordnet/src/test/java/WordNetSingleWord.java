import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Test;

public class WordNetSingleWord {

	static final String SYN_FILENAME = "synsets.txt";
	static final String HYP_FILENAME = "hypernyms.txt"; 
	
	String getSynPath()
	{
		URL url = this.getClass().getResource(SYN_FILENAME);
		assertNotNull(url);
		assertTrue(new File(url.getPath()).exists());
		return url.getPath();
	}
	
	String getHypPath()
	{
		URL url = this.getClass().getResource(HYP_FILENAME);
		assertNotNull(url);
		assertTrue(new File(url.getPath()).exists());
		return url.getPath();
	}

	@Test
	public void test() {
		String synfile = getSynPath();
		String hypfile = getHypPath();
		
		WordNet wn = new WordNet(synfile,hypfile);
		assertTrue(wn.isNoun("slag_code"));
	}

}
