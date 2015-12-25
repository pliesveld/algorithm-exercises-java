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

public abstract class AbstractTestImage {

	abstract public String getFilename();

	
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
	
	
	protected void printf(String format, Object... args)
	{
		if(out != null)
		{
			out.printf(format,args);
		} else {
			StdOut.printf(format,args);
		}
		
	}


	protected <T> void println(T line)
	{
		if(out != null)
		{
			out.println(line);
		} else {
			StdOut.println(line);
		}
		
	}

	protected <T> void print(T line)
	{		
		if(out != null)
		{
			out.print(line);
		} else {
			StdOut.print(line);
		}
	}

	
	@Before
	public void check_generated_folder()
	{
		URL url = this.getClass().getResource("generated");
		if(url != null)
		{
			File testFolder = new File(url.getPath());
			assertTrue(testFolder.exists());
			assertTrue(testFolder.isDirectory());
				
			String out_filename = getFilename();
			out_filename = out_filename.replaceFirst("png", "printseams.txt");
			String out_path = testFolder.getAbsolutePath() + File.separatorChar + out_filename;
			StdOut.println("Writing to: " + out_filename);
			out = new Out(out_path);
		}
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
                printf("%7.2f%s ", energy, marker);
            }
            println("");
        }                
        // StdOut.println();
        printf("Total energy = %f\n", totalSeamEnergy);
        println("");
        println("");
        //verifySeam(seam,direction);
        return totalSeamEnergy;
    }
	
	
	protected void verifySeam(int[] seam, boolean direction) {
		// Stub for overloading class to verify seam
	}


	protected void print_array(int[] array)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("{ ");
		for(int x : array)
		{
			sb.append(x);
			sb.append(" ");
		}
		sb.append("}");
		
		println(sb.toString());
	}
	
    public void print_energy() {
        
        printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        printf("Printing energy calculated for each pixel.\n");        

        for (int j = 0; j < sc.height(); j++) {
            for (int i = 0; i < sc.width(); i++)
                printf("%9.0f ", sc.energy(i, j));
            println("");
        }
        println("");
    }
    
    

	
	public void print_header()
	{
	    printf("%s (%d-by-%d image)\n\n", getFilename(),picture.width(), picture.height());
	    println("The table gives the dual-gradient energies of each pixel.\nThe asterisks denote a minimum energy vertical or horizontal seam.\n");
	}
    
	public void print_seam_vertical()
	{
		print("Vertical seam: ");
		int[] seam = sc.findVerticalSeam();
		print_array(seam);
		printSeam(sc,seam,false);
	}
    
	
	public void print_seam_horizontal()
	{
		print("Horizontal seam: ");
		int[] seam = sc.findHorizontalSeam();
		print_array(seam);
		printSeam(sc,seam,true);
	}

	@Test(timeout=15000)
	public void examine_file()
	{
		print_header();
		print_seam_vertical();
		print_seam_horizontal();
	}
}