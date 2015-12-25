import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;


public class TestWordNetLoad {
	
	WordNet wordnet;

	@Test
	public void load_wordnet_main()
	{
		URL url_syn = this.getClass().getClassLoader().getResource("synsets.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms.txt");
		assertNotNull("Could not load symset",url_syn);
		assertNotNull("Could not load hypernyms",url_hyp);
		wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}

	@Test
	public void load_wordnet15()
	{
		URL url_syn = this.getClass().getClassLoader().getResource("synsets15.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms15Path.txt");
		assertNotNull("Could not load symset",url_syn);
		assertNotNull("Could not load hypernyms",url_hyp);
		wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}

	@Test
	public void load_wordnet6_two_ancestors()
	{
		URL url_syn = this.getClass().getClassLoader().getResource("synsets6.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms6TwoAncestors.txt");
		assertNotNull("Could not load symset",url_syn);
		assertNotNull("Could not load hypernyms",url_hyp);
		wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}

	@Test
	public void load_wordnet11_ambiguous_ancestor()
	{
		URL url_syn = this.getClass().getClassLoader().getResource("synsets11.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms11AmbiguousAncestor.txt");
		assertNotNull("Could not load symset",url_syn);
		assertNotNull("Could not load hypernyms",url_hyp);
		wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}
	
	
	@Test
	public void load_wordnet11_manypaths_one_ancestor()
	{
		URL url_syn = this.getClass().getClassLoader().getResource("synsets11.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms11ManyPathsOneAncestor.txt");
		assertNotNull("Could not load symset",url_syn);
		assertNotNull("Could not load hypernyms",url_hyp);
		wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}
	
	@Test
	public void load_wordnet8_many_ancestors()
	{
		URL url_syn = this.getClass().getClassLoader().getResource("synsets8.txt");
		URL url_hyp = this.getClass().getClassLoader().getResource("hypernyms8ManyAncestors.txt");
		assertNotNull("Could not load symset",url_syn);
		assertNotNull("Could not load hypernyms",url_hyp);
		wordnet = new WordNet(url_syn.toString(),url_hyp.toString());
	}
}
