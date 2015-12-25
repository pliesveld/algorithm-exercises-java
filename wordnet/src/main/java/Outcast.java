import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

	private WordNet wordnet;
	
    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
    	this.wordnet = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
    //	double[] distances = new double[nouns.length];
    	
    	double max = 0.0;
    	int max_i = -1;

    	for(int i = 0; i < nouns.length;i++)
    	{

    		//distances[i] = 0.0;
    		double sum = 0.0;
        	for(int j = 0; j < nouns.length;j++)
        	{
        		if(i ==j)
        			continue;
        		sum += wordnet.distance(nouns[i], nouns[j]);
        		
        	}
        	//distances[i] = sum;
        	
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
