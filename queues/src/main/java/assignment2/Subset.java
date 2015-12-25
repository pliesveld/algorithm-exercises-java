/* vi: set ts=4 sw=4 expandtab: */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    /**
     * Unit tests
     */
    public static void main(String[] args)
    {
        if(args.length < 1) 
            throw new IllegalArgumentException("Not enough arguments supplied.  Usage: Subset <k>\n");

        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> q = new RandomizedQueue<String>();
        int N = 0;

        while(!StdIn.isEmpty())
        {
            String s = StdIn.readString();
            q.enqueue(s);
            N++;
        }

        assert k <= N;
        if(k > N)
        {
            throw new IllegalArgumentException("Supplied argument k needs to be larger than number of strings N read from std in.");
        }

        for(int i=0;i < k;++i)
        {
            StdOut.println(q.dequeue());
        }

    }
}
