import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;


public class MoveToFront {
	
	private static char[] initializeByteArray()
	{
    	char[] alphabet = 
    			new char[256];
    	for(char i = 0;i < alphabet.length;i++)
    	{
    		alphabet[i] = (char)i;
    	}
		return alphabet;
	}
	
	
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {	
    	char[] alphabet = initializeByteArray();

		while(!BinaryStdIn.isEmpty())
		{
			char b = BinaryStdIn.readChar();
			boolean found = false;

	    	for(char i = 0;i < alphabet.length;i++)
	    	{
	    		
	    		if(b == alphabet[i])
	    		{
	    			found = true;
	    			final char b_i = i;

	    			BinaryStdOut.write(b_i);

	    			System.arraycopy(alphabet, 0, alphabet, 1, i);
	    			if( i == 0)
	    				assert alphabet[0] == b;
	    			alphabet[0] = b;
	    			assert alphabet[0] == b;
	    			break;
	    			

	    		}
	    	}
	    	assert found;
		}
		BinaryStdOut.flush();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
    	char[] alphabet = initializeByteArray();
    	while(!BinaryStdIn.isEmpty())
    	{
    		char i = BinaryStdIn.readChar();
    		assert i >= 0 && i <= 255;
    		
    		char ch = alphabet[i];
    		BinaryStdOut.write(ch);
    		
    		System.arraycopy(alphabet, 0, alphabet, 1, i);
    		alphabet[0] = ch;
    	}
    	
    	BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
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
