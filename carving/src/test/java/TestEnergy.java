import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.Picture;

public class TestEnergy {
	final static String IMAGE_FILENAME = "3x4.png";
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
	public void spec_energy_pixel_1_2() {
		int x = 1;
		int y = 2;
		
		double pixel_energy =  carver.energy(x, y);
		double expected_energy = Math.sqrt(52024);
		assertEquals("Pixel Energy in spec",expected_energy,pixel_energy,0.00001);
	}

	@Test
	public void spec_energy_pixel_1_1() {
		int x = 1;
		int y = 1;
		
		double pixel_energy =  carver.energy(x, y);
		double expected_energy = Math.sqrt(52225);
		assertEquals("Pixel Energy in spec",expected_energy,pixel_energy,0.00001);
	}

	
}
