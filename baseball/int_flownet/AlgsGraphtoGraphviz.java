import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;

public class AlgsGraphtoGraphviz {

	static public void print(FlowNetwork network) {
		Out out = new Out("/tmp/g.dot");
		
		
		out.println("digraph G {");
		for(FlowEdge edge : network.edges())
		{
			out.printf("%d -> %d;\n",
					edge.from(),
					edge.to());
		}
		out.println("}");
		out.close();
	}
	
}
