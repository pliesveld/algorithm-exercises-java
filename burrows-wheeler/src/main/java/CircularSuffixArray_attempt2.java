import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray_attempt2 {

	private int N;
	private int[] toOrig; 
	
	public CircularSuffixArray_attempt2(String s) // circular suffix array of s
	{
		if(s == null)
			throw new NullPointerException("Parameter s is null");

		N = s.length();
		
		List<CharArrayIndex> listString = new ArrayList<>(N);

		
		StringBuilder sb = new StringBuilder(s);
			
		for(int i = 0; i < N; i++)
		{
			listString.add(new CharArrayIndex(sb,i));
			char tmp = sb.charAt(0);
			sb.deleteCharAt(0);
			sb.append(tmp);
		}
		
		Collections.sort(listString);
		
		toOrig = new int[N];
		
		for(int i = 0; i < N; i++)
			toOrig[i] = listString.get(i).getIndex();
		

		int i = 0;
		for(CharArrayIndex cai : listString)
		{
			StdOut.printf("%-2d: xxxxxxxxx %s    %d\n",i,cai.getArray(),cai.getIndex());
			i++;
		}


	}

	public int length() // length of s
	{
		return N;
	}

	public int index(int i) // returns index of ith sorted suffix
	{
		if(i < 0 || i >= N)
			throw new IndexOutOfBoundsException("Parameter i is out of bounds.  Must be between 0 and " + (N-1));
		
		return toOrig[i];
	}

	private class CharArrayIndex
	implements Comparable<CharArrayIndex>
	
	{
		CharSequence array;
		
		private int index;
		
		public CharArrayIndex(StringBuilder array, int index)
		{
			this.array = new String(array);
			this.index = index;
		}
		
		public CharSequence getArray() {
			return array;
		}
		
		public int getIndex() {
			return index;
		}

		@Override
		public int compareTo(CharArrayIndex o) {
			for(int i = 0; i < N; i++)
			{
				int cmp = array.charAt(i) - o.array.charAt(i);
				if(cmp == 0)
					continue;
				return cmp;
			}
			return 0;
		}
	}
	
	public static void main(String[] args)// unit testing of the methods
	// (optional)
	{
		String testString = "ABRACADABRA!";
		CircularSuffixArray_attempt2 csa = new CircularSuffixArray_attempt2(testString);
		assert csa.length() == testString.length();
		assert csa.index(11) == 2;
	}

}
