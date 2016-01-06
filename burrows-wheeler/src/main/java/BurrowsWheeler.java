import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Burrows-Wheeler 
 * Transforms the input sequence such that sequences of the same character
 * appear near each other many times.
 */
public class BurrowsWheeler {
    /** Apply Burrows-Wheeler encoding, reading from standard input and writing to standard output */
    public static void encode()
    {
        String input = BinaryStdIn.readString();
        
        final int N = input.length();
        final int N_max = N - 1;
        CircularSuffixArray csa = new CircularSuffixArray(input);
        
        int first = 0;
        boolean found = false;
        for(int idx = 0;idx < csa.length();idx++)
        {
            if(csa.index(idx) == 0)
            {
                first = idx;
                found = true;
                break;
            }
        }
        
        assert found;
        
        BinaryStdOut.write(first);

        final char[] ch_bytes = input.toCharArray();
        
        for(int idx = 0;idx < csa.length();idx++)
        {
            final int ch_idx = ((csa.index(idx) - 1 + N_max)%N + 1)%N;
            final char ch_b = ch_bytes[ch_idx];
            BinaryStdOut.write(ch_b);
        }

        BinaryStdOut.flush();
       
    }

    /** Apply Burrows-Wheeler decoding, reading from standard input and writing to standard output */
    public static void decode() throws UnsupportedEncodingException
    {
        int first = BinaryStdIn.readInt();
        String input = BinaryStdIn.readString();
        char[] input_sorted = input.toCharArray();
        Arrays.sort(input_sorted);
        final int N = input.length();
        
        int[] next = new int[N];
        
        int i = 0;
        next[0] = first;
        
        while(i < N)
        {
            int i_old = i;
            int last_scan = 0;
            
            char ch_next = input.charAt(i); 
            char j_next = input_sorted[i];
            char t_i = ch_next;
 
            int j_shift = -1;
            do {
                j_shift = input.indexOf(j_next, last_scan);

                if(j_shift != -1 && i < N)
                {
                    next[i++] = j_shift;
                }

                last_scan = j_shift + 1;
            } while(j_shift != -1 && i < N);
            
        }
            
        
/******* DEBUG HEADER
        System.err.printf("%-3s %17s%6s\t\t%6s\n","i","Sorted Suffixes","t","next");
*/

/******************* DEBUG TABLE            
        i = 0;
        next[0] = first;
        
        while(i < N)
        {
            int i_old = i;
            int last_scan = 0;
                        
            int ch_next = input.charAt(i); 
            
            
            char j_next = input_sorted[i];
            char t_i = (char)ch_next;

            i++;
        
            System.err.printf("%-3d %c %17s %c\t\t%3d\n",
                i_old, j_next, "? ? ? ? ? ? ? ? ? ?",t_i,next[i_old]);
        
        }
*/
        
/******************* DEBUG DECODE        
        int i_next = next[0];
        i_next = next[i_next];
        for(int idx = 0; idx < N ; idx++)
        {
            System.err.print(input.charAt(i_next));
            i_next = next[i_next];          
        }
        System.err.println(input.charAt(next[0]));
*/
        int i_next = next[0];
        i_next = next[i_next];
        for(int idx = 0; idx < N ; idx++)
        {
            
            final char b_out = input.charAt(i_next);
            BinaryStdOut.write(b_out);
            i_next = next[i_next];
        }

        BinaryStdOut.flush();
    }
        
    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) throws UnsupportedEncodingException
    {            
        if(args.length < 1)
            throw new NullPointerException("Expected argument.");
               
        switch(args[0])
        {
        case "-":
            encode();            
            break;
        case "+":
            decode();
            break;
            
            default:
                throw new NullPointerException("Expected argument - or +");
        }
            
    }
}
