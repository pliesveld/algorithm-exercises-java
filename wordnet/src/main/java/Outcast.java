import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Given a wordnet, computes outcasts of a list of words.
 */
public class Outcast {

	private WordNet wordnet;
	
    /**
     * Outcast constructor, WordNet to compute outcast from
     * @param wordnet
     */
    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
    	this.wordnet = wordnet;
    }

    /**
     * Given a list of words, computes the distance among them
     * and returns the word that has the lowest level of connectivity among
     * the words supplied.
     *
     * @param nouns array of words to compute outcast from
     */
    public String outcast(String[] nouns)
    {
    	double max = 0.0;
    	int max_i = -1;

    	for(int i = 0; i < nouns.length;i++)
    	{

    		double sum = 0.0;
        	for(int j = 0; j < nouns.length;j++)
        	{
        		if(i == j)
        			continue;
        		sum += wordnet.distance(nouns[i], nouns[j]);
        		
        	}
        	
        	if(sum > max)
        	{
        		max = sum;
        		max_i = i;
        	}

    	}
    	
    	return nouns[max_i];
    }

    public static void main(String[] args)  // see test client below
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
