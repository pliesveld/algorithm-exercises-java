package assignment5;/* vi: set ts=4 sw=4 expandtab: */

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class TestKdTree_draw
{
    static final String[] INPUT_FILES = {
        "spec.txt",
        "circle10.txt",
        "circle10k.txt"
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
            DrawKdTree.testFile(url);
            System.out.println();
        }
 
    }

}

