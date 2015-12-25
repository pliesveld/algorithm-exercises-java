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
import edu.princeton.cs.algs4.Out;

public class TestBruteCollinear_timed extends AbstractTestBase
{
    static final String[] INPUT_FILES = {
 /*
        "input400.txt",
        "input200.txt",
        "input100.txt", 
        "horizontal100.txt", 
        "vertical100.txt",*/
        "horizontal75.txt",
        "vertical75.txt",
        "horizontal50.txt",
        "vertical50.txt",
        "horizontal25.txt",
        "vertical25.txt",
        "input40.txt",
        "input8.txt",
        "input6.txt"
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
    public void timeFiles()
    {
        URL out_url = this.getClass().getResource("/");
        File out_file = new File(out_url.getPath() + "timed_brute.txt");
        Out out = new Out(out_file.getPath());
        for( String filename : INPUT_FILES)
        {
            URL url = this.getClass().getResource(filename);
            Point[] points = loadPointsFromFile(new File(url.getFile()));
            double duration = TimedBruteSegments.timedTest(points);
            out.printf("%-16s : %.3f\n",filename,duration);
        }
    }

}

