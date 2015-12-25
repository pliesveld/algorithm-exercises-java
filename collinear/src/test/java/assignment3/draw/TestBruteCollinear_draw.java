/* vi: set ts=4 sw=4 expandtab: */
import java.net.URL;
import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.In;

public class TestBruteCollinear_draw
{
    static final String[] INPUT_FILES = {
/*      "rs1423.txt",
        "input400.txt",
        "input200.txt",
        "input100.txt", */
/*        "horizontal100.txt", 
        "vertical100.txt",
        "horizontal75.txt",
        "vertical75.txt",
        "horizontal50.txt",
        "vertical50.txt", */
        "horizontal25.txt",
        "horizontal5.txt",
        "vertical25.txt",
        "vertical5.txt",
        "input56.txt", // contains 
        "input50.txt",
        "input40.txt",
        "input8.txt",
        "input6.txt",
        "input5_len4.txt",
        "input4.txt"
    };

    @Before
    public void verifyFiles()
    {
        for( String infile : INPUT_FILES)
        {
            URL url = this.getClass().getResource(infile);
            File testFile = new File(url.getFile());
            assertTrue("Couldnt find " + infile,testFile.exists());
        }

    }

    @Test
    public void drawFiles()
    {
        for( String filename : INPUT_FILES)
        {
            URL url = this.getClass().getResource(filename);
            System.out.println("Drawing " + url.getFile());
            DrawBruteSegments.testFile(url);
            System.out.println();
        }
 
    }

}

