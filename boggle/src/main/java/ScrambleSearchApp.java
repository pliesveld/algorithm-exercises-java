/*
    Scramble is a word game where a player constructs words from adjacent letters.
    Once a letter has been chosen, it cannot be used again in the same word.

    This is a brute-force search application for finding words within a scramble board.
 */


import java.io.*;
import java.lang.ClassLoader;
import java.util.*;

import static java.lang.System.*;

class Dictionary
{
    Set<String> wordList = new HashSet<String>();

    <F extends BufferedReader> Dictionary(F in) throws IOException {
        String w;
        while((w = in.readLine()) != null)
        {
            assert !wordList.contains(w);
            wordList.add(w);
        }

        in.close();

    }

    boolean isWord(String w)
    {
        return wordList.contains(w);
    }
}

enum Action
{
    NORTH(0,1), NORTHEAST(1,1), EAST(1,0), SOUTHEAST(1,-1), SOUTH(0,-1), SOUTHWEST(-1,-1), WEST(-1,0), NORTHWEST(-1,1);


    Action(int x, int y) { this.d_x = x; this.d_y = y; }

    private final int d_x;
    private final int d_y;

    public int next_x() { return this.d_x; }
    public int next_y() { return this.d_y; }

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

class Location
{
    int x, y;

    Location(int _x, int _y)
    {
        assert _x >= 0 && _x <= 3;
        assert _y >= 0 && _y <= 3;

        this.x = _x;
        this.y = _y;
    }

    Location(Location rhs)
    {
        this.x = rhs.x;
        this.y = rhs.y;
    }

    int getX() { return x; }
    int getY() { return y; }

    @Override
    public int hashCode()
    {
        if(x >= 0 && y >= 0 && x <= 3 && y <= 3)
            return 1 + (4 * this.x) + this.y;
        return 0;
    }
    @Override
    public boolean equals(Object o)
    {
        return o instanceof Location
                && x == ((Location)o).x
                && y == ((Location)o).y;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(8);
        sb.append('(');
        sb.append(this.x);
        sb.append(',');
        sb.append(this.y);
        sb.append(')');
        return sb.toString();
    }

    /*
        Advance this location's coordinates
        @param a: cardinal direction
     */
    void advance(Action a)
    {
        int d_x = 0, d_y = 0;

        d_x += a.next_x();
        d_y += a.next_y();

        try {
            assert (this.x + d_x) >= 0 && (this.x + d_x) <= 3;
            assert (this.y + d_y) >= 0 && (this.y + d_y) <= 3;
        } catch(java.lang.AssertionError e)
        {
            err.println("Cannot advance: " + a);
            err.println("Current (x,y): (" + this.x + ',' + this.y + ')');
            err.println("Delta (x,y): (" + d_x + ',' + d_y + ')');
            throw e;
        }

        this.x += d_x;
        this.y += d_y;
    }

/*
    Returns a new Location that is adjacent to this location
    @param a: cardinal direction
 */
    Location adjacent(Action a)
    {
        int d_x = 0, d_y = 0;

        d_x += a.next_x();
        d_y += a.next_y();

        int new_x = (this.x + d_x);
        int new_y = (this.y + d_y);

        try {
            assert new_x >= 0 && new_x <= 3;
            assert new_y >= 0 && new_y <= 3;
        } catch(AssertionError e)
        {
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


        return new Location(new_x,new_y);
    }

}


class Board
{
    static final int MAX_BOARD_WIDTH = 4;
    static final int MAX_BOARD_HEIGHT = 4;
    static class Tile
    {
        private char letter;

        Tile(char letter)
        {
            this.letter = letter;
        }

        char getLetter()
        {
            return this.letter;
        }

        public String toString()
        {
            return ""+letter;
        }

    }

    Tile[][] board;

    Board(Tile[][] board)
    {
        this.board = board;
    }

    Board.Tile getTileAt(Location loc)
    {
        int y = loc.getY();
        int x = loc.getX();

        return this.board[y][x];
    }

    char getLetterAt(Location loc)
    {
        Board.Tile t = getTileAt(loc);
        return t.getLetter();
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(512);
        for(int i = 0; i < MAX_BOARD_HEIGHT;++i)
        {
            for(int j = 0; j < MAX_BOARD_WIDTH;++j)
            {
                sb.append(board[i][j]);
                sb.append('\t');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}

class Util
{
    /*
        Returns the set of actions that are initially available at a location.
        Removes Actions that would advance the location off board.
     */
    static EnumSet<Action> initial_actions(Location initial)
    {
        final EnumSet<Action> east  = EnumSet.of(Action.NORTHEAST, Action.EAST, Action.SOUTHEAST);
        final EnumSet<Action> west  = EnumSet.of(Action.NORTHWEST, Action.WEST, Action.SOUTHWEST);
        final EnumSet<Action> north = EnumSet.of(Action.NORTHWEST, Action.NORTH, Action.NORTHEAST);
        final EnumSet<Action> south = EnumSet.of(Action.SOUTHWEST, Action.SOUTH, Action.SOUTHEAST);

        int x = initial.getX(), y = initial.getY();

        EnumSet<Action> actions = EnumSet.allOf(Action.class);

        if(x == 0 )
            actions.removeAll(west);
        else if(x == 3 )
            actions.removeAll(east);

        if(y == 0 )
            actions.removeAll(south);
        else if(y == 3 )
            actions.removeAll(north);
        return actions;
    }

    /*
        Accumulates the point total of individual letters
     */
    static int valueOf(int word_len)
    {
        int score = 0;

        switch(word_len)
        {
            case 1:
            case 2:
                return 1;
            case 3:
            case 4:
                break;
            case 8:
                score += 5;
            case 7:
                score += 5;
            case 5:
            case 6:
                score += 5;
                break;

        }
        return score;
    }
}

class State
{
    Location loc;

    HashSet<Location> visited;
    Action prev;
    Board board;

    List<Character> word;

    State(State rhs)
    {
        this.loc = new Location(rhs.loc);
        (this.visited = new HashSet<Location>()).addAll(rhs.visited);
        this.word = new ArrayList<Character>(rhs.word);
        this.prev = rhs.prev;
        this.board = rhs.board;
    }

    State(Board board, Location loc)
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

        Board.Tile tile = board.getTileAt(loc);
        char c = tile.getLetter();

        this.word.add(c);
        if(c == 'q')
            this.word.add('u');
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

    int getScore()
    {
        return Util.valueOf(word.size());
    }

    EnumSet<Action> available_actions()
    {
        EnumSet<Action> actions = Util.initial_actions(this.loc);

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

class Scramble
{
    static final int MAX_WORD_SIZE;
    static final int MIN_WORD_SIZE;

    static final int MAX_BOARD_WIDTH = Board.MAX_BOARD_WIDTH;
    static final int MAX_BOARD_HEIGHT = Board.MAX_BOARD_HEIGHT;

    static Board board;

    static {
       String env;
       MIN_WORD_SIZE = (env = getenv("MIN_WORD_SIZE")) != null ? Integer.parseInt(env) : 4;
       MAX_WORD_SIZE = (env = getenv("MAX_WORD_SIZE")) != null ? Integer.parseInt(env) : 8;
       assert MIN_WORD_SIZE <= MAX_WORD_SIZE;

       int cur = 0;

       BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
       Scanner in = new Scanner(r);

        try {

            Board.Tile[][] tile_array = new Board.Tile[MAX_BOARD_HEIGHT][MAX_BOARD_WIDTH];

            if(r.ready() || getenv("BLOCKING") == null)
            {
            reading:
                while(in.hasNext())
                {
                    String line = in.next();
                    int len = line.length();

                    for(int i = 0; i < len; ++i)
                    {
                        char c = line.charAt(i);

                        if(!Character.isLetter(c))
                            continue;

                        c = Character.toLowerCase(c);

                        int Y = (cur/4)%4;
                        int X = cur%4;

                        Board.Tile t = new Board.Tile(c);
                        tile_array[Y][X] = t;

                        if(++cur >= MAX_BOARD_WIDTH*MAX_BOARD_HEIGHT )
                            break reading;

                    }

                }

                board = new Board(tile_array);
            } else {
                board = null; //TODO: ugly, fix me
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Dictionary dict;
    LinkedList<State> fringe;

    Scramble(Dictionary dict)
    {
        System.out.println("Board:\n" + board);

        this.dict = dict;
        fringe = new LinkedList<State>();

        for(int i = 0; i < MAX_BOARD_WIDTH; ++i)
            for(int j = 0; j < MAX_BOARD_HEIGHT; ++j)
                fringe.add(new State(Scramble.board,new Location(j,i)));


    }

    void find_words()
    {
        State s;
	List<String> found = new LinkedList<>();
        while (( s = fringe.poll()) != null)
        {

         //   System.out.println("Current: " + s);

            String w = s.getWord();
            if(dict.isWord(w) && w.length() >= 3)
                found.add(w);

            if(w.length() >= MAX_WORD_SIZE)
                continue;

            LinkedList<State> next_states = new LinkedList<State>();

            for(Action a : s.available_actions())
            {
                next_states.add(s.next(a));
            }

            for(State ns : next_states)
            {
                fringe.push(ns);
            }

        }


        boolean printScore = getenv("SCORE") != null;
        int n_found = 0;
	int total_score = 0;

        while(!found.isEmpty())
        {

            String word = found.remove(0);
            if(printScore)
                out.print("Found: ");
            out.println(word.toString());
            total_score += Util.valueOf(word.length());
            ++n_found;
        }

        out.println("Score = " + total_score);

    }

}


public class ScrambleSearchApp {
    public static void main(String[] args) throws IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream("dictionary-enable1.txt");

        if(is == null)
        {
            is = new FileInputStream("enable1.txt");
        }

        BufferedReader inBuff = new BufferedReader(new InputStreamReader(is));
        Dictionary dict = new Dictionary(inBuff);
        Scramble s = new Scramble(dict);
        s.find_words();
    }
}
