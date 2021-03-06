import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.LinkedList;

/**
 * SeamCarver takes a picture image and removes a seam from the image.
 *
 * Each pixel in the image calculate an energy value.  These values are used
 * when finding the seam to remove to pick a seam that doesn't cut through 
 * the "interesting" parts of the image.
 *
 * When a seam is selected to be removed, a EdgeWeightedGraph is computed where
 * the edge weights are the energy values of the pixels.  Then a shortest-path
 * is computed from source to sink, selected the pixels along the seam that
 * have the lowest energy cost path.
 */
public class SeamCarver {
	
	
	private Picture picture;
	private double[][] energy;
	
	
    /**
     * Constructor for SeamCarver.  Copies Picture image, and calculates initial pixel energy.
     * @param picture
     */
	public SeamCarver(Picture picture) // create a seam carver object based on
										// the given picture
	{
		this.picture = new Picture(picture);
		calc_energy_while();
	}
	
    /**
     * Calculates energy for all pixels.
     * Edge pixels have an energy value of 1000.
     */
	private void calc_energy_while()
	{
		int w = width();
		int h = height();
		energy = new double[w][h];

		
		int row = 0, col =0;
		

		for(col = 0; col < w; ++col)
		{
			energy[col][0] = 1000.0;
			energy[col][h-1] = 1000.0;
		}

		for(row = 0; row < h; ++row)
		{
			energy[0][row] = 1000.0;
			energy[w-1][row] = 1000.0;
		}

		for(row = 1; row < h-1; row++)
		{
			for(col = 1; col < w-1; col++)
			{
				double e = compute_energy_of(col,row);
				energy[col][row] = e;
			}
		}
		
	}

    /**
     * Breaks Color object into three integer RGB values
     * @param color Color extracted from image
     * @param colors array of color values in RGB
     * @return if colors is of the right length, it is returned, otherwise a new array is allocated
     */
	private int[] getRGBColorComponents(Color color, int[] colors)
	{
		int[] ret = null;
		if(colors == null || colors.length != 3)
		{
			ret = new int[3];
		} else {
			ret = colors;
		}
		
		ret[0] = color.getRed();
		ret[1] = color.getGreen();
		ret[2] = color.getBlue();
		
		return ret;
	}
	
    /**
     * Computes energy for individual pixel
     *
     * Energy of pixel (x, y) = sqrt( x-gradient(x, y) + y-gradient(x, y) )
     *
     * where x-gradient(x, y) = R_x(x,y)^2 + G_x(x,y)^2 + B_x(x,y)^2
     *
     * Where the central differences, R_x, G_x, B_x, are the differences 
     * between pixel(x + 1, y) and pixel (x - 1, y)
     *
     * Similiarly for the y-gradient.
     *
     * @param col Column of pixel
     * @param row Row of pixel
     * @return energy Value of pixel
     */
	private double compute_energy_of(int col, int row) {

		int[][] rgb_colors = new int[3][2];
		 
		rgb_colors[0] = getRGBColorComponents(picture.get(col-1, row), rgb_colors[0]);
		rgb_colors[1] = getRGBColorComponents(picture.get(col+1, row), rgb_colors[1]);
		
		double x_gradient = 0.0;
		
		for(int i_color = 0; i_color < 3; i_color++)
		{
			x_gradient += Math.pow(rgb_colors[0][i_color] - rgb_colors[1][i_color],2);
		}

		rgb_colors[0] = getRGBColorComponents(picture.get(col, row-1), rgb_colors[0]);
		rgb_colors[1] = getRGBColorComponents(picture.get(col, row+1), rgb_colors[1]);
		
		double y_gradient = 0.0;
		
		for(int i_color = 0; i_color < 3; i_color++)
		{
			y_gradient += Math.pow(rgb_colors[0][i_color] - rgb_colors[1][i_color],2);
		}
		
		return Math.sqrt(x_gradient + y_gradient);
		
	}

    /** @return Copy of picture image */
	public Picture picture() 
	{
		return new Picture(this.picture);
	}

    /** @return Current Picture Width */
	public int width()
	{
		return this.picture.width();
	}

    /** @return Current Picture Height */
	public int height()
	{
		return this.picture.height();
	}

	/**
	 * Dual-gradiuent energy function.  Measures importance of each pixel.  The heigher
	 * the energy, the less liekly that the pixel will be included as part of a seam.
     *
     * @param x column
     * @param y row
	 */
	public double energy(int x, int y) // energy of pixel at column x and row y
	{
		if(x < 0 || x >= width())
			throw new IndexOutOfBoundsException("Invalid index x:" + x + " needs to be between [0 and " + width() + ")");

		if(y < 0 || y >= height())
			throw new IndexOutOfBoundsException("Invalid index y: " + y + " needs to be between [0 and " + height() + ")");
		
		return energy[x][y];
	}

    /** Utility class, computres unique id for
     * specific pixel located at col row.
     *
     * @param col column of pixel
     * @param row row of pixel
     * @return unique id for pixel
     */
	private int vertOf(int col, int row)
	{
		if(col < 0 || col >= width())
			throw new IndexOutOfBoundsException("Invalid index col:" + col + " needs to be between [0 and " + width() + ")");

		if(row < 0 || row >= height())
			throw new IndexOutOfBoundsException("Invalid index y: " + row + " needs to be between [0 and " + height() + ")");

		return 2 + (width()*row + col);
	}
	
    /** Utility class.  Given a unique id
     * returns the pixel coordinates.
     * @param vert vertex id to compute from
     * @param coord array to store coordinates in
     * @return array contaiing pixel coordinate
     */
	private int[] fromVert(int vert, int[] coord) {
		int[] ret;
		int w = width();
		int h = height();
		
		if(coord == null || coord.length != 2)
		{
			ret = new int[2];
		} else {
			ret = coord;
		}
		
		vert = vert - 2;
		
		int col = vert%w;
		int row = (vert)/w;

		coord[0] = col;
		coord[1] = row;
		return coord;
	}

	
	private static final int SOURCE_VERTEX = 0;
	private static final int SINK_VERTEX = 1;
	
    /** Computes a vertical seam.
     *
     * A sequence of row coordinates.  Each value may not be different from the prior 
     * value by more than one.  Ie, the pixel values they represent are connected adjacently.
     *
     * The solution creates two virtual nodes at each end, with a weighted cost of 0.  Each intermediatary
     * node uses the pixel energy value as the cost in the edge weight.  Dijkstra's algorithm is then
     * computed to find the Shortest Path from the left to the right side. 
     *
     * This will be the seam pixels.
     *
     * @return vertical seam.
     */
	public int[] findVerticalSeam() // sequence of indices for vertical seam
	{

		final int height = height();
		final int width = width();
		
		if(height == 1)
			return new int[]{0};
		
		if(height == 2)
			return new int[]{0,0};
					
		int[] seam = new int[height];

		final int source_vertex_row = 0;
		final int sink_vertex_row = height - 1;
		
/*****************		
		StdOut.println("table -- vertOf(col,row)");
		for(int j = source_vertex_row; j <= sink_vertex_row;j++)
		{
			for(int i= 0;i < width;i++)
			{
				StdOut.printf("%3d", vertOf(i,j));
			}
			StdOut.println();
		}
		StdOut.println();
		
		
		StdOut.println("table -- col,row");
		for(int j = source_vertex_row; j <= sink_vertex_row;j++)
		{
			for(int i= 0;i < width;i++)
			{
				StdOut.printf("%4d,%-4d", i,j);
			}
			StdOut.println();
		}
		StdOut.println();

		
		StdOut.println("table -- energy");
		for(int j = source_vertex_row; j <= sink_vertex_row;j++)
		{
			for(int i= 0;i < width;i++)
			{
				StdOut.printf("%12.2f",energy(i,j));
			}
			StdOut.println();
		}
		StdOut.println();
**********************/		

		int[] source_vertex_candidates = new int[width];
		int[] sink_vertex_candidates = new int[width];
		
	
		int total_cells = width*height;
		
		EdgeWeightedDigraph graph = new EdgeWeightedDigraph(total_cells + 2);
		
		for(int i= 0;i < width;i++)
		{
			source_vertex_candidates[i] = i;
			sink_vertex_candidates[i] = i;
			
			for(int j = source_vertex_row; j < sink_vertex_row;j++)
			{
				int head = vertOf(i,j);
				
				int adj[];
				if(width == 1) {
					adj = new int[]{0};
				} else if(i == 0) {
					adj = new int[]{i,i+1};
					
				} else if(i == width - 1) {
					adj = new int[]{i-1,i};
				} else {
					adj = new int[]{i-1,i,i+1};
				
				}
				
				for(int next_i : adj)
				{
					int tail = vertOf(next_i,j+1);
					
					double cost = energy(next_i,j+1);
					graph.addEdge(new DirectedEdge(head,tail,cost));					
				}
			}
			
			int tail = vertOf(source_vertex_candidates[i],source_vertex_row);
			graph.addEdge(new DirectedEdge(SOURCE_VERTEX,tail,0));
			int head = vertOf(sink_vertex_candidates[i],sink_vertex_row);
			graph.addEdge(new DirectedEdge(head,SINK_VERTEX,0));
		}
		
		DijkstraSP dijkstra = new DijkstraSP(graph,SOURCE_VERTEX);
		
		int[] coord = new int[2];
		int seam_idx = 0;
		
		LinkedList<DirectedEdge> path = new LinkedList<DirectedEdge>();

		


		
		int count = 0;
		for(DirectedEdge edge : dijkstra.pathTo(SINK_VERTEX))
		{
			path.add(edge);
//			StdOut.println("Edge in path: " + edge);
			count++;
		}
		path.remove(path.size()-1);

		
		for(DirectedEdge edge : path)
		{
			coord = fromVert(edge.to(),coord);

			int col = coord[0];
			int row = coord[1]; 
//			StdOut.println("Edge: "  + edge + "row=" + row + " col=" + col);			
			
			seam[seam_idx] = col;
			seam_idx++;
		}
		
		/*
		StdOut.print("{");
		for(int IDX : seam)
		{
			StdOut.print(" " + IDX + " ");
		}
		StdOut.println("}");
		*/
		return seam;
	}

	
    /** Computes a horizontal seam.
     *
     * A sequence of col coordinates.  Each value may not be different from the prior 
     * value by more than one.  Ie, the pixel values they represent are connected adjacently.
     *
     * The solution creates two virtual nodes at each end, with a weighted cost of 0.  Each intermediatary
     * node uses the pixel energy value as the cost in the edge weight.  Dijkstra's algorithm is then
     * computed to find the Shortest Path from the top to the bottom side. 
     *
     * This will be the seam pixels.
     *
     * @return horizontal seam.
     */

	public int[] findHorizontalSeam() // sequence of indices for horizontal seam
	{
		final int height = height();
		final int width = width();
				
		if(width == 1)
			return new int[]{0};
		
		if(width == 2)
			return new int[]{0,0};
		
		int[] seam = new int[width];
		
		int[] source_vertex_candidates = new int[height];
		int[] sink_vertex_candidates = new int[height];

		final int source_vertex_col = 0;
		final int sink_vertex_col = width - 1;

/*
		StdOut.println("table -- vertOf(col,row)");
		for(int i= 0;i < height;i++)
		{
		        for(int j = source_vertex_col; j <= sink_vertex_col;j++)
			{
				StdOut.printf("%3d", vertOf(j,i));
			}
			StdOut.println();
		}
		StdOut.println();
		
		
		StdOut.println("table -- col,row");
		for(int i= 0;i < height;i++)
		{
			for(int j = source_vertex_col; j <= sink_vertex_col;j++)
			{
				StdOut.printf("%4d,%-4d", j,i);
			}
			StdOut.println();
		}
		StdOut.println();

		
		StdOut.println("table -- energy");
		for(int i= 0;i < height;i++)
		{
			for(int j = source_vertex_col; j <= sink_vertex_col;j++)
			{
				StdOut.printf("%12.2f",energy(j,i));
			}
			StdOut.println();
		}
		StdOut.println();


*/
	
		int total_cells = width*height;
		
		EdgeWeightedDigraph graph = new EdgeWeightedDigraph(total_cells + 2);
		
		for(int i= 0;i < height;i++)
		{
			source_vertex_candidates[i] = i;
			sink_vertex_candidates[i] = i;
			
			for(int j = source_vertex_col; j < sink_vertex_col;j++)
			{
				int head = vertOf(j,i);
				
				int adj[];
				if(height == 1) {
					adj = new int[]{0};
				} else if(i == 0) {
					adj = new int[]{i,i+1};
					
				} else if(i == height - 1) {
					adj = new int[]{i-1,i};
				} else {
					adj = new int[]{i-1,i,i+1};
				
				}
				
				for(int next_i : adj)
				{
					int tail = vertOf(j + 1, next_i);
					
					double cost = energy(j + 1, next_i);
					graph.addEdge(new DirectedEdge(head,tail,cost));					
				}
			}
			
			int tail = vertOf(source_vertex_col,source_vertex_candidates[i]);
			graph.addEdge(new DirectedEdge(SOURCE_VERTEX,tail,0));
			int head = vertOf(sink_vertex_col,sink_vertex_candidates[i]);
			graph.addEdge(new DirectedEdge(head,SINK_VERTEX,0));
		}

		//StdOut.println("Graph\n" + graph);
		
		DijkstraSP dijkstra = new DijkstraSP(graph,SOURCE_VERTEX);
		
		int[] coord = new int[2];
		int seam_idx = 0;
		
		LinkedList<DirectedEdge> path = new LinkedList<DirectedEdge>();

		


		
		int count = 0;
		for(DirectedEdge edge : dijkstra.pathTo(SINK_VERTEX))
		{
			path.add(edge);
	//		StdOut.println("Edge in path: " + edge);
			count++;
		}
		path.remove(path.size()-1);

		
		for(DirectedEdge edge : path)
		{
			coord = fromVert(edge.to(),coord);

			int col = coord[0];
			int row = coord[1]; 
	//		StdOut.println("Edge: "  + edge + "row=" + row + " col=" + col);			
			
			seam[seam_idx] = row;
			seam_idx++;
		}
		
/*
		StdOut.print("{");
		for(int IDX : seam)
		{
			StdOut.print(" " + IDX + " ");
		}
		StdOut.println("}");
*/		
	

		return seam;
	}


    /**
     * Removes seam.
     *
     * Makes image copy, pixel per pixel, when seam is encountered
     * adjust src to skip pixel.
     * @param seam array of seam coordinates
     */
	public void removeHorizontalSeam(int[] seam) // remove horizontal seam from
													// current picture
	{
		if(seam == null)
			throw new NullPointerException("Null argument");
		
		if(height() <= 1)
			throw new IllegalArgumentException("Picture has an invalid height");
		
		verifyHorizontalSeam(seam,width(),height());
		
		int row = 0, col = 0;
		int width = width();
		int height = height();
		
		Picture src = this.picture;
		Picture dest = new Picture(width,height-1);
		
		for(col = 0; col < width; col++)
		{

			int skip_row = seam[col];
			int dst_row = 0;
//			StdOut.println("Skipping row " + skip_row);
			for(row = 0;row < height;row++)
			{
				int src_col = col;
				int src_row = row;
				int dst_col = col;


				if(row != skip_row )
				{
//					StdOut.printf("Copying to dest[%3d,%3d] from src[%3d,%3d]\n",dst_col,dst_row,src_col,src_row);
					Color color = src.get(src_col, src_row);
					dest.set(dst_col, dst_row, color);
					dst_row++;				
				}
			}
		}

		this.picture = dest;
		calc_energy_while();


	}

    /**
     * Removes seam.
     *
     * Makes image copy, pixel per pixel, when seam is encountered
     * adjust src to skip pixel.
     * @param seam array of seam coordinates
     */
	public void removeVerticalSeam(int[] seam) // remove vertical seam from
												// current picture
	{
		if(seam == null)
			throw new NullPointerException("Null argument");
		
		if(width() <= 1)
			throw new IllegalArgumentException("Picture has an invalid width");
		
		verifyVerticalSeam(seam,width(),height());

		int row = 0, col = 0;
		int width = width();
		int height = height();
		
		Picture src = this.picture;
		Picture dest = new Picture(width-1,height);

		for(row = 0;row < height;row++)
		{

			int skip_col = seam[row];
			int dst_col = 0;
//			StdOut.println("Skipping col " + skip_col);
	
			for(col = 0; col < width; col++)
			{
				int src_col = col;
				int src_row = row;
				int dst_row = row;
				
				
				if(col != skip_col)
				{
//					StdOut.printf("Copying to dest[%3d,%3d] from src[%3d,%3d]\n",dst_col,dst_row,src_col,src_row);
					Color color = src.get(src_col, src_row);
					dest.set(dst_col, dst_row, color);
					dst_col++;
				}
			}
		}

		this.picture = dest;
		calc_energy_while();
	}

    /** Utility function verifies seam is valid.
     * @param seam to check
     * @param width of seam
     * @param height of seam
     */
	private void verifyVerticalSeam(int[] seam,int width, int height) {
		verifySeamArray(seam,false,width,height);
	}

    /** Utility function verifies seam is valid.
     * @param seam to check
     * @param width of seam
     * @param height of seam
     */
	private void verifyHorizontalSeam(int[] seam,int width, int height) {
		verifySeamArray(seam,true,width,height);
	}
	
	/**
	 * A seam is invalid if the length of the array is invalid or if the array is not a valid 
	 * seam.  ( either an entry is outside its prescribed range or two adjacent entries differ by more than 1 )
     * @param seam to check
     * @param direction of seam, true = horizontal, false = vertical
     * @param width of seam
     * @param height of seam
	 */
	private void verifySeamArray(int[] seam, boolean dir, int width, int height) {
		
		int expected_seam_len = 0;
		int max_entry = 0;
		
		if(dir == true)
		{
			expected_seam_len = width;
			max_entry = height - 1;
		} else {
			expected_seam_len = height;
			max_entry = width - 1;
		}
		
		
		if(seam.length != expected_seam_len)
			throw new IllegalArgumentException("Invalid array length: " + seam.length + " should be " + expected_seam_len);
		
		int old = -1;
		for(int next : seam)
		{
			if(next < 0 || next > max_entry)
			{
				String message = String.format("Invalid array entry: %d.  Entries must in in range [0,%d]", next, max_entry);
				throw new IllegalArgumentException(message);
			}
			
			if(old != -1)
			{
				if( Math.abs(old - next) > 1)
				{
					throw new IllegalArgumentException("Invalid array entry: previous=" + old + " next=" + next);
				}
			}
				
			old = next;		
		}
	}
}
