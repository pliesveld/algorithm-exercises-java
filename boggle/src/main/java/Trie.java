import edu.princeton.cs.algs4.Queue;

/**
 * A Trie is a data-structure used in string processing algorithms.  It can be used to lookup 
 * and iterate through words that have start with a common string.
 *
 * A Trie is a tree data structure where a node has R links, where R is the size of the alphabet
 * ( letters allowed in constructing the string )
 *
 * Each node has a parent node, except for the root.   The root is the start of the prefix tree
 * and has no value associated with it.  Each node that has a value, forms a word from the root to the current node.
 * In this implementation, -1 represents the absense of a value.
 */
public class Trie
{
        /*
         * Internal node has an alphabet of A - Z.  26 total letters.
         * Starting at the letter 'A' being represented by the next[0] 
         * reference.
         */
    private static class Node
    {
        private int val = -1;
        private Node[] next = new Node[26];

        public Node()
        {
        }

        /**
         * Constructor taking as a parameter the constructing node's
         * parent node.
         * @param parent this node's parent
         * @param ch the index from the parent node's next this node referes to
         */
        public Node(Node parent, char ch)
        {
            parent.assignNode(this,ch);
        }

        /**
         * Assigns the next pointer indexed by idex to x
         * @param x child
         * @param idx index slot to assign
         */
         private void assignNode(Node x, char idx)
        {
            if(idx < 0 || idx > 26)
                throw new IllegalArgumentException("Invalid index: " + idx); 

            if(next[idx] != null)
                throw new IllegalArgumentException("Argument letter already used: " + idx); 

            
            next[idx] = x;
        }  

    }

    /**
     * Stores string word with value val in the trie.  Cannot store -1, as it represents the absence of a value.
     * @param word word to store
     * @param val to store.
     */
    public void put(String word,int val)
    {
        if(val == -1)
                throw new IllegalArgumentException("Implementation cannot store value -1");

        put(root,word,val,0);
    }

    /**
     * Retrieves value of word stored in trie.
     * @param word to lookup
     */
    public int get(String word)
    {
        Node x = get(root,word,0);

        if(x == null)
            return -1;
        return x.val;
    }

    /**
     * Helper function.  Advances Node x along it's
     * next links according to the letters encountered
     * in word starting at offset d.
     *
     * @param x Node to advance
     * @param word to lookup by letter
     * @param d offset in word to lookup
     *
     * @return null if word is not found.
     */
    private Node get(Node x,String word,int d)
    {
    	final int len = word.length();
    	
    	while(d <= len)
    	{
	        if(x == null)
	            return null;
	
	        if(len == d)
	            return x;
	
	        char ch = (char)(word.charAt(d) - 'A');
	        x = x.next[ch];
	        d++;
    	}

    	return null;
    }



    /**
     * Helper function.  Advances Node x along it's
     * next links according to the letters encountered
     * in word starting at offset d.  If a link is null,
     * then a new node is created for each link containing a null
     * reference.  When nodes for string word are created letter-by-
     * letter, value val is stored.
     *
     * @param x Node to advance
     * @param word to lookup by letter
     * @param val value to store
     * @param d offset in word to lookup
     *
     * @return null if word is not found.
     */
    private Node put(Node x, String word, int val, int d)
    {
        if(x == null)
            return null;

        if(word.length() == d)
        {
            x.val = val;
            return x;
        }

        char word_ch = word.charAt(d);
        char ch = (char)(word_ch - 'A');
        assert ch >= 0 && ch <= 25;

        if(x.next[ch] == null)
        {
            Node y = new Node(x,ch);
            x.next[ch] = y;
        }

        return put(x.next[ch],word,val,d+1);
    }

    /**
     * Iterates through all words stored
     */
    public Iterable<String> keys()
    {
    	return keysWithPrefix("");
    }

    /**
     * Do any words exists having starting with
     * the string pre.
     * @param pre prefix to search
     * @return prefix found
     */
    public boolean anyKeyHasPrefix(final String pre)
    {
    	Node x = get(root,pre,0);
    	if(x == null)
    		return false;
    	for(char c = 0; c < 26;c++)
    	{
    		if(x.next[c] != null)
    			return true;
    	}
    	return x.val != -1;
    }

    /**
     * Iterate any words exists having starting with
     * the string pre.
     * @param pre prefix to search
     * @return iteratable collection of words
     */
   public Iterable<String> keysWithPrefix(final String pre) {

    	Queue<String> q = new Queue<>();
    	collect(get(root,pre,0),pre,q);
    	return q;
	}
    

   /**
    * Helper function.  Performs a breadth-first-search on the trie starting at 
    * node x.
    * @param x current node
    * @param pre string computed so far
    * @param q queue of words found
    */
    private void collect(Node x, String pre, Queue<String> q)
    {
    	if(x == null)
    		return;
    	if(x.val != -1)
    		q.enqueue(pre);
    	for(char c = 0; c < 26;c++)
    	{
    		char ch = (char)('A' + c);
    		collect(x.next[c],pre + ch,q);
    		
    	}    	
    }


	private Node root = new Node();

    public static void main(String[] args)
    {
        Trie trie = new Trie();
        trie.put("SHE",1);
        trie.put("SELLS",1);
        trie.put("SEA",1);
        trie.put("SHELLS",1);
        trie.put("BY",1);
        trie.put("THE",1);
        trie.put("SEA",1);
        trie.put("SHORE",1);


        assert trie.get("SHE") == 1;
        assert trie.get("SELLS") == 1;
        assert trie.get("SEA") == 1;
        assert trie.get("SHELLS") == 1;
        assert trie.get("BY") == 1;
        assert trie.get("THE") == 1;
        assert trie.get("SEA") == 1;
        assert trie.get("SHORE") == 1;
        
        {
        	int cnt = 0;
        	for(String word : trie.keys())
        	{
        		cnt++;
        	}
        	assert cnt == 7;
        }
    }
}
