import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by happs on 12/5/15.
 */
public class TestSAP_digraph2  {

    private static final String DIGRAPH_FILENAME = "digraph2.txt";
    private URL url;
    private Digraph G;
    private SAP sap;



    public void load_graph(In file) {
        G = new Digraph(file);
        sap = new SAP(G);
    }

    @Before
    public void load_unit_test_file() {
        url = TestSAP_digraph2.class.getResource(DIGRAPH_FILENAME);
        In inFile = new In(url.getFile());
        assertTrue(inFile.exists());
        load_graph(inFile);
    }

    @Test
    public void spec_test1_length()
    {
        int v = 1;
        int w = 5;

        int expected_length = 2;
        
        assertEquals("length:",expected_length, sap.length(v, w));
    }
    
    @Test
    public void spec_test1_ancestor()
    {
        int v = 1;
        int w = 5;

        int expected_ancestor = 0;

        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }


    @Test
    public void spec_test1_length_reverse()
    {
        int v = 5;
        int w = 1;

        int expected_length = 2;
        
        assertEquals("length:",expected_length, sap.length(v, w));
    }
    
    @Test
    public void spec_test1_ancestor_reverse()
    {
        int v = 5;
        int w = 1;

        int expected_ancestor = 0;

        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }

    
    @Test
    public void gen_test1()
    {
        int v = 4;
        int w = 0;

        int expected_ancestor = 0;
        int expected_length = 2;
        
        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }
    
    @Test
    public void gen_test1_reverse()
    {
        int v = 0;
        int w = 4;

        int expected_ancestor = 0;
        int expected_length = 2;
        
        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }


    @Test
    public void gen_test2()
    {
        int v = 2;
        int w = 5;

        int expected_length = 3;
        
        assertEquals("length:",expected_length, sap.length(v, w));
    }

    @Test
    public void gen_test2_reverse()
    {
        int v = 5;
        int w = 2;

        int expected_length = 3;
        
        assertEquals("length:",expected_length, sap.length(v, w));
    }


}
