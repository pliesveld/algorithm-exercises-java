import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

import org.junit.Test;

public class SpecMoveToFrontTest {

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
	
	
	void openResourceRedirectedToStdIn(String filename)
	{
		Path filePath = openResourceFile(filename);
		String filePathStr = filePath.toAbsolutePath().toString();

		try {
			System.setIn(new FileInputStream(filePathStr));
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void test_redirect()
	{
		openResourceRedirectedToStdIn("abra.txt");
		MoveToFront.main(new String[]{"-"});
	}
	
}
