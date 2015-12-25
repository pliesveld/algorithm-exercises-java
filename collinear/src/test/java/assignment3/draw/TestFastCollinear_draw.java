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

public class TestFastCollinear_draw
{
    static final String[] INPUT_FILES = {
        "input10000.txt",
        "mystery10089.txt",
        "input8000.txt",
        "input6000.txt",
        "input5000.txt",
        "input4000.txt",
        "input3000.txt",
        "input2000.txt",
        "rs1423.txt",
        "input1000.txt",
        "input400.txt",
        "input200.txt",
        "input100.txt",
        "input80.txt",
        "input56.txt",
        "input50.txt",
        "input40.txt",
        "inarow.txt",
        "input20.txt",
        "input10.txt",
        "horizontal100.txt",
        "horizontal75.txt",
        "horizontal50.txt",
        "horizontal25.txt",
        "horizontal5.txt",
        "vertical100.txt",
        "vertical75.txt",
        "vertical50.txt",
        "vertical25.txt",
        "vertical5.txt",
        "input9.txt",
        "input8.txt",
        "input6.txt",
        "input5.txt",
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
            DrawFastSegments.testFile(url);
            System.out.println();
        }
 
    }

}

