import static java.lang.System.err;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver
{
	private class Dictionary
	{
	    private Set<String> wordList = new HashSet<String>();
		private Trie trie = new Trie();

	    private int max_word = 0;
	    
    
	    private void addWord(final String w)
	    {
	    	wordList.add(w);
	    	trie.put(w,BoggleSolver.this.wordLenScore(w));
	    }

	    public <F extends BufferedReader> Dictionary(F in) throws IOException {
	        String w;
	        int max = 0;
	        while((w = in.readLine()) != null)
	        {
	            assert !wordList.contains(w);
	            addWord(w);
	            if(w.length() > max)
	            {
	            	max = w.length();
	            }
	        }
	        in.close();
	        max_word = max;

	    }

	    public Dictionary(String[] dictionary) {
	    	int max = 0;
	    	for(String w : dictionary)
	    	{
	    		addWord(w);
	    		if(w.length() > max)
	    			max = w.length();
	    	}
	    	max_word = max;
		}
	    

		boolean isWord(String w)
	    {
	        return wordList.contains(w);
	    }
		
	    public int getMaxWordSize()
	    {
	    	return max_word;
	    }

	}
	
	private enum Action
	{ 
	    NORTH(0,1), NORTHEAST(1,1), EAST(1,0), SOUTHEAST(1,-1), SOUTH(0,-1), SOUTHWEST(-1,-1), WEST(-1,0), NORTHWEST(-1,1);


	    Action(int x, int y) { this.d_x = x; this.d_y = y; }

	    private final int d_x;
	    private final int d_y;

	    public int next_x() { return this.d_x; }
	    public int next_y() { return this.d_y; }

	 // TODO: Move EnumMap to abstract method
	    static final EnumMap<Action,Action> complement = new EnumMap<Action,Action>(Action.class);

	    static {
	        complement.put(NORTH,SOUTH);
	        complement.put(NORTHEAST,SOUTHWEST);
	        complement.put(EAST,WEST);
	        complement.put(SOUTHEAST,NORTHWEST);

	        for(Map.Entry<Action,Action> e : complement.entrySet())
	        {
	            complement.put(e.getValue(),e.getKey());
	        }
	    }


	}

	
	
	private class Location {
		protected int x;
		protected int y;

		Location(int _x, int _y) {
			this.x = _x;
			this.y = _y;
		}

		Location(Location rhs) {
			this.x = rhs.x;
			this.y = rhs.y;
		}

		int getX() {
			return x;
		}

		int getY() {
			return y;
		}
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
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
			Location other = (Location) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder(8);
			sb.append('(');
			sb.append(this.x);
			sb.append(',');
			sb.append(this.y);
			sb.append(')');
			return sb.toString();
		}

		/*
		 * Advance this location's coordinates
		 * 
		 * @param a: cardinal direction
		 */
		void advance(Action a)
		 {
			final int width = BoggleSolver.this.getMaxBoardWidth();
			final int height = BoggleSolver.this.getMaxBoardHeight();
			int d_x = 0, d_y = 0;

			d_x += a.next_x();
			d_y += a.next_y();

			try {
				assert (this.x + d_x) >= 0 && (this.x + d_x) <= width;
				assert (this.y + d_y) >= 0 && (this.y + d_y) <= height;
			} catch (java.lang.AssertionError e) {
				err.println("Cannot advance: " + a);
				err.println("Current (x,y): (" + this.x + ',' + this.y + ')');
				err.println("Delta (x,y): (" + d_x + ',' + d_y + ')');
				throw e;
			}

			this.x += d_x;
			this.y += d_y;
		}

		Location adjacent(Action a)
		{
			final int width = BoggleSolver.this.getMaxBoardWidth();
			final int height = BoggleSolver.this.getMaxBoardHeight();
			int d_x = 0, d_y = 0;

			d_x += a.next_x();
			d_y += a.next_y();

			int new_x = (this.x + d_x);
			int new_y = (this.y + d_y);

			try {
				assert new_x >= 0 && new_x <= width;
				assert new_y >= 0 && new_y <= height;
			} catch (AssertionError e) {
				err.print("Location: " + this);
				err.print("+= (");
				err.print(d_x);
				err.print(',');
				err.print(d_y);
				err.print(") = ");
				err.print(new_x);
				err.print(',');
				err.println(new_y);
				throw e;
			}
			return new Location(new_x, new_y);
		}

		private BoggleSolver getOuterType() {
			return BoggleSolver.this;
		}

	}

    private EnumSet<Action> initial_actions(Location initial)
    {
		final int width = BoggleSolver.this.getMaxBoardWidth();
		final int height = BoggleSolver.this.getMaxBoardHeight();

        final EnumSet<Action> east  = EnumSet.of(Action.NORTHEAST, Action.EAST, Action.SOUTHEAST);
        final EnumSet<Action> west  = EnumSet.of(Action.NORTHWEST, Action.WEST, Action.SOUTHWEST);
        final EnumSet<Action> north = EnumSet.of(Action.NORTHWEST, Action.NORTH, Action.NORTHEAST);
        final EnumSet<Action> south = EnumSet.of(Action.SOUTHWEST, Action.SOUTH, Action.SOUTHEAST);

        int x = initial.getX(), y = initial.getY();

        EnumSet<Action> actions = EnumSet.allOf(Action.class);

        if(x == 0 )
            actions.removeAll(west);
        if(x == width-1 )
            actions.removeAll(east);

        if(y == 0 )
            actions.removeAll(south);
        if(y == height-1 )
            actions.removeAll(north);
        return actions;
    }
	
	private class State
	{
	    private Location loc;

	    private HashSet<Location> visited;
	    private Action prev;
	    private BoggleBoard board;
	    private List<Character> word;
	    

	    State(State rhs)
	    {
	        this.loc = new Location(rhs.loc);
	        (this.visited = new HashSet<Location>()).addAll(rhs.visited);
	        this.word = new ArrayList<Character>(rhs.word);
	        this.prev = rhs.prev;
	        this.board = rhs.board;
	    }

	    State(BoggleBoard board, Location loc)
	    {
	        this.loc = loc;
	        this.board = board;
	        this.word = new ArrayList<Character>();
	        this.visited = new HashSet<Location>();
	        addLocationToWord();
	    }

	    void addLocationToWord()
	    {
	        this.visited.add(this.loc);
	        final int col = loc.getX();
	        final int row = loc.getY();
	        
	        char c = board.getLetter(row,col);

	        this.word.add(c);
	        if(c == 'Q')
	            this.word.add('U');
	    }

	    String getWord()
	    {
	        StringBuilder sb = new StringBuilder(word.size());
	        for(Character ch: word)
	        {
	            sb.append(ch);
	        }
	        return sb.toString();
	    }

	    EnumSet<Action> available_actions()
	    {
	        EnumSet<Action> actions = initial_actions(this.loc);

	        if(this.prev != null)
	            actions.remove(Action.complement.get(this.prev));

	        EnumSet<Action> visited_history = EnumSet.noneOf(Action.class);

	        for(Action a : actions)
	        {
	            Location next_loc = loc.adjacent(a);
	            if(has_visited(next_loc))
	                visited_history.add(a);
	        }

	        actions.removeAll(visited_history);
	        return actions;
	    }

	    void advance(Action a)
	    {
	        this.prev = a;
	        this.loc.advance(a);
	        assert !has_visited(this.loc);
	        addLocationToWord();
	    }

	    boolean has_visited(Location l)
	    {
	        return this.visited.contains(l);
	    }

	    public State next(Action a)
	    {
	        State s = new State(this);
	        s.advance(a);
	        return s;
	    }

	    public String toString()
	    {
	        StringBuilder sb = new StringBuilder(1024);

	        sb.append("Word:");
	        sb.append(getWord());
	        sb.append("\tLoc: ");
	        sb.append(this.loc);
	        sb.append("\tActions (");
	        sb.append(this.available_actions());
	        sb.append(")");

	        return sb.toString();
	    }
	}
	
	private Dictionary dict;
	
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
    	dict  = new Dictionary(dictionary);
    }

    private int maxBoardWidth  = 4;
    private int maxBoardHeight = 4;
    private int maxWordSize = 8;
    private static final int MIN_WORD_SIZE = 3;

    
    private int getMaxBoardWidth() {
		return maxBoardWidth;
	}

	private void setMaxBoardWidth(int maxBoardWidth) {
		this.maxBoardWidth = maxBoardWidth;
	}

	private int getMaxBoardHeight() {
		return maxBoardHeight;
	}

	private void setMaxBoardHeight(int maxBoardHeight) {
		this.maxBoardHeight = maxBoardHeight;
	}


	// Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {

    	int board_width = board.cols();
    	int board_height = board.rows();
    	
    	setMaxBoardWidth(board_width);
    	setMaxBoardHeight(board_height);
    	int maxWordSize = dict.getMaxWordSize();
    	
        LinkedList<State> fringe = new LinkedList<State>();
        
        for(int i = 0; i < board_width; ++i)
            for(int j = 0; j < board_height; ++j)
                fringe.add(new State(board,new Location(i,j)));

        
        
//        StdOut.println("mws:" + maxWordSize);
    	Set<String> found = new LinkedHashSet<>();
    	State s;
        while ((s = fringe.poll()) != null)
        {

         //   System.out.println("Current: " + s);

            String w = s.getWord();
            if(dict.isWord(w) && w.length() >= MIN_WORD_SIZE)
                found.add(w);

            if(w.length() > maxWordSize)
                continue;

            LinkedList<State> next_states = new LinkedList<State>();

            for(Action a : s.available_actions())
            {
                next_states.add(s.next(a));
            }

            for(State ns : next_states)
            {
            	if(BoggleSolver.this.dict.trie.anyKeyHasPrefix(ns.getWord()))
            		fringe.push(ns);
          //  	Iterator<String> it_pre = BoggleSolver.this.dict.trie.keysWithPrefix(ns.getWord()).iterator();
           // 	if(it_pre.hasNext())
           // 		fringe.push(ns);
            }

        }

        
        return found;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
    	if(dict.wordList.contains(word))
    		return wordLenScore(word);
    	return 0;
    }
    
    private int wordLenScore(String word)
    {
        switch(word.length())
        {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
            	return 2;
            case 6:
            	return 3;
            case 7:
            	return 5;
			default:
				return 11;
        }
    }


    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}




