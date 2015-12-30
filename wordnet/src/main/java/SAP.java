import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;

/**
 * Shortest Ancestrial Path.  Given a rooted DAG, An ancestrial path is defined to be a common vertex X that is in the path from
 * two vertexes towards the root.
 *
 * To compute the SAP, two parallel BFS searches are performed.  Once a node has been marked in one BFS search, if the other parallel
 * BFS search crosses the same node, then that node is the common ancestor of both and guarenteed to be the shortest because of the
 * property of BFS that exlpores nodes increasingly in depth.
 * 
 */
public class SAP {

    private Digraph graph;
 
    /**
     * Constructor for dia. Copies digraph locally
     * @param G digraph
     */
    public SAP(Digraph G) {
        if (G == null)
            throw new NullPointerException("Directed Graph parameter was null.");
    
        graph = new Digraph(G);

    }


    /**
     * A utility class used in computing a path.  Maintaks a node that is common root,
     * and records accompaning cost.
     */
    private class PathNode
    implements Comparable<PathNode>
    {
    	private int node;
    	private int cost;
    	
        /** Constructor for PathNode @param node node id of ancestor @param cost cost to node */
		public PathNode(int node, int cost) {
			super();
			this.node = node;
			this.cost = cost;
			
		}
		
        /** @return node id of ancestor found */
		public int getNode() {
			return node;
		}

        /** @return cost of ancestor found */
		public int getCost() {
			return cost;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + cost;
			result = prime * result + node;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PathNode other = (PathNode) obj;
			if (cost != other.cost)
				return false;
			if (node != other.node)
				return false;
			return true;
		}

		@Override
		public int compareTo(PathNode o) {
			return this.cost - o.cost;
		}

    	
    }
    
    /**
     * Computes the distance between word nounA and nounB.
     *
     * Defines the semantic relatedness of two nouns.  
     *
     * distance( A , B ) = distance is the minimum length of any ancestral path
     * between any synset v of A and any synset of w of B.
     *
     * @param v synset
     * @param w synset
     * @return distance between v and w of shortest ancestor, -1 if none found
     */

    public int length(int v, int w) {
        verifyVertex(v);
        verifyVertex(w);
        

        if(v == w)
        	return 0;
       
        MinPQ<PathNode> visited_history = new MinPQ<>(4);
        Set<Integer> visited = new HashSet<Integer>();
        Queue<PathNode> queue_v = new LinkedList<>(); 
        Queue<PathNode> queue_w = new LinkedList<>();
        
        Set<Integer> explored_v = new HashSet<Integer>();
        Set<Integer> explored_w = new HashSet<Integer>();
        
        explored_v.add(v);
        explored_w.add(w);
        
        ST<Integer,Integer> v_cost = new ST<>();
        ST<Integer,Integer> w_cost = new ST<>();

        v_cost.put(v,0);
        w_cost.put(w,0);
        
        queue_v.add(new PathNode(v,0));
        queue_w.add(new PathNode(w,0));


        while(!queue_v.isEmpty() || !queue_w.isEmpty())
        {
        	
        	if(!queue_v.isEmpty())
        	{
	        	PathNode u = queue_v.remove();
	            int length = u.getCost();
	            int last_node = u.getNode();

	        	if(explored_v.contains(last_node) && explored_w.contains(last_node) && !visited.contains(last_node))
	        	{
	        		visited.add(last_node);
	        		int len1 = v_cost.get(last_node);
	        		int len2 = w_cost.get(last_node);
	        		visited_history.insert(new PathNode(last_node,len1+len2));
	        	}

	        	for(int child : graph.adj(u.getNode()))
	        	{
	        		if(!explored_v.contains(child))
	        		{
	        			explored_v.add(child);
	        			queue_v.add(new PathNode(child,length+1));
        				v_cost.put(child,length+1);
	        		}
	        	}
        	}
        	
        	
        	if(!queue_w.isEmpty())
        	{
	        	PathNode u = queue_w.remove();
	            int length = u.getCost();
	            int last_node = u.getNode();

	        	if(explored_v.contains(last_node) && explored_w.contains(last_node)&& !visited.contains(last_node))
	        	{
	        		visited.add(last_node);
	        		int len1 = v_cost.get(last_node);
	        		int len2 = w_cost.get(last_node);
	        		visited_history.insert(new PathNode(last_node,len1+len2));
	        	}
	            
	        	for(int child : graph.adj(u.getNode()))
	        	{
	        		if(!explored_w.contains(child))
	        		{
	        			explored_w.add(child);
	        			queue_w.add(new PathNode(child,length+1));
        				w_cost.put(child,length+1);
	        		}
	        	}
        	}
        	
        }
        
        if(visited_history.isEmpty())
        	return -1;
        
        PathNode best = visited_history.delMin();
        return best.getCost();


    }


     /** Shortest Ancestral Path between synset v and synset w.
     * Traverses the Diagraph to find a common ancestor of v and w.  Guarenteed
     * to be the shortest traversal distance.
     *
     * @return synset id of ancestor of v and w that is of the shortest distance.
     */
    public int ancestor(int v, int w) {
        verifyVertex(v);
        verifyVertex(w);
        
        if(v == w)
        	return v;
        
        /*
        for(int child : graph.adj(v))
        {
        	if(child == w)
        		return child;
        }
        
        for(int child : graph.adj(w))
        {
        	if(child == v)
        		return child;
        }
        */
        
        
        
        MinPQ<PathNode> visited_history = new MinPQ<>(4);
        Set<Integer> visited = new HashSet<Integer>();
        Queue<PathNode> queue_v = new LinkedList<>(); 
        Queue<PathNode> queue_w = new LinkedList<>();
        
        Set<Integer> explored_v = new HashSet<Integer>();
        Set<Integer> explored_w = new HashSet<Integer>();

        explored_v.add(v);
        explored_w.add(w);
        
        ST<Integer,Integer> v_cost = new ST<>();
        ST<Integer,Integer> w_cost = new ST<>();
        
        v_cost.put(v,0);
        w_cost.put(w,0);
        
        queue_v.add(new PathNode(v,0));
        queue_w.add(new PathNode(w,0));

        while(!queue_v.isEmpty() || !queue_w.isEmpty())
        {
        	Integer last_node = -1;
        	if(!queue_v.isEmpty())
        	{
	        	PathNode u = queue_v.remove();
	            int length = u.getCost();
	            last_node = u.getNode();

	        	if(explored_v.contains(last_node) && explored_w.contains(last_node) && !visited.contains(last_node))
	        	{
	        		visited.add(last_node);
	        		int len1 = v_cost.get(last_node);
	        		int len2 = w_cost.get(last_node);
	        		visited_history.insert(new PathNode(last_node,len1+len2));
	        	}

	            
	        	for(int child : graph.adj(u.getNode()))
	        	{
	        		if(!explored_v.contains(child))
	        		{
	        			explored_v.add(child);
	        			queue_v.add(new PathNode(child,length+1));
	        			v_cost.put(child,length+1);
	        		}
	        	}
        	}
        	       	
        	if(!queue_w.isEmpty())
        	{
	        	PathNode u = queue_w.remove();
	            int length = u.getCost();
	            last_node = u.getNode();

	        	if(explored_v.contains(last_node) && explored_w.contains(last_node)&& !visited.contains(last_node))
	        	{
	        		visited.add(last_node);
	        		int len1 = v_cost.get(last_node);
	        		int len2 = w_cost.get(last_node);
	        		visited_history.insert(new PathNode(last_node,len1+len2));
	        	}
	        	
	        	for(int child : graph.adj(u.getNode()))
	        	{
	        		if(!explored_w.contains(child))
	        		{
	        			explored_w.add(child);
	        			queue_w.add(new PathNode(child,length+1));
	        			w_cost.put(child,length+1);
	        		}
	        	}
        	}
        	

        }

        if(visited_history.isEmpty())
        	return -1;
        
        PathNode best = visited_history.delMin();
        return best.getNode();
    }

    /**
     * Given a collection of nodes v and w, perform a length 
     * calculation betewen any node in v and any node in w and
     * @return shortest length between any two nodes in v and w.
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
    	if(v == null || w == null)
    		throw new NullPointerException("length supplied with null argument.");
    	
    	LinkedList<Integer> w_list = new LinkedList<>();
    	   	
    	for(Integer w_id : w)
    	{
    		w_list.add(w_id);
    	}
    	
    	int b_len = -1;
    	
    	
    	for(Integer v_id : v)
    	{
    		for(Integer w_id : w_list)
    		{
    			int len = length(v_id,w_id);
    			
    			if(b_len == -1 || len < b_len)
    			{
    				b_len = len;
    			}
    		}
    	}
    	
    	return b_len;
    }

    /**
     * Given a collection of nodes v and w, perform a SAP
     * calculation betewen any node in v and any node in w and
     * @return the ancestor with the shortest length between any two nodes in v and w.
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    	if(v == null || w == null)
    		throw new NullPointerException("ancestor supplied with null argument.");

    	LinkedList<Integer> w_list = new LinkedList<>();
	   	
    	for(Integer w_id : w)
    	{
    		w_list.add(w_id);
    	}

    	int b_len = -1;
    	int b_v = -1, b_w = -1;
    	
    	
    	for(Integer v_id : v)
    	{
    		for(Integer w_id : w_list)
    		{
    			int len = length(v_id,w_id);
    			

    			if(b_len == -1 || len < b_len)
    			{
    				b_len = len;
    				b_w = w_id;
    				b_v = v_id;
    			}


    		}
    	}

    	
    	if(b_len == -1)
    		return -1;
    	
    	return ancestor(b_v,b_w);
    }

    /** Verify Vertex @param x is valid inside digraph */
    private void verifyVertex(int x) {
        if (x >= 0 && x < graph.V())
            return;
        throw new IndexOutOfBoundsException("Invalid index: " + x);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }


}

