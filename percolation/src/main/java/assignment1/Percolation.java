/* vi: set ts=4 sw=4 expandtab: */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;

/*
 * Imagine a cross-cut section of earth.  Overlay a 2-d grid.  Begin a process to sequentionally dig out 
 * squares randomly.   Assume that the digging process only effects dirt in that square. ( dirt does observe gravity )
 *
 * Water is poured from the top, and fills only empty squares.  Water can transfer to adjacent squares.  The goal of
 * this program is to determine if a given square receives water.  Is there a path back to the source ( top ) that connects 
 * a given square to any empty square in the top row.
 * 
 * The algorithm chosen is an online algorithm ( can compute as it goes, as supposed to first pre-processing the state )
 * of disjoined sets, called QuickUnionFind.
 *
 *
 * In the following example X represents closed:
 *
 *
 * |X|X| |X|
 * ---------
 * |X| | |X|
 * ---------
 * |X|X|X| |
 *
 *
 * To determine if there exists a flow from top to bottom, two virtual components are created, and connected to all
 * members of the top and bottom rows respectively. 
 *
 *   Source
 *  / / \ \
 * |X|X| |X|
 * ---------
 * |X| | |X|
 * ---------
 * |X|X|X| |
 * \ \  / /
 *   Sink
 *
 *
 * As slots are opened, they are connected to adjacent already opened slots.  Those links
 * are recorded in the UnionFind.  To determine if the system percolates ( a path exists from the top 
 * row to the bottom ), it simply tests if the Source and Sink are connected ( part of the same component )
 *
 *
 */
public class Percolation {

	private int MAX_GRID;               // 2D grid dimensions

	private boolean[][] site;           // maintains if a cell site has been opened
   
//  private QuickUnionUF uf; // Unoptimized version

	private WeightedQuickUnionUF uf;

	private int INDEX_TOP = 0;          // virtual index at top connected to all top row cell sites
	private int INDEX_BOTTOM = 1;       // virtual index at bottom connected to all bottom row cell sites

	public Percolation(int N) {
		// create N-by-N site, with all sites blocked
		if(N <= 0)
			throw new IllegalArgumentException("N should be positive");
		MAX_GRID = N+1;
		site = new boolean[MAX_GRID][MAX_GRID];

		//wuf = new WeightedQuickUnionUF(MAX_GRID);
		uf = new WeightedQuickUnionUF(MAX_GRID*MAX_GRID);

		for(int i = 1; i < MAX_GRID; ++i)
			for(int j = 1; j < MAX_GRID; ++j) {
				site[i][j] = false;
			}
		//StdOut.printf("Percolation(%d)\n",MAX_GRID);

	}

    /**
     * Return the unique id that represents the cell
     * located at row i, and column j.
     * @param i row
     * @param j column
     */
	private int indice_to_index(final int i,final int j) {
		validIndex(i,j);
		int ret = j * MAX_GRID + i;
		return ret;
	}

    /**
     * Given a cells id, return the row of the cell
     * @param site cell id
     */
	private int row_of_index(final int site) {
		return site/MAX_GRID;
	}

    /**
     * Given a cells id, return the col of the cell
     * @param site cell id
     */
	private int col_of_index(final int site) {
		return site%MAX_GRID;
	}

    /**
     * Opens the location at the intersection of row i and column j.
     *
     * @param i row
     * @param j column
     */
	public void open(int i, int j) {        // open site (row i, column j) if it is not open already
        /*
         * The follow has been unrolled for performance.
         */
		//StdOut.printf("Percolation::open(%d,%d)\n",i,j);
		validIndex(i,j);
		if(site[i][j])
			return;  /* site already open */

		site[i][j] = true;

        int new_site = indice_to_index(i,j);

        if(MAX_GRID == 2)
        {
            uf.union(INDEX_TOP,new_site);
            uf.union(INDEX_BOTTOM,new_site);
            return;
        }

		if(i == 1) {
            uf.union(INDEX_TOP,new_site);
            INDEX_TOP = uf.find(INDEX_TOP);

            if(j == 1) {                     /* Upper-left */
                /* two adjacent sites */
                {
                    int u_i = i+1, u_j = 1;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = 1, u_j = j+1;
                    open_aux(new_site,u_i,u_j);
                }
            } else if( j == MAX_GRID - 1) { /* Upper-right */
                /* two adjacent sites */
                {
                    int u_i = i, u_j = j-1;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i+1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
             } else {                        /* Upper-middle */
                /* three adjacent sites */
                {
                    int u_i = 1, u_j = j+1;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = 1, u_j = j-1;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i+1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
              }

        } else if( i == MAX_GRID - 1 ) {
            uf.union(INDEX_BOTTOM,new_site);
            INDEX_BOTTOM = uf.find(INDEX_BOTTOM);
            if(j == 1) {                     /* Lower-left */
                /* two adjacent sites */
                {
                    int u_i = i-1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i, u_j = j+1;
                    open_aux(new_site,u_i,u_j);
                }
             } else if( j == MAX_GRID - 1) { /* Lower-right */
                /* two adjacent sites */
                {
                    int u_i = i-1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i, u_j = j-1;
                    open_aux(new_site,u_i,u_j);
                }
            } else {                        /* Lower-middle */
                /* three adjacent sites */
                {
                    int u_i = i, u_j = j-1;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i, u_j = j+1;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i-1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
            }
        } else {
            if(j == 1) {                     /* Middle-left  */
                /* three adjacent sites */
                {
                    int u_i = i, u_j = j+1;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i+1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i-1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
            } else if( j == MAX_GRID - 1) { /* Middle-right */
                /* three adjacent sites */
                {
                    int u_i = i, u_j = j-1;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i-1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i+1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
             } else {                        /* Middle-middle */
                /* four adjacent sites */
                {
                    int u_i = i+1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i-1, u_j = j;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i, u_j = j-1;
                    open_aux(new_site,u_i,u_j);
                }
                {
                    int u_i = i, u_j = j+1;
                    open_aux(new_site,u_i,u_j);
                }
                   
            }
        }
    }

    /**
     * Helper method for opening a cell.  u_i, u_j are the row and columns
     * of a site adjacent to new_site.  If this newly opened site that is
     * adjacent to another site that is open, add two components together in
     * side the UnionFind algorithm.
     *
     * @param new_site id of cell just opened
     * @param u_i  row of cell adjacent to new_site
     * @param u_j  column of coll adjacent to new_site
     */
    private void open_aux(final int new_site,final int u_i,final int u_j)
    {
        boolean is_opened = true;
        if(isOpen(u_i,u_j))  {
            int u_site = indice_to_index(u_i,u_j);
            int par_new = uf.find(new_site);
            int par_u = uf.find(u_site);
            uf.union(new_site,u_site);
            uf.union(par_new,par_u);
        }

    }

    /**
     * Is the cell located at row i and column j open?
     * @param i row
     * @param j column
     */
    public boolean isOpen(int i, int j) {   // is site (row i, column j) open?
        validIndex(i,j);
        return site[i][j];
    }

    /**
     * Is the cell located at row i and column j connected to an opened 
     * cell in the top row.
     * @param i row
     * @param j column
     */
    public boolean isFull(int i, int j) {   // is site (row i, column j) full?
        validIndex(i,j);
        return isOpen(i,j) && uf.connected(INDEX_TOP,indice_to_index(i,j));
    }

    /**
     * Does the system percolate?
     */
    public boolean percolates() {           // does the system percolate?
        return uf.connected(INDEX_TOP,INDEX_BOTTOM);
    }

    private void validIndex(final int i, final int j) {
        if( i > 0 && i < MAX_GRID )
            if( j > 0 && j < MAX_GRID )
                return;

        throw new IndexOutOfBoundsException("Invalid index" + i + "," + j);
    }

    public static void main(String[] args) { // test client (optional)
        int N = StdIn.readInt();
        Percolation p = new Percolation(N);

        StdOut.printf("Grid size %d\n",N);

        while(!StdIn.isEmpty()) {
            int i = StdIn.readInt();
            int j = StdIn.readInt();
            p.open(i,j);
        }

        StdOut.printf("Percolates = %b\n",p.percolates());

    }
}
