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

public class TestFastCollinear_timed extends AbstractTestBase
{
    static final String[] INPUT_FILES = {
//        "input10000.txt",
        "input8000.txt",
        "input6000.txt",
        "input5000.txt",
        "input4000.txt",
        "input3000.txt",
        "input2000.txt",
        "input1000.txt"
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
        File out_file = new File(out_url.getPath() + "timed_fast.txt");
        Out out = new Out(out_file.getPath());
        for( String filename : INPUT_FILES)
        {
            URL url = this.getClass().getResource(filename);
            Point[] points = loadPointsFromFile(new File(url.getFile()));
            double duration = TimedFastSegments.timedTest(points);
            out.printf("%-16s : %.3f\n",filename,duration);
        }
    }

}

