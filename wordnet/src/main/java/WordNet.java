import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class WordNet {
    private Set<Synset> list_synset = new LinkedHashSet<WordNet.Synset>();
    private Set<String> set_nouns = new LinkedHashSet<>();

    private Map<Integer, Synset> lookup_noun = new LinkedHashMap<>();
	private Map<String, List<Integer>> word_to_synset = new LinkedHashMap<>();
	
    private Digraph relation_is_a;
    private SAP sap;


    private class Synset {
        private int id;
        private String words;
        private String desc;

        private Synset(int id, String words, String desc) {
            this.id = id;
            this.words = words;
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public String getWords() {
            return words;
        }

        public String getDesc() {
            return desc;
        }
    }
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        throwIfNull(synsets);
        throwIfNull(hypernyms);

        In inFile_synsets = new In(synsets);

        if (!inFile_synsets.exists()) {
            throw new IllegalArgumentException("Invalid file: " + synsets);
        }

        In inFile_hypernyms = new In(hypernyms);

        if (!inFile_hypernyms.exists()) {
            throw new IllegalArgumentException("Invalid file: " + hypernyms);
        }


        int regex_fail = 0;
        int last_id = 0;
        int line_no = 0;
        while (inFile_synsets.hasNextLine()) {
        	line_no++;
            String line = inFile_synsets.readLine();

            Pattern synset_pattern = Pattern.compile("[,]");

            String[] lines_match = synset_pattern.split(line);
            if (lines_match.length < 3) {
            	throw new IllegalArgumentException("Line has must have atleast two commas. lineno: " + line_no);
            }

            Pattern word_pattern = Pattern.compile("[ ]");

            String[] words = word_pattern.split(lines_match[1]);

            int id = Integer.valueOf(lines_match[0]);

            String desc = lines_match[2];
            if(id < last_id)
            {
            	throw new IllegalArgumentException("Id values must be increasing in value" + line_no);
            }

            for (String word : words) {
            	if(!set_nouns.contains(word))
            	{
            		set_nouns.add(word);
            		List<Integer> list_new = new LinkedList<>();
            		list_new.add(id);
            		word_to_synset.put(word,list_new);
            	} else {
            		List<Integer> list_ids = word_to_synset.get(word);
            		list_ids.add(id);
            	}
            }

            Synset syn = new Synset(id, lines_match[1], desc);
            list_synset.add(syn);
            lookup_noun.put(id, syn);
            last_id = id;

           /*
           StdOut.printf("Id: %relation_is_a\n",id);
           StringBuilder sb = new StringBuilder();
           for(String word : words)
           {
               sb.append(word + " ");
           }
           StdOut.println("Words: " + sb.toString());
           */
        }

        relation_is_a = new Digraph(last_id + 1);

        assert regex_fail == 0;
        if(regex_fail != 0)
        	throw new IllegalArgumentException("Failed to parse synsets file.  errors=" + regex_fail);
        
        int hypernyms_fail = 0;

        while (inFile_hypernyms.hasNextLine()) {
        	
        	String line = inFile_hypernyms.readLine();
        	
        	Pattern comma_pattern = Pattern.compile("[,]");
        	
        	String[] str_symsids = comma_pattern.split(line);
        	
            if (str_symsids.length <= 0) {
                hypernyms_fail++;
                continue;
            }
            
            int source_id = -1;

            for (String str_id : str_symsids) {
                int id = Integer.parseInt(str_id);
                
                if(source_id == -1)
                {
                	source_id = id;
                	continue;
                }
                relation_is_a.addEdge(source_id, id);
            }
        }
        assert hypernyms_fail == 0;
        if(hypernyms_fail != 0)
        	throw new IllegalArgumentException("Failed to parse hypernyms file.  errors=" + hypernyms_fail);


        //verifyNoMultiRoot(relation_is_a);
        
        sap = new SAP(relation_is_a);
    }

    private void verifyNoMultiRoot(Digraph graph) {
    	int V = graph.V();
    	
    	if(V == 0 || V == 1)
    		return;
    	
    	boolean found_root = false;
    	int root_id = -1;
    	
    	for(int v = 0; v < V;v++)
    	{
    		if(!lookup_noun.containsKey(v))
    			continue;
    		
    		if(graph.indegree(v) == 0)
    		{
    			if(found_root)
    			{
        			Synset s = lookup_noun.get(v);
        			int other_root_id = s.id;
        			String message = String.format("More than one root found.  First = %d Other = %d.  Graph is not a DAG.",root_id,other_root_id);
    				throw new IllegalArgumentException(message);
    			}
    			
    			if(graph.outdegree(v) == 0)
    			{
    				Synset s = lookup_noun.get(v);
    				int comp_id = s.id;
    				throw new IllegalArgumentException("Found non-connected component. id=" + comp_id +" Graph is not a DAG");
    			}
    				
    			found_root = true;
    			Synset s = lookup_noun.get(v);
    			root_id = s.id;
    		}
    		
    	}
    	
		
	}

	// returns all WordNet nouns
    public Iterable<String> nouns() {
        return set_nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        throwIfNull(word);
        return set_nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        throwIfNull(nounA);
        throwIfNull(nounB);
        throwIfNotWord(nounA);
        throwIfNotWord(nounB);
        
        return sap.length(word_to_synset.get(nounA), word_to_synset.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        throwIfNull(nounA);
        throwIfNull(nounB);
        throwIfNotWord(nounA);
        throwIfNotWord(nounB);
        
        int ret = sap.ancestor(word_to_synset.get(nounA), word_to_synset.get(nounB));
        
        if(ret != -1)
        {
        	Synset synset = lookup_noun.get(ret);
        	return synset.getWords();
        }
        
        return null;
    }

    private void throwIfNull(String arg) {
        if (arg == null) {
            throw new NullPointerException("Null argument.");
        }
    }

    private void throwIfNotWord(String arg) {
        if (!isNoun(arg)) {
            throw new IllegalArgumentException("Argument not in WordNet: " + arg);
        }
    }


    // do unit testing of this class
    public static void main(String[] args) {
//        StdOut.println(System.getProperty("user.dir"));
        URL url_s = WordNet.class.getResource("synsets.txt");
        URL url_h = WordNet.class.getResource("hypernyms.txt");
        assert url_s != null;
        assert url_h != null;
//        StdOut.println("Resource: " + url);
        WordNet wn = new WordNet(url_s.getPath(), url_h.getPath());
    }


}
