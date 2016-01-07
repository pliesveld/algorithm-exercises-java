import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import java.util.Map;
import java.util.Set;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;


/**
 * In the baseball elimination problem, a set of teams within a league, and the goal
 * is to determine if a team has been mathematically eliminated from winning.  Elimination occurs
 * if another team is guaranteed to have more wins at the end of the season than team under consideration.
 *
 * The input to the system is a collection of N teams, and their respective wins, losses, and games remaining.
 * Followed by the number of games remaining with each team in the league. 
 *
 * A team is trivially eliminated if the maximum number of games it can win is less than the 
 * number of wins a team another team has.  That is for a team x, if there exists a team i, such
 * that w[x] + r[x] < w[i], then team x is trivially eliminated by team i.
 *
 * Non-trivial elimination requires examination of each teams remaining games and those games 
 * participants in determing if a team is mathematically eliminated.
 *
 *
 * In the following, it may seam that Detroit has a remote chance of winning.  
 *
 * i team        w[i]  l[i]  r[i]    g[i][j]
 *               wins  loss  left    NY   Bal  Bos  Tor  Det
 * ---------------------------------------------------------
 * 1 New_York    75    59    28      -    3    8    7    3
 * 2 Baltimore   71    63    28      3    -    2    7    7
 * 3 Boston      69    66    27      8    2    -    0    3
 * 4 Toronto     63    72    27      7    7    0    -    3
 * 5 Detroit     49    86    27      3    7    3    3    -
 *
 * For Detroit, x = 5, the w[x] + r[x] = 49 + 27 = 76.  With no team having more than 76 currently,  
 * it is not trivially eliminated.  However, if Detroit were to go on a 27-game win streak, the teams
 * in the remaining games Detroit plays, would receive losses.
 *
 * i team        w[i]  l[i]  r[i]    g[i][j]
 *               wins  loss  left    NY   Bal  Bos  Tor  Det
 * ---------------------------------------------------------
 * 1 New_York    75    62    25      -    3    8    7    0
 * 2 Baltimore   71    70    21      3    -    2    7    0
 * 3 Boston      69    69    24      8    2    -    0    0
 * 4 Toronto     63    75    24      7    7    0    -    0
 * 5 Detroit     76    86    0       0    0    0    0    -
 *
 * Likewise, New_York would have to go on a 25-game loosing streak.  And the teams New York plays
 * in the remaining games, would receive wins.
 *
 * i team        w[i]  l[i]  r[i]    g[i][j]
 *               wins  loss  left    NY   Bal  Bos  Tor  Det
 * ---------------------------------------------------------
 * 1 New_York    75    87     0      -    0    0    0    0
 * 2 Baltimore   74    70    19      3    -    2    7    0
 * 3 Boston      77    69    16      8    2    -    0    0
 * 4 Toronto     70    75    17      7    7    0    -    0
 * 5 Detroit     76    86    0       0    0    0    0    -
 *
 * However the outcomes of those games would put Boston at 77 wins, mathematically eliminating Detriot. There
 * may be other teams that eliminate Detroit, finding these teams is called the certificate of elimination.
 *
 * To solve this problem, we construct a FlowNetwork and solve the maximum-flow problem.
 *
 * A flow network is a edge-weighted Directed Ayclic Graph with two special vertexes: a source (denoted with s)
 * and a sink (denoted with t).  There can be any number of intermediate vertexes in the network.  Each edge
 * in the network has a non-negative capacity.  For vertex u and v, the capcity is of the edge from the
 * source vertex u to the target vertex v denoted c(u,v).  
 *
 * An st-flow is a flow network with a source s, and sink t.  Each edge has a flow value ( denoted f(u,v) )
 * assigned.  The following must be satisfied:
 *
 * 1) Capcity constraint: for all u,v in V, 0 <= f(u,v) <= c(u,v).
 * The flow value must be non-negative and not greater than the capacity.
 *
 * 2) Flow conservation / Local Equilibrium in a flow network: flow in equals flow out
 * For every vertex except the s and t, the summation of the edge's flow into the vertex
 * equals the summation of the edge's flow out of the vertex.
 *
 * To setup the network for the baseball, we construct a network with two types of vertexes.  The first type
 * is a game vertex that represents a remaining game between two teams.  Given N teams, the number of 
 * game vertexes is N choose 2.
 *
 * The source vertex s has an edge to each game vertex.  The capacity of each edge is the remaining games
 * yet to be decided between the two teams represented in the game vertex.  In the above example, the 
 * vertex representing the game between Boston and Baltimore would have an edge capacity of 2.
 *
 * The second type of vertex is a team vertex.  Each game vertex has two edges targeting the respective
 * team vertex.  The capacity of this edge is infinite.  Each team vertex has an edge to the sink vertex.
 * The capcity of this edge is the special sauce of this algorithm.
 *
 * The unit of flow in the network corresponds to a remaining game.  As the network flows through the game
 * vertexes and then the team vertex, the flow value represents a game won by a team.
 *
 * When solving the BaseballEliminatation problem, a flow-network is constructed for each team to determine
 * elimination.  The team under consideration does not participate in the network constructed.  Game vetexes 
 * that the team participates in have edges within the network.  For all other team vertexes, their edge
 * towards the sink has the following capacity:  For team vertex x under consideration, each remaining team
 * vertex i has an edge to the sink with the capacity w[x] + r[x] - w[i].  Where w[] is the wins, and r[] is the
 * games remaining.  In the above example. Detroit x = 5, would have a maximum possible of w[5] + r[5] = 76.
 *
 * We want to know if there is some way of completing all the games so that team x ends up winning atleast
 * as many games as team i.  Since team x can win as many as w[x] + r[x] games, we prevent team i from 
 * winning more than that many games in total, by including an edge from team vertex i to the sink vertex
 * with capacity w[x] + r[x] - w[i].  Effectively, we are saying team i may win this many more games.
 *
 * This maximum possible is constrained by each teams wins.  Recall that edge capacaities must be non-negative,
 * however, because we are considering teams that have not been trivially eliminated, there exists no team
 * that has more wins than the maximum possible.
 *
 * In the flow network for Detroit, i = 3, the team vertex Boston would have an edge towards sink with a capacity 
 * of w[5] + r[5] - w[3] = 49 + 27 - 69 = 7.
 *
 * The rest of the network constructed when testing Detroit elimination is as follows:
 *
 *

 *
 *
 *
 *
 * Vertexes: 
 *
 * Vertex 0    is SOURCE
 * Vertex 1    is team New_York
 * Vertex 2    is team Baltimore
 * Vertex 3    is team Boston
 * Vertex 4    is team Toronto
 * Vertex 5    is team Detroit
 * Vertex 6    is game (1,2)  [r=3]  // r = remaining
 * Vertex 7    is game (1,3)  [r=8]
 * Vertex 8    is game (1,4)  [r=7]
 * Vertex 9    is game (1,5)  [r=3]
 * Vertex 10   is game (2,3)  [r=2]
 * Vertex 11   is game (2,4)  [r=7]
 * Vertex 12   is game (2,5)  [r=7]
 * Vertex 13   is game (3,4)  [r=0]
 * Vertex 14   is game (3,5)  [r=3]
 * Vertex 15   is game (4,5)  [r=3]
 * Vertex 16   is SINK
 *
 * Team Detroit(5) has 76 possible wins.
 *
 * 1: New_York -> SINK 1
 * 2: Baltimore -> SINK 5
 * 3: Boston -> SINK 7
 * 4: Toronto -> SINK 13
 *
 * (vertex): (edge src)->(edge target) (flow)/(capacity)
 *     
 * 0:  0->13 0.0/0.0  0->11 0.0/7.0  0->10 0.0/2.0  0->8 0.0/7.0  0->7 0.0/8.0  0->6 0.0/3.0  
 * 1:  1->16 0.0/1.0  
 * 2:  2->16 0.0/5.0  
 * 3:  3->16 0.0/7.0  
 * 4:  4->16 0.0/13.0  
 * 5:  
 * 6:  6->2 0.0/Infinity  6->1 0.0/Infinity  
 * 7:  7->3 0.0/Infinity  7->1 0.0/Infinity  
 * 8:  8->4 0.0/Infinity  8->1 0.0/Infinity  
 * 9:  
 * 10:  10->3 0.0/Infinity  10->2 0.0/Infinity  
 * 11:  11->4 0.0/Infinity  11->2 0.0/Infinity  
 * 12:  
 * 13:  13->4 0.0/Infinity  13->3 0.0/Infinity  
 * 14:  
 * 15:  
 * 16:
 * 
 * The Ford-Fulkerson method is used to compute the maximum flow of the network.  It assigns a flow value
 * to each edge in the network while maintaining the feasibility constraints.
 * 
 * If all edges in the maxflow that are point from s are full, then this corresponds to assigning winners
 * to all of the remaining games in such a way that no team wins more games than x. 
 *
 *
 * Maximum Flow for Detroit after FordFulkerson
 * 17 22
 * 0:  0->13 0.0/0.0  0->11 7.0/7.0  0->10 2.0/2.0  0->8 7.0/7.0  0->7 7.0/8.0  0->6 3.0/3.0  
 * 1:  1->16 1.0/1.0  
 * 2:  2->16 5.0/5.0  
 * 3:  3->16 7.0/7.0  
 * 4:  4->16 13.0/13.0  
 * 5:  
 * 6:  6->2 3.0/Infinity  6->1 0.0/Infinity  
 * 7:  7->3 7.0/Infinity  7->1 0.0/Infinity  
 * 8:  8->4 6.0/Infinity  8->1 1.0/Infinity  
 * 9:  
 * 10:  10->3 0.0/Infinity  10->2 2.0/Infinity  
 * 11:  11->4 7.0/Infinity  11->2 0.0/Infinity  
 * 12:  
 * 13:  13->4 0.0/Infinity  13->3 0.0/Infinity  
 * 14:  
 * 15:  
 * 16:
 * 
 * Notice the source vertex 0.  The maximum flow to vertex 7 has a value of 7, and a capacity of 8.
 * Not all games could be assigned winners in such a way that would have Detroit win atleast as many games
 * as all others, hence Detroit is eliminated.
 * 
 */
public class BaseballElimination
{
	private ST<String,Integer> team_lookup = new ST<>();        // team -> vertex
	private String[] teams;                                     // Index 0 is empty
	
	private final boolean DEBUG;
	private final boolean DEBUG_NETWORK;
	
	private static final int SOURCE_VERTEX = 0;                 // Virtual Index of SOURCE in flow network
	private final int SINK_VERTEX;                              // Virtual Index of SINK   in flow network
	
	// key is from vertex of team.  First team is indexed starting at 1.
	private ST<Integer, Integer> wins = new ST<>();             // team vertex -> wins
	private ST<Integer, Integer> losses = new ST<>();           // team vertex -> losses
	private ST<Integer, Integer> remaining = new ST<>();        // team vertex -> remaining
	
	// key is from vertexOfGame( team_i, team_j )
	private ST<Integer, Integer> vs_remaining = new ST<>();     // game vertex -> remaining
	
	// key is from hashOfGame( team_i, team_j )
	private Map<Integer,Integer> lookupGame = new HashMap<>();  // game vertex hash -> game vertex
	
	private ArrayList<Set<String>> eliminates;                  // Indexed by team, set of teams in elimination certificate
	
	// key is from vertexOfGame( team_i, team_j )
	// value is TeamPair class encapsulating team_i, and team_j
	private ST<Integer, TeamPair> lookupTeams = new ST<>();     // game vertex -> tuple of teams
	
    /** Constructor for BaseballElimination.
     * Reads in game standings and computes baseball elimination using flow network.
     * Java property baseball.debug can be set to true to display debugging information
     * Java property baseball.solve can be used to specify a comma separated list of teams
     * to test.  The default action is to solve for all teams.
     * 
     * @param filename of game standing
     */
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		int i_team = 0;
		int nTeams = in.readInt();
		
		eliminates = new ArrayList<>(nTeams + 1);
		eliminates.add(new LinkedHashSet<String>());
		
		teams = new String[nTeams + 1];
		
		int V =  1      // Artificial Source vertex
              +  1       //Artificial Sink vertex
              +  nTeams  //Team Vertex,  nTeams total
              +  (nTeams * (nTeams - 1))/2; // Number of Game vertexes, nTeams choose 2
		
		SINK_VERTEX = V-1;
		
        /* Populate class members representing game vertexes */
		if(nTeams != 1)
			allocateGameNodes(nTeams + 1);
		
        // Read file input, and populate class members
		do {
			String team = in.readString();
			int win = in.readInt();
			int loss = in.readInt();
			int remaining_games = in.readInt();
			
			int v_team_i = i_team + 1;
			
			wins.put(v_team_i,win);
			losses.put(v_team_i,loss);
			remaining.put(v_team_i,remaining_games);
			
			eliminates.add(new LinkedHashSet<String>());
			
			for(int j_team = 0; j_team < nTeams; j_team++)
			{
				int i_vs_j = in.readInt();
				if(i_team == j_team)
					continue;

				int v_team_j = j_team + 1;
				int v_game = vertexOfGame(v_team_i,v_team_j);
				
				vs_remaining.put(v_game,i_vs_j);
			}
			
			team_lookup.put(team,v_team_i);
			teams[v_team_i] = team;
			
		} while(++i_team < nTeams);

        /* Check for baseball.debug property */
		boolean will_debug = false;
		try {
			will_debug = Boolean.valueOf(System.getProperty("baseball.debug"));
		} catch(SecurityException e) {
			will_debug = false;
		}		
		DEBUG = will_debug;

        /* Check for baseball.network.debug property */
		boolean will_debug_network = false;
		try {
			will_debug_network = Boolean.valueOf(System.getProperty("baseball.network.debug"));
		} catch(SecurityException e) {
			will_debug_network = false;
		}		
		DEBUG_NETWORK = will_debug_network;

		determine_trivial_eliminations();
		
		if(DEBUG)
			print_eliminations();

		if(DEBUG)
			printNetworkInfo();
		
		LinkedList<String> solve_teams = new LinkedList<>();
		boolean select_solve = false;

        /* Restrict to only solving the non-trivial elimination to
         * those specified in the baseball.solve property, if present */
		try {
			String names = System.getProperty("baseball.solve");
			if(names != null && !names.equals(""))
			{
				if(names.contains(" "))
				{
					solve_teams.addAll(Arrays.asList(names.split(" ")));
				} else if(names.contains(",")) {
					solve_teams.addAll(Arrays.asList(names.split(",")));
				} else {
					solve_teams.add(names);
				}
				
				for(String input_name : solve_teams)
				{
					if(!team_lookup.contains(input_name))
					{
						throw new IllegalArgumentException("Invalid teamname in baseball.solve property: " + input_name + "\nUse comma or space to separate multiple names.");
					}
				}
				select_solve = true;
			}
		} catch(SecurityException e) {
			will_debug = false;
		}	
		
        // default, solve for all teams.
		if(!select_solve)
		{
			solve_teams.clear();
			for(String team_name : teams())
				solve_teams.add(team_name);
		}
		
		if(nTeams != 1)
		{
			for(String team : solve_teams)
			{
				if(DEBUG)
					StdOut.println("Building flow network for: " + team);
				test_elimination_by_flow(team);
			}		
		}
	}
	
    /** Helper function for recording elimination certificate.
     * @param victim team name that has been eliminated
     * @param by_team team name of team that that caused the elimination.
     */
	private void addElimination(String victim, String by_team)
	{
		int vec_team = team_lookup.get(victim);
		Set<String> eliminated_by = eliminates.get(vec_team);
		eliminated_by.add(by_team);
	}

    /** Helper function for recording trivial eliminations in the elimination
     * certificate.  A team is eliminated trivially if a teams possible 
     * wins (wins + remaining counted as wins) is less than any other 
     * teams win count.
     * @param team team name to test for trivial elimination.
     */
	private void addEliminatedTrivially(String team) {
		int max_wins = wins(team) + remaining(team);
		
		for(String other_team : teams())
		{
			if(team.equals(other_team))
				continue;
			
			if(max_wins < wins(other_team))
			{
				addElimination(team,other_team);
			}
		}
	}
	
    /** Helper function for determination trivial eliminations
     */
	private void determine_trivial_eliminations() {
		for(String team : teams())
		{
			addEliminatedTrivially(team);
		}
		
	}

    /** Debug function for displaying Team Vertexes and Game Vertexes in flow network. */
	private void printNetworkInfo()
	{
		StdOut.printf("Vertex %-4d is SOURCE\n",SOURCE_VERTEX);
		
		int v_team = 1;
		for(String team : teams())
		{
			StdOut.printf("Vertex %-4d is team %s\n",v_team, team);
			v_team++;
		}

		int v_team_i = 0;
		for(String team_i : teams())
		{
			v_team_i++;
			int v_team_j = 0;
			for(String team_j : teams())
			{
				v_team_j++;
				if(team_i.equals(team_j))
					continue;

				if(v_team_i > v_team_j)
					continue;
				
				int hash = hashOfGame(v_team_i,v_team_j);
				int v_game = vertexOfGame(v_team_i, v_team_j);
				
				StringBuffer output = new StringBuffer();
				
				output.append(String.format("Vertex %-4d is game (%d,%d) ",v_game, v_team_i,v_team_j));
				int i_vs_j = vs_remaining.get(v_game);
				output.append(String.format(" [r=%d]",i_vs_j));
						
				StdOut.println(output.toString());
				
			}
		}
		StdOut.printf("Vertex %-4d is SINK\n",SINK_VERTEX);
		
	}

    /** Debug function for displaying Teams elimination certificate */
	private void print_eliminations() {

		for(String team : teams())
		{
			String message;
			if(isEliminated(team))
			{
				message = String.format("Team %s eliminated { %s }\n",team,String.join(" ", certificateOfElimination(team)));
				
			} else {
				message = String.format("Team %s viable",team); 
			}
			
			StdOut.println(message);
		}
	}

    /** Builds FlowNetwork for determining the outcomes of the remaing games (excluding team @param team).
     *
     * The flownetwork is composed of the following vertexes.
     * 1) Each team is represented as a vertex. (excluding the team being tested)
     * 2) The outcome of the set of two teams is represented as a game vertex.
     * 3) A virtual source vertex and virtual sink vertex used in the maxflow calculation.
     *
     * The source vertex has an edge to each game vertex.  The capacity of the link is set to the
     * number of games remaining between the two teams represented in the game vertex.
     *
     * Each game vertex has two edges leading to the vertexes of the two teams 
     * represented in the game vertex.
     *
     * Each team vertex has an edge leading to the sink vertex.  The capacity 
     * of this edge represents the number of games that team can still win.  
     * It is a function of the team this network is being constructed for.  
     * Wins[team] + Remaining[team] - Wins[i].
     *
     * @param team to build flow network to test for elimination.
     */
	private FlowNetwork buildNetworkAgainstTeam(String team)
	{
		FlowNetwork network = new FlowNetwork(SINK_VERTEX + 1);
		int v_team_test = team_lookup.get(team);
		
		int wins_possible = wins(team) + remaining(team);
		
		if(DEBUG)
		{
			StdOut.printf("Team %s(%d) has %d possible wins.\n",team,v_team_test,wins_possible);
		}
		
		int nTeams = numberOfTeams();

		int v_team_i = 1;
		int v_team_j = 2;
		
		// Starting vertex of the game nodes is the vertex after the last team node
		int vertex_game = nTeams; 

        // Permutation of game vertexes
		do {
			v_team_j = v_team_i + 1;
		
			do {
				int hash = hashOfGame(v_team_i,v_team_j);
				assert lookupGame.containsKey(hash);				
				vertex_game++;
				int v_game = vertexOfGame(v_team_i,v_team_j);
				assert v_game == vertex_game;

				if(v_team_j == v_team_test || v_team_i == v_team_test)
					continue;
				
				int i_vs_j = vs_remaining.get(v_game);				
				
				network.addEdge(new FlowEdge(SOURCE_VERTEX,v_game,i_vs_j));
				
				network.addEdge(new FlowEdge(v_game,v_team_i,Double.POSITIVE_INFINITY));
				network.addEdge(new FlowEdge(v_game,v_team_j,Double.POSITIVE_INFINITY));

				
			} while(++v_team_j <= nTeams);
		} while(++v_team_i <= nTeams-1);

		int v_team = 0;
		for(String other : teams())
		{
			v_team++;
			if(team.equals(other))
				continue;
			
			int other_wins_possible = wins_possible - wins(other);
			
			if(DEBUG)
				StdOut.printf("%d: %s -> SINK %d\n",v_team,other,other_wins_possible);
		
			if(other_wins_possible >= 0)
				network.addEdge(new FlowEdge(v_team,SINK_VERTEX,other_wins_possible));
			else {
				addElimination(team, other);
			}
		}

		if(DEBUG && DEBUG_NETWORK)
        {
            StdOut.println("FlowNetwork for " + team);
			StdOut.println(network);
        }
		
		return network;
	}

    /** Constructs a FlowNetwork, and solves the min-cut using the FordFulkerson
     * algorithm to determine if a team is mathematically eliminated.
     *
     * In the residual capacity network computed, the maximum flow is computed.  If all
     * edges in the maxflow are full, then this corresponds to assigning winners to all
     * of the remaining games in such a way that no team wins more games than @param team.
     *
     * If some edges pointing from the SOURCE_VERTEX are not full, then there is no scenario
     * in which team x can win the division.
     *
     * An st-cut in a flownetwork partitions each vertex in a network into two disjoint sets.
     * A min-cut assigns the source vertex in one set, and the sink vertex in the outher.  The
     * value of the cut is the summation of the flow that crosses the cut.  The min-cut finds
     * a set a vertexes of minimal value.
     *
     * The min-cut is used 
     *
     * @param team to test for elimination.
     */
	private void test_elimination_by_flow(String team) {
		FlowNetwork network = buildNetworkAgainstTeam(team);
		
		FordFulkerson max = new FordFulkerson(network,SOURCE_VERTEX,SINK_VERTEX);

		if(DEBUG && DEBUG_NETWORK)
        {
            StdOut.println("Maximum Flow for " + team + " after FordFulkerson");
			StdOut.println(network);
        }

		Set<Integer> min_cut = new LinkedHashSet<Integer>();
		for(int v = 1; v <= numberOfTeams();v++)
			if(max.inCut(v))
			{
				min_cut.add(v);
				addElimination(team, teams[v]);
			}
		
		if(DEBUG)
		{
			StdOut.println("Vertexes in min-cut: " + min_cut);
		}

		Set<String> candidate = new HashSet<String>();
		
        /* For edges from the source vertex check edges from source 
         * vertex have flow == capacity
         *
         * If the flow is not at capacity, then the teams in the
         * matchup of the game vertex maybe a candidate for elimination.
         *
         * If all edges are full, then flow is feasible.
         */
		boolean feasible = true;
		for(FlowEdge edge : network.adj(SOURCE_VERTEX))
		{
			int vertex_to = edge.to();
			int vertex_from = edge.from();
			
			if(edge.residualCapacityTo(vertex_to) == 0)
			{ // if edge from source is full
				if(DEBUG && DEBUG_NETWORK)
					StdOut.println("e    full: " + edge);
			} else {
				if(DEBUG && DEBUG_NETWORK)
				{
					StdOut.println("e partial: " + edge);
				}
				feasible = false;
				
				int vec_game = edge.to();
				TeamPair pair = lookupTeams.get(vec_game);
				assert pair != null;
				
				candidate.add(teams[pair.getFirst()]);
				candidate.add(teams[pair.getSecond()]);
			}
		}
		

        /* Elimination detected, determine certificate of elimination.
         * For all edges leading into the sink.  If the source of the edge
         * is a candidate team vertex, then check if the team vertex has
         * more wins than possible.  If so, then the team vertex participates
         * in the certificate of elimination.
         */
		if(!feasible)
		{
			int max_wins_possible = wins(team) + remaining(team);

			if(DEBUG)
			{
				StdOut.println("Team " + team + " eliminated.");
				StdOut.println("\nDetermining Set R from team nodes:");
			}
			int vec_team = team_lookup.get(team);

			
			Set<String> eliminator = new HashSet<String>();
			
			for(FlowEdge edge : network.adj(SINK_VERTEX))
			{
				int vertex_from = edge.from();
				String by_team = teams[vertex_from];
				
				if(candidate.contains(by_team))
				{
					int wins_possible = wins(by_team) + remaining(by_team);
					if(wins_possible > max_wins_possible)
					{
						if(DEBUG)
							StdOut.println("Elimination by " + by_team);
						eliminator.add(by_team);
					}
				}
				if(DEBUG)					
					StdOut.println("Team " + by_team + " has edge: " + edge);
				
			}
			
			assert !eliminator.isEmpty();
			eliminates.get(vec_team).addAll(eliminator);
			
		}
		
	}


    /** Helper class.  Encapsulates two team vertexes.
     * Constructor ensures that the class member first is less than second. */
	private class TeamPair {
		private int first;
		private int second;

        /**
         * Team pair constructor.  May swaps arguments to ensure team_first < team_second.
         * @param team_frist team in pair membership
         * @param team_second team in pair membership
         */
		public TeamPair(int team_first, int team_second) {
			if (team_first == team_second)
				throw new IllegalArgumentException("Teams must be unqiue.");

			if (team_first > team_second) {
				int tmp = team_first;
				team_first = team_second;
				team_second = tmp;
			}

			this.first = team_first;
			this.second = team_second;
		}

        /** @return first team vertex */
		public int getFirst() {
			return first;
		}

        /** @return second team vertex */
		public int getSecond() {
			return second;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + first;
			result = prime * result + second;
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
			TeamPair other = (TeamPair) obj;
			if (first != other.first)
				return false;
			if (second != other.second)
				return false;
			return true;
		}

	}
	
    /** Helper function used during class instantiation. Initializes 
     * class members related to game vertexes based on number of teams
     * in system.
     */
	private void allocateGameNodes(int nTeams) {
		
		int i_team = 1;
		int j_team = 2;
		
		int vertex_game = nTeams;
		do {
			j_team = i_team + 1;
			do {
				int hash = hashOfGame(i_team,j_team);
				if(lookupGame.containsKey(hash))
				{
					int vert = lookupGame.get(hash);
					if(DEBUG)
						System.err.printf("Hash %d already hashed to %d.  wanting to hash to %d\n",hash,vert,vertex_game);
					assert !lookupGame.containsKey(hash);
					
				} else {
					lookupGame.put(hash, vertex_game);
				}

				
				if(lookupTeams.contains(vertex_game))
				{
					TeamPair tp = lookupTeams.get(vertex_game);
					if(DEBUG)
						System.err.printf("Game Vertex %d already mapped to teams %d, %d",vertex_game,tp.getFirst(),tp.getSecond());
				} else { 
					lookupTeams.put(vertex_game,new TeamPair(i_team,j_team));
				}
				
				assert(vertexOfGame(i_team,j_team) == vertex_game);
				vertex_game++;
			} while(++j_team < nTeams);
		} while(++i_team < nTeams-1);
	}

    /** Helper function computing game vertex.
     * @param TeamA team vertex in game vertex
     * @param TeamB team vertex in game vertex 
     * @return vertex of game between TeamA and TeamB
     */
	private int vertexOfGame(int teamA, int teamB)
	{
		int hash = hashOfGame(teamA,teamB);
		return lookupGame.get(hash);
	}
	
    /** Helper function computing hash value for game vertex.
     * @param TeamA team vertex in game vertex
     * @param TeamB team vertex in game vertex 
     * @return unique value representing game between TeamA and TeamB
     */
	private int hashOfGame(int teamA, int teamB)
	{
		verifyTeamVertex(teamA);
		verifyTeamVertex(teamB);
		
		if(teamA == teamB)
			throw new IllegalArgumentException("Team vertex " + teamA + " asked for game to itself");
		
		if(teamA > teamB)
		{
			int tmp = teamA;
			teamA = teamB;
			teamB = tmp;
		}

		return 31
				+ teamA*teams.length
				+ 5791 * teamB;
	}
	
    /** 
     * Validates team vertex.
     * @param team vertex to test
     * @throws IllegalArgumentException if team vertex is invalid.
     */
	private void verifyTeamVertex(int team) {
		if(team > 0 && team < teams.length)
			return;
		throw new IllegalArgumentException("Team vertex " + team + " should be between 0 and " + (teams.length-1));
	}

    /** @return number of teams */
	public int numberOfTeams()
	{
		return teams.length - 1;
	}

    /** @return Iterator over all team names */
	public Iterable<String> teams()
	{
		return new Iterable<String>()
				{
					final String[] it_teams = teams;
					@Override
					public Iterator<String> iterator() {
						return new Iterator<String>() {
							int curr = 0;

							@Override
							public boolean hasNext() {
								return curr < it_teams.length - 1;
							}

							@Override
							public String next() {
								return it_teams[++curr];
							}
							
						};

					}
			
				};

	}

    /** @param team to query
     * @return number of wins for team */
	public int wins(String team)
	{
		verifyTeamName(team);
		Integer vec_team = team_lookup.get(team);
		return wins.get(vec_team);
	}

    /** @param team to query
     * @return number of losses for team */
	public int losses(String team)
	{
		verifyTeamName(team);
		Integer vec_team = team_lookup.get(team);
		return losses.get(vec_team);
	}

    /** @param team to query
     * @return number of remaining games for team */
		public int remaining(String team)
	{
		verifyTeamName(team);
		Integer vec_team = team_lookup.get(team);
		return remaining.get(vec_team);
	}

    /** @return the number of games between @param team1 and @param team2. */
	public int against(String team1, String team2)
	{
		verifyTeamName(team1);
		verifyTeamName(team2);
		if(team1.equals(team2))
			return 0;
		
		Integer vec_team_i = team_lookup.get(team1);
		Integer vec_team_j = team_lookup.get(team2);
		int hash_of_game = vertexOfGame(vec_team_i,vec_team_j);
		return vs_remaining.get(hash_of_game);
	}

    /** Validates team @param name.
     * @throws NullPointerException if argument is null.
     * @throws IllegalArgumentException if name is not known.
     */
	private void verifyTeamName(String name)
	{
		if(name == null)
			throw new NullPointerException("Invalid team argument");
		
		if(!team_lookup.contains(name))
		{
			throw new IllegalArgumentException("Invalid team name: " + name);
		}
	}
	
    /** @return if @param team has been eliminated. */
	public boolean isEliminated(String team) // is given team eliminated?
	{
		verifyTeamName(team);
		int vec_team = team_lookup.get(team);
		Set<String> eliminated_by = eliminates.get(vec_team);
		return (!eliminated_by.isEmpty());
	}

    /** @return subset R of teams that eliminates given team;  null if not eliminated */
	public Iterable<String> certificateOfElimination(String team)
	{
		verifyTeamName(team);
		int vec_team = team_lookup.get(team);
		Set<String> eliminated_by = eliminates.get(vec_team);
		if(eliminated_by.isEmpty())
			return null;
		return eliminated_by;
	}
	

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);
		
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}
