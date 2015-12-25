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

public class SAP {

    private Digraph graph;
 
    public SAP(Digraph G) {
        if (G == null)
            throw new NullPointerException("Directed Graph parameter was null.");
    
        graph = new Digraph(G);

    }

    private class PathNode
    implements Comparable<PathNode>
    {
    	private int node;
    	private int cost;
    	
		public PathNode(int node, int cost) {
			super();
			this.node = node;
			this.cost = cost;
			
		}
		
		public int getNode() {
			return node;
		}

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
    
    // length of shortest ancestral path between v and w; -1 if no such path
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
    
    // a	 common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
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

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
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

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
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

