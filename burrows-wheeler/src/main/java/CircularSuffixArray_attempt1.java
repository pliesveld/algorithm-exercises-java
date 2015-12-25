import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray_attempt1 {

	private int N;
	private int[] toOrig; 
	
	public CircularSuffixArray_attempt1(String s) // circular suffix array of s
	{
		if(s == null)
			throw new NullPointerException("Parameter s is null");

		N = s.length();
		
		List<CharArrayIndex> listString = new ArrayList<>(N);
			
		char[] ch_arr = s.toCharArray();
		
		for(int i = 0; i < N; i++)
		{
			StringBuilder sb = new StringBuilder(N);
			sb.append(ch_arr,i,N-i).append(ch_arr,0,i);
			listString.add(new CharArrayIndex(sb,i));
		}
		
		Collections.sort(listString);
		
		toOrig = new int[N];
		
		for(int i = 0; i < N; i++)
			toOrig[i] = listString.get(i).getIndex();
		
		/*
		for(CharArrayIndex cai : listString)
			StdOut.println(cai.getArray());
		 */
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

	public static void main(String[] args)// unit testing of the methods
											// (optional)
	{
		String testString = "ABC";
		CircularSuffixArray_attempt1 csa = new CircularSuffixArray_attempt1(testString);
		assert csa.length() == testString.length();
	}
	
	private class CharArrayIndex
	implements Comparable<CharArrayIndex>
	
	{
		CharSequence array;
		
		private int index;
		
		public CharArrayIndex(CharSequence array, int index)
		{
			this.array = array;
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
				int cmp = array.charAt(0) - o.array.charAt(0);
				if(cmp == 0)
					continue;
				return cmp;
			}
			return 0;
		}
	}
}
