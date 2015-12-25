/* vi: set ts=4 sw=4 expandtab: */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;

public class Percolation {

	private int MAX_GRID;
	private boolean[][] site;
//  private QuickUnionUF uf;
	private WeightedQuickUnionUF uf;

	private int INDEX_TOP = 0;
	private int INDEX_BOTTOM = 1;

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

	private int indice_to_index(final int i,final int j) {
		validIndex(i,j);
		int ret = j * MAX_GRID + i;
		return ret;
	}

	private int row_of_index(final int site) {
		return site/MAX_GRID;
	}

	private int col_of_index(final int site) {
		return site%MAX_GRID;
	}

	public void open(int i, int j) {        // open site (row i, column j) if it is not open already
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

public boolean isOpen(int i, int j) {   // is site (row i, column j) open?
    validIndex(i,j);
    return site[i][j];
}

public boolean isFull(int i, int j) {   // is site (row i, column j) full?
    validIndex(i,j);
    return isOpen(i,j) && uf.connected(INDEX_TOP,indice_to_index(i,j));
}

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
