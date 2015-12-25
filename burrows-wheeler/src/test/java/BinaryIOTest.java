import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import edu.princeton.cs.algs4.BinaryIn;
import edu.princeton.cs.algs4.BinaryOut;
import edu.princeton.cs.algs4.BinaryStdIn;

public class BinaryIOTest {

	
	Path openResourceFile(String filename)
	{
		Path ret = null;
		try {
			ret = Paths.get(ClassLoader.getSystemResource(filename).toURI());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}
		
		return ret;
	}
	
	
	String fileResourcePath(String filename)
	{
		Path filePath = openResourceFile(filename);
		String filePathStr = filePath.toAbsolutePath().toString();
		return filePathStr;
	}
	
	@Test
	public void read_binary_file()
	{
		Path path = openResourceFile("us.gif");
		Path folder = path.getParent();
		BinaryIn bin = new BinaryIn(path.toString());
		
		String data = bin.readString();
		 
		BinaryOut bout = new BinaryOut("us.gif.duplicate");
		
		bout.write(data);
		bout.close();
		
		
		
	}
}
