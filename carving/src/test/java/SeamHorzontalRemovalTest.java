import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.Picture;

public class SeamHorzontalRemovalTest {
	
	private static final int WIDTH = 6;
	private static final int HEIGHT = 5;

	private Picture picture;
	
	@Before
	public void initialize_blank_image()
	{
		picture = new Picture(WIDTH, HEIGHT);
		
		for(int row = 0; row < HEIGHT; row++)
			for(int col = 0; col < WIDTH; col++)
			{
				Color color = Color.WHITE;
				picture.set(col, row, color);
			}
	}
	
	private void verifyPixels()
	{
		int height = picture.height();
		int width = picture.width();
		
		for(int row = 0; row < height; row++)
			for(int col = 0; col < width; col++)
			{
				String message = String.format("Pixel at [row,col] [%d,%d] should be WHITE",row,col);
				Color color = picture.get(col, row);
				assertEquals(message,Color.WHITE,color);
			}
	}
	
	@Test
	public void sanity_test()
	{
		assertNotNull(picture);
		assertEquals("Picture WIDTH",WIDTH,picture.width());
		assertEquals("Picture HEIGHT",HEIGHT,picture.height());
		
		verifyPixels();
	}

	private void paint_seam(int[] seam)
	{
		int idx = 0;
		assertEquals("Invalid horizontal seam length",WIDTH,seam.length);
		for(int col = 0; col < WIDTH;col++)
		{
			picture.set(col,seam[idx++],Color.MAGENTA);
		}
	}
	
	private void verifyNewPictureDimensions()
	{
		assertEquals("Image height decreased",HEIGHT-1,picture.height());
		assertEquals("Image width unchanged",WIDTH,picture.width());
	}
	
	@Test
	public void top_seam_removal()
	{
		int[] seam = new int[WIDTH];
		for(int i = 0; i < seam.length;i++)
			seam[i] = 0;
		
		paint_seam(seam);
		SeamCarver sc = new SeamCarver(picture);
		sc.removeHorizontalSeam(seam);
		picture = sc.picture();
		verifyNewPictureDimensions();
		verifyPixels();
	}

	@Test
	public void bottom_seam_removal()
	{
		int[] seam = new int[WIDTH];
		for(int i = 0; i < seam.length;i++)
			seam[i] = HEIGHT - 1;
		
		paint_seam(seam);
		SeamCarver sc = new SeamCarver(picture);
		sc.removeHorizontalSeam(seam);
		picture = sc.picture();
		verifyNewPictureDimensions();
		verifyPixels();
	}
}
