import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class TestInvalidWordNet {

	@Test(expected=IllegalArgumentException.class)
	public void invalid_dag_two_roots()
	{
		URL url_syn = this.getClass().getClassLoader().getResource("synsets3.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms3InvalidTwoRoots.txt");
		assertNotNull("Could not find test resource synset",url_syn);
		assertNotNull("Could not find test resource hypernyms",url_hyp);
		WordNet wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void invalid_dag_cycle()
	{
		
		URL url_syn = this.getClass().getClassLoader().getResource("synsets3.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms3InvalidCycle.txt");
		assertNotNull("Could not find test resource synset",url_syn);
		assertNotNull("Could not find test resource hypernyms",url_hyp);
		WordNet wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void invalid_dag_two_roots2()
	{
		
		URL url_syn = this.getClass().getClassLoader().getResource("synsets6.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms6InvalidTwoRoots.txt");
		assertNotNull("Could not find test resource synset",url_syn);
		assertNotNull("Could not find test resource hypernyms",url_hyp);
		WordNet wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void invalid_dag_cycle2()
	{
		
		URL url_syn = this.getClass().getClassLoader().getResource("synsets6.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms6InvalidCycle.txt");
		assertNotNull("Could not find test resource synset",url_syn);
		assertNotNull("Could not find test resource hypernyms",url_hyp);
		WordNet wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalid_dag_cycle_and_path()
	{
		
		URL url_syn = this.getClass().getClassLoader().getResource("synsets6.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms6InvalidCycle+Path.txt");
		assertNotNull("Could not find test resource synset",url_syn);
		assertNotNull("Could not find test resource hypernyms",url_hyp);
		WordNet wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}
	
}