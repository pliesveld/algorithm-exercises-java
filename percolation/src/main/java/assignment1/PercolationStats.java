/* vi: set ts=4 sw=4 expandtab: */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.String;
import java.lang.Math;



/*
 * Starts with a completely closed grid of cell sites, and randomly opens sites.  
 * Collects statistics on the number of steps required until system percolation is achieved.
 *
 */
public class PercolationStats {

    private double mean = 0;
    private double stddev = 0;
    private double ci_low = 0;
    private double ci_high = 0;
 
	public PercolationStats(int N, int T) {   // perform T independent experiments on an N-by-N grid

        final int MAX = N + 1;
        double[] percolate_trial = new double[T];
        double timed_exec = 0.0;


        if(N <=0 || T <= 0)
            throw new java.lang.IllegalArgumentException("Not enough arguments supplied.  Usage: PercolationStats::main <N> <T>\n");


		for(int i = 0; i < T; i++) {
            /*
             * prepare Array containing randomized indexes
             */
            int[][] unused = new int[MAX][];
            for(int i_row = 1; i_row < unused.length;i_row++)
            {
                int unused_row[] = new int[N];
                for(int j = 0; j < unused_row.length; j++)
                    unused_row[j] = j+1;
                StdRandom.shuffle(unused_row);
                unused[i_row] = unused_row;
            }

            int unused_counters[] = new int[MAX];
            unused[0] = unused_counters;

            /*
             * store indexes allowed per row
             */
            for(int i_row = 1; i_row < unused.length;i_row++)
                unused[0][i_row] = N - 1;


            Stopwatch timer = new Stopwatch();
            Percolation p = new Percolation(N);
            int sites_opened = 0;

            while(true)
            {
                int row = StdRandom.uniform(1,MAX);
                int available;
                /*
                 * If there are no more available available
                 * sites at this row, find new row.
                 */
                if((available = unused[0][row]) < 0)
                    continue;

                //StdOut.printf("row %d has available = %d\n",row,available);
                int col = unused[row][available];
                unused[0][row] = unused[0][row] - 1;

                p.open(row,col);
                sites_opened++;

                if(p.percolates())
                    break;
            }

            int sites_total = N*N;

            double obs_mean = (((double)sites_opened)/sites_total);

            timed_exec += timer.elapsedTime();
            percolate_trial[i] = obs_mean;
		}


        mean = StdStats.mean(percolate_trial);
        stddev = StdStats.stddev(percolate_trial);
        double variance = StdStats.var(percolate_trial);

        //assert (variance*variance) - stddev < 0.0001; 

        double term = (1.96*stddev) / Math.sqrt((double)T);
        ci_low = mean-term;
        ci_high = mean+term;

	}

	public double mean() {                    // sample mean of percolation threshold
        return mean;
	}
	public double stddev() {                  // sample standard deviation of percolation threshold
        return stddev;
	}
	public double confidenceLo() {            // low  endpoint of 95% confidence interval
        return ci_low;
	}
	public double confidenceHi() {            // high endpoint of 95% confidence interval
        return ci_high;
	}

	public static void main(String[] args) {  // test client (described below)
		if(args.length < 2) {
            
            throw new IllegalArgumentException("Not enough arguments supplied.  Usage: PercolationStats::main <N> <T>\n");
		}

        
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
        StdOut.printf("PercolationStats N=%d T=%d\n",N,T);

		PercolationStats p = new PercolationStats(N,T);
        StdOut.printf("%-25s = %f\n","mean",p.mean());
        StdOut.printf("%-25s = %f\n","stddev",p.stddev());
        StdOut.printf("%-25s = %f %f\n","95% confidence interval",p.confidenceLo(),p.confidenceHi());
//        StdOut.printf("%-25s = %f\n","running time",timed_exec);

	}
}
