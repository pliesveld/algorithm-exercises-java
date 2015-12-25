import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.Picture;

public class TestSeam {
	final static String IMAGE_FILENAME = "6x5.png";
	private Picture pic;
	private SeamCarver carver;

	@Before
	public void load_image()
	{	
		File file = new File(IMAGE_FILENAME);
		if(!file.exists()) {
			URL url = this.getClass().getResource(IMAGE_FILENAME);
			file = new File(url.getPath());

			if(!file.exists())
			{
				fail("Could not find test image: " + IMAGE_FILENAME);
			}
		}
		
		pic = new Picture(file);
		carver = new SeamCarver(pic);
	}
	
	@Test
	public void spec_energy_pixel() {
		
		final double [][] expected = {
				{1000.00,1000.00,1000.00,1000.00,1000.00,1000.00},
				{1000.00,237.35,151.02,234.09,107.89,1000.00},
				{1000.00,138.69,228.10,133.07,211.51,1000.00},
				{1000.00,153.88,174.01,284.01,194.50,1000.00},
				{1000.00,1000.00,1000.00,1000.00,1000.00,1000.00}
		};


		for(int row = 0; row < 5;row++)
		{
			for(int col = 0; col < 6; col++)
			{
				String message = String.format("Pixel %d,%d Energy in spec",row,col);
				double pixel_energy =  carver.energy(col, row);
				double expected_energy = expected[row][col];
				assertEquals(message,expected_energy,pixel_energy,0.01);
			}
		}

	}
	
	@Test(timeout=5000)
	public void spec_vertical_seam()
	{
		int[] expected = {
				3,4,3,2,2
		};
		
		int[] array = carver.findVerticalSeam();
		assertEquals("Array size check",expected.length,array.length);
		
		int len = array.length;
		for(int i = 1; i < len-1;i++)
		{
			String message = String.format("Element at pos %d",i);
			int array_idx = array[i];
			int expected_idx = expected[i];
			assertEquals(message,expected_idx,array_idx);
		}
	}

	@Test(timeout=5000)
	public void spec_horizontal_seam()
	{
		int[] expected = {
				2,2,1,2,1,2	
		};
		
		int[] array = carver.findHorizontalSeam();
		assertEquals("Array size check",expected.length,array.length);
		
		int len = array.length;
		for(int i = 1; i < len-1;i++)
		{
			String message = String.format("Element at pos %d",i);
			int array_idx = array[i];
			int expected_idx = expected[i];
			assertEquals(message,expected_idx,array_idx);
		}
	}	
	
	
}
