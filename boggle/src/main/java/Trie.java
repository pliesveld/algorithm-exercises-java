import edu.princeton.cs.algs4.Queue;

public class Trie
{
    private static class Node
    {
        private int val = -1;
        private Node[] next = new Node[26];

        public Node()
        {
        }

        public Node(Node parent, char ch)
        {
            parent.assignNode(this,ch);
        }

        private void assignNode(Node x, char idx)
        {
            if(idx < 0 || idx > 26)
                throw new IllegalArgumentException("Invalid index: " + idx); 

            if(next[idx] != null)
                throw new IllegalArgumentException("Argument letter already used: " + idx); 

            
            next[idx] = x;
        }  

    }

    public void put(String word,int val)
    {
        put(root,word,val,0);
    }

    public int get(String word)
    {
        Node x = get(root,word,0);

        if(x == null)
            return -1;
        return x.val;
    }

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

    public Iterable<String> keys()
    {
    	return keysWithPrefix("");
    }

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

    public Iterable<String> keysWithPrefix(final String pre) {

    	Queue<String> q = new Queue<>();
    	collect(get(root,pre,0),pre,q);
    	return q;
	}
    
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
