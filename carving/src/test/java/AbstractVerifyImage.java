import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public abstract class AbstractVerifyImage extends AbstractTestImage {

	abstract public double getExpectedEnergy(boolean horizontal); 
	abstract public int[] getExpectedSeam(boolean horizontal);
	
	
	@Override
	public void print_seam_vertical()
	{
		print("Vertical seam: ");
		int[] seam = sc.findVerticalSeam();
		print_array(seam);
		double totalEnergy = printSeam(sc,seam,false);
		String message = String.format("Vertical Seam of %s",getFilename());
		assertEquals(message,getExpectedEnergy(false),totalEnergy,0.0001);
	}
	
	
	@Override
	public void print_seam_horizontal()
	{
		print("Horizontal seam: ");
		int[] seam = sc.findHorizontalSeam();
		print_array(seam);
		double totalEnergy = printSeam(sc,seam,true);
		
		String message = String.format("Horizontal Seam of %s",getFilename());
		assertEquals(message,getExpectedEnergy(true),totalEnergy,0.0001);
	}
	
	@Override
	protected void verifySeam(int[] seam, boolean direction) {
		int[] expected_seam = getExpectedSeam(direction);
		
		String message = String.format("Verifying seam %s: ", direction ? "horizontal" : "vertical");
		
		assertEquals(message + " length ",expected_seam.length,seam.length);
		assertArrayEquals(message + " values ",expected_seam,seam);
	}
}