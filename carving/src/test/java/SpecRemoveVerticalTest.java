

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SpecRemoveVerticalTest {


	Picture picture;
	SeamCarver sc;
	Out out;
	
	@Before
	public void load_image()
	{
		String filename = getFilename();
		File file = new File(filename);
		
		if(!file.exists())
		{
			file = new File(this.getClass().getResource(filename).getPath());
			
		}
		assertTrue("test image exists: ",file.exists());
		this.picture = new Picture(file);
        sc = new SeamCarver(picture);
	}
	
	public String getFilename() {
		return "6x5.png";
	}
	
    private static final boolean HORIZONTAL   = true;
    private static final boolean VERTICAL     = false;

    double printSeam(SeamCarver carver, int[] seam, boolean direction) {
        double totalSeamEnergy = 0.0;

        for (int row = 0; row < carver.height(); row++) {
            for (int col = 0; col < carver.width(); col++) {
                double energy = carver.energy(col, row);
                String marker = " ";
                if ((direction == HORIZONTAL && row == seam[col]) ||
                    (direction == VERTICAL   && col == seam[row])) {
                    marker = "*";
                    totalSeamEnergy += energy;
                }
                StdOut.printf("%7.2f%s ", energy, marker);
            }
            StdOut.println("");
        }                
        // StdOut.println();
        StdOut.printf("Total energy = %f\n", totalSeamEnergy);
        StdOut.println("");
        StdOut.println("");
        //verifySeam(seam,direction);
        return totalSeamEnergy;
    }
	
	
	@Test
	public void test_seam_remove() {
		StdOut.printf("Vertical seam:\n");
		int[] seam = sc.findVerticalSeam();
		printSeam(sc,seam,false);
		sc.removeVerticalSeam(seam);
		printSeam(sc,seam,false);
	}

}
