import java.util.Arrays;
import java.util.Comparator;


/**
 * Circular Suffix Array generates a sorted array of N circular circular 
 * suffixes of a string of length N.
 *
 * Given the input string of ABC
 * The following suffix arrays are generated:
 *
 * ABC
 * BCA
 * CAB
 * 
 */
public class CircularSuffixArray {

	final private int N;
	final private int[] toOrig; 
	
	final Comparator<char[]> charComparator = new Comparator<char[]>()
			{
				@Override
				public int compare(char[] o1, char[] o2) {
					for(int idx = 0;idx < o1.length;idx++)
					{
						int cmp = o1[idx] - o2[idx];
						if(cmp == 0)
							continue;
						return cmp;
					}
					return 0;
				}
			
			};
	
    /** Constructor generates a circular suffix array 
     * @param s string to generate suffix array from
     */
	public CircularSuffixArray(String s) // circular suffix array of s
	{
		if(s == null)
			throw new NullPointerException("Parameter s is null");

		N = s.length();
	
		
		char[] src_chars = s.toCharArray();
		
		char[][] mat_char = new char[N][];

		
		for(char idx = 0 ; idx < N; idx++)
		{
			char[] b_arr = new char[N+1];
			b_arr[N] = idx;
			
			System.arraycopy(src_chars, idx, b_arr, 0, N-idx);
			System.arraycopy(src_chars, 0, b_arr, N-idx, idx);
			mat_char[idx] = b_arr;

		}
		
		Arrays.sort(mat_char, charComparator);

		toOrig = new int[N];

		for(int i = 0; i < N; i++)
		{
			toOrig[i] = mat_char[i][N];
		}
			
/*
		for(int i = 0; i < N; i++)
		{
			int s_idx = (int)mat_char[i][N];
			System.err.printf("%-2d: xxxxxxxxx     %d\n",i,s_idx);

		}
*/

	}

    /** @return length of input string */
	public int length() // length of s
	{
		return N;
	}

    /**
     * Maps index from sorted array to original array.
     * @param i index in sorted array
     * @return index of ith sorted suffix
     */
	public int index(int i)
	{
		if(i < 0 || i >= N)
			throw new IndexOutOfBoundsException("Parameter i is out of bounds.  Must be between 0 and " + (N-1));
		
		return toOrig[i];
	}

	public static void main(String[] args)
	{
		String testString = "ABRACADABRA!";
		CircularSuffixArray csa = new CircularSuffixArray(testString);
		//assert csa.length() == testString.length();
	}

}
