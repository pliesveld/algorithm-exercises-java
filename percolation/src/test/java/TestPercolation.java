import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.In;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


public class TestPercolation
{

    private Path loadTestResource(String filename)
    {
        URL url = this.getClass().getClassLoader().getResource(filename);
        assertNotNull(url);
        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            fail(e.getMessage());
        }
        return null;
    }

	@Test
	public void percolateDefaultFalse() {
		Percolation p = new Percolation(2);
		assertEquals("should not percolate",false,p.percolates());

	}

	@Test
	public void percolateOpenNoPerc() {
		Percolation p = new Percolation(2);
		p.open(1,1);
		assertEquals("should not percolate",false,p.percolates());

	}

	@Test
	public void percolateOpenNoPerc2() {
		Percolation p = new Percolation(2);
		p.open(1,2);
		assertEquals("should not percolate",false,p.percolates());

	}

	@Test
	public void percolateOpenNoPerc3() {
		Percolation p = new Percolation(2);
		p.open(1,1);
		p.open(2,2);
		assertEquals("should not percolate",false,p.percolates());

	}

	@Test
	public void percolateOpenPerc1() {
		Percolation p = new Percolation(2);
		p.open(1,1);
		p.open(1,2);
		assertEquals("should not percolate",false,p.percolates());
		p.open(2,2);
		assertEquals("should not percolate",true,p.percolates());

	}

	@Test
	public void percolateOpenPerc2() {
		Percolation p = new Percolation(2);
		p.open(1,1);
		p.open(1,2);
		assertEquals("should not percolate",false,p.percolates());
		p.open(2,1);
		assertEquals("should not percolate",true,p.percolates());
	}

	@Test
	public void percolateStats() {
		PercolationStats p = new PercolationStats(200,100);

		assertEquals("mean should be ~0.592",true,(p.mean() - 0.592 )< 0.01);

	}

	@Test
	public void percolateTestFile() {
		String filename = "input4.txt";

		In infile = new In(filename);
		assertEquals("could not open file.",true,infile.exists());
		int N = infile.readInt();

		Percolation p = new Percolation(N);
		while(!infile.isEmpty())
		{
			int i = infile.readInt();
			int j = infile.readInt();
			p.open(i,j);
		}

		assertEquals("should percolate",true,p.percolates());

	}

	@Test
	public void percolateTestBackwash() {
		String filename = loadTestResource("input10.txt").toAbsolutePath().toString();

		In infile = new In(filename);
		assertEquals("could not open file.",true,infile.exists());
		int N = infile.readInt();

		Percolation p = new Percolation(N);
		while(!infile.isEmpty())
		{
			int i = infile.readInt();
			int j = infile.readInt();
			p.open(i,j);
		}

		assertEquals("should percolate",true,p.percolates());
		assertEquals("should be open",true,p.isFull(10,8));
		assertEquals("shouldn't be fullopen",false,p.isFull(10,8));
		

	}




}

