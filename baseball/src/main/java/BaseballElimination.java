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


public class BaseballElimination // create a baseball division from given
									// filename in format specified below
{
	private ST<String,Integer> team_lookup = new ST<>(); // team -> vertex
	private String[] teams; // Index 0 is empty
	
	private final boolean DEBUG;
	
	private static final int SOURCE_VERTEX = 0;
	private final int SINK_VERTEX; // Last vertex in graph
	
	// key is from vertex of team.  First team is indexed starting at 1.
	private ST<Integer, Integer> wins = new ST<>();
	private ST<Integer, Integer> losses = new ST<>();
	private ST<Integer, Integer> remaining = new ST<>();
	
	// key is from vertexOfGame( team_i, team_j )
	private ST<Integer, Integer> vs_remaining = new ST<>();
	
	// Game nodes
	private Map<Integer,Integer> lookupGame = new HashMap<>();
	
	private ArrayList<Set<String>> eliminates;
	
	// key is from vertexOfGame( team_i, team_j )
	// value is TeamPair class encapsulating team_i, and team_j
	private ST<Integer, TeamPair> lookupTeams = new ST<>();
	
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		
		int i_team = 0;
		
		int nTeams = in.readInt();
		
		eliminates = new ArrayList<>(nTeams + 1);
		eliminates.add(new LinkedHashSet<String>());
		
		teams = new String[nTeams + 1];
		
		int V =  1 //Artificial Source vertex
				+ 1 //Artificial Sink vertex
				+ nTeams //Team Vertex,  nTeams total
				+ (nTeams * (nTeams - 1))/2; // Game vertex, nTeams choose 2
		
		SINK_VERTEX = V-1;
		

		if(nTeams != 1)
			allocateGameNodes(nTeams + 1);
		
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

		boolean will_debug = false;
		try {
			will_debug = Boolean.valueOf(System.getProperty("baseball.debug"));
		}catch(SecurityException e) {
			will_debug = false;
		}		
		DEBUG = will_debug;
		
		determine_trivial_eliminations();
		
		if(DEBUG)
			print_eliminations();

		if(DEBUG)
			printNetworkInfo();
		
		LinkedList<String> solve_teams = new LinkedList<>();
		boolean select_solve = false;
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
	
	private void addElimination(String victim, String by_team)
	{
		int vec_team = team_lookup.get(victim);
		Set<String> eliminated_by = eliminates.get(vec_team);
		eliminated_by.add(by_team);
	}

	private void addEliminatedTrivially(String team) {
		//int vec_team = team_lookup.get(team);
		//Set<String> eliminated_by = eliminates.get(vec_team);
		
		int max_wins = wins(team) + remaining(team);
		
		for(String other_team : teams())
		{
			if(team.equals(other_team))
				continue;
			
			if(max_wins < wins(other_team))
			{
				addElimination(team,other_team);
				//eliminated_by.add(other_team);
			}
		}
	}
	
	private void determine_trivial_eliminations() {
		for(String team : teams())
		{
			addEliminatedTrivially(team);
		}
		
	}

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

		if(DEBUG && false)
			StdOut.println(network);
		
		return network;
	}

	private void test_elimination_by_flow(String team) {
		FlowNetwork network = buildNetworkAgainstTeam(team);
		
		FordFulkerson max = new FordFulkerson(network,SOURCE_VERTEX,SINK_VERTEX);

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
		
		boolean feasable = true;
		for(FlowEdge edge : network.adj(SOURCE_VERTEX))
		{
			int vertex_to = edge.to();
			int vertex_from = edge.from();
			
			if(edge.residualCapacityTo(vertex_to) == 0)
			{ // if edge from source is full
				if(DEBUG && false)
					StdOut.println("e    full: " + edge);
			} else {
				if(DEBUG && false)
				{
					StdOut.println("e partial: " + edge);
				}
				feasable = false;
				
				int vec_game = edge.to();
				TeamPair pair = lookupTeams.get(vec_game);
				assert pair != null;
				
				candidate.add(teams[pair.getFirst()]);
				candidate.add(teams[pair.getSecond()]);
			}
		}
		


		
		if(!feasable)
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


	private class TeamPair {
		private int first;
		private int second;

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

		public int getFirst() {
			return first;
		}

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

	
	private int vertexOfGame(int teamA, int teamB)
	{
		int hash = hashOfGame(teamA,teamB);
		return lookupGame.get(hash);
	}
	
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
	
	private void verifyTeamVertex(int team) {
		if(team > 0 && team < teams.length)
			return;
		throw new IllegalArgumentException("Team vertex " + team + " should be between 0 and " + (teams.length-1));
	}

	public int numberOfTeams() // number of teams
	{
		return teams.length - 1;
	}

	public Iterable<String> teams() // all teams
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

	public int wins(String team) // number of wins for given team
	{
		verifyTeamName(team);
		Integer vec_team = team_lookup.get(team);
		return wins.get(vec_team);
	}

	public int losses(String team) // number of losses for given team
	{
		verifyTeamName(team);
		Integer vec_team = team_lookup.get(team);
		return losses.get(vec_team);
	}

	public int remaining(String team) // number of remaining games for given
										// team
	{
		verifyTeamName(team);
		Integer vec_team = team_lookup.get(team);
		return remaining.get(vec_team);
	}

	public int against(String team1, String team2) // number of remaining games
													// between team1 and team2
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

	private void verifyTeamName(String name)
	{
		if(name == null)
			throw new NullPointerException("Invalid team argument");
		
		if(!team_lookup.contains(name))
		{
			throw new IllegalArgumentException("Invalid team name: " + name);
		}
	}
	
	public boolean isEliminated(String team) // is given team eliminated?
	{
		verifyTeamName(team);
		int vec_team = team_lookup.get(team);
		Set<String> eliminated_by = eliminates.get(vec_team);
		return (!eliminated_by.isEmpty());
	}



	public Iterable<String> certificateOfElimination(String team) // subset R of
																	// teams
																	// that
																	// eliminates
																	// given
																	// team;
																	// null if
																	// not
																	// eliminated
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
