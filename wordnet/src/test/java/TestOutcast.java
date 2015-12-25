import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;


public class TestOutcast {
	
	WordNet wordnet;

	@Before
	public void load_wordnet()
	{
		URL url_syn = this.getClass().getClassLoader().getResource("synsets.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms.txt");
		wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}
	
	@Test
	public void test_spec_outcast5()
	{
		Outcast outcast = new Outcast(wordnet);
		String result = outcast.outcast(new String[]{"horse","zebra","cat","bear","table"});
		assertEquals("Expected:","table",result);
	}

	@Test
	public void test_spec_outcast8()
	{
		Outcast outcast = new Outcast(wordnet);
		String result = outcast.outcast(new String[]{"water","soda","bed","orange_juice","milk","apple_juice","tea","coffee"});
		assertEquals("Expected:","bed",result);
	}	

	@Test
	public void test_spec_outcast11()
	{
		Outcast outcast = new Outcast(wordnet);
		String result = outcast.outcast(new String[]{"apple","pear","peach","banana","lime","lemon","blueberry","strawberry","mango","watermelon","potato"});
		assertEquals("Expected:","potato",result);
	}	

	
}
