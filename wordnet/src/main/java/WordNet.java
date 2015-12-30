import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * A Wordnet is a lexical database of words and relationships among them.
 *
 * A synset is a set of words and a short description describing their semantic relationship.
 *
 * A WordNet also stores a semantic relationship between seynsets.  One such relationship
 * is-a is defined as follows:
 *
 * A hyponym (more specific synset) to a hypernym (more general synset)
 *
 * For example: a plant organ is a hypernym of a carrot, and a plant organ is a hypernym of plant root.
 *
 * This WordNet expects two files, one containing the synsets, and the other describing relationships between.
 *
 * These relationship relationships form a Directed Acyclic Graph (DAG).  The WordNet constructed 
 * from these two files is expected to be a rooted DAG, where a single synset is the vertex that is the 
 * ancestor of every other vertex.
 *
 */
public class WordNet {
    private Set<Synset> list_synset = new LinkedHashSet<WordNet.Synset>();      // set of all synsets
    private Set<String> set_nouns = new LinkedHashSet<>();                      // set of all words in all synsets

    private Map<Integer, Synset> lookup_noun = new LinkedHashMap<>();           // maps synset id -> synset obj
	private Map<String, List<Integer>> word_to_synset = new LinkedHashMap<>();  // maps word -> list of synset ids word appears in
	
    /* Stores the v -> w edge representing the relationship that
     * w is a hypernym of v.  That is of the two synsets v and w, 
     * w is a more specific synset of synset v.
     */
    private Digraph relation_is_a;

    /*
     * Computes common ancestors
     */
    private SAP sap;


    /**
     * A single synset entry.  
     * The id is guarenteed to be unique, however A word in a synset is not.
     * They appear a line at a time, and are separated by a comma.  A space seperates
     * a word from another in the word set.
     */
    private class Synset {
        private int id;
        private String words;
        private String desc;

        /**
         * Constructor for Synset.
         * id is unqiue.
         * words are space separated, with each not unique among synsets
         * 
         * @param id unique id representing this synset
         * @param words a string containing words
         * @param desc description of synset
         */
        private Synset(int id, String words, String desc) {
            this.id = id;
            this.words = words;
            this.desc = desc;
        }

        /**
         * @return this synset's unique id
         */
        public int getId() {
            return id;
        }

        /**
         * @return this synset's words string
         */
        public String getWords() {
            return words;
        }

        /**
         * @return this synset's description
         */
        public String getDesc() {
            return desc;
        }

        // TODO: this synset is used in hash, where is hashCode ??
    }
    
    /** 
     * Constructor takes the name of the two input files synsets hypernyms.
     * Both text files containg an entry per line.
     *
     * The Synset file takes on form of:
     * <id>,<words>,<description>
     *
     * <id> is unique, and increasing
     * The words within <words> are separated by a space
     * The string <description> may contain the comma token
     *
     * The hypernyms file takes on the form of:
     * <hyponym id>,<hypernym id>[, hypernym id]...
     *
     * Where the <hyponym> is the synset id of the more general
     * synset and has a relationship to each following synset id as
     * being a hypernym of the first.
     *
     *
     * @param synsets filename containing synsets
     * @param hypernyms filename containing hypernyms
     */
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

    /** Verify graph a rooted diagraph.
     * Iterates over all vertexes in the digraph.  If there is more than one vertex with an indegree
     * of zero, then the diagraph has more than one root.
     * @param graph to check for rooted
     * @throws IllegalArgumentException if graph is not a rooted digraph
     */
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

	/** @return all WordNet nouns */
    public Iterable<String> nouns() {
        return set_nouns;
    }

    /** @return is the word a WordNet noun? */
    public boolean isNoun(String word) {
        throwIfNull(word);
        return set_nouns.contains(word);
    }

    /**
     * Computes the distance between word nounA and nounB.
     *
     * Defines the semantic relatedness of two nouns.  
     *
     * distance( A , B ) = distance is the minimum length of any ancestral path
     * between any synset v of A and any synset of w of B.
     *
     * @param nounA word to check distance
     * @param nounB word to check distance
     */
    public int distance(String nounA, String nounB) {
        throwIfNull(nounA);
        throwIfNull(nounB);
        throwIfNotWord(nounA);
        throwIfNotWord(nounB);
        
        return sap.length(word_to_synset.get(nounA), word_to_synset.get(nounB));
    }

    /** Shortest Ancestral Path between nounA and nounB.
     * Traverses the Diagraph to find a common ancestor of nounA and nounB.  Guarenteed
     * to be the shortest traversal distance.
     *
     * @return synset id of ancestor of nounA and nounB that is of the shortest distance.
     */
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

    /** @throws NullPointerException if @param arg is null */
    private void throwIfNull(String arg) {
        if (arg == null) {
            throw new NullPointerException("Null argument.");
        }
    }

    /** @throws IllegalArgumentException if @param arg is not a noun known in the WordNet */
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
