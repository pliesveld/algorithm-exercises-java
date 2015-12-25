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
public class TestSAP_digraph3  {

    private static final String DIGRAPH_FILENAME = "digraph3.txt";
    private URL url;
    private Digraph G;
    private SAP sap;



    public void load_graph(In file) {
        G = new Digraph(file);
        sap = new SAP(G);
    }

    @Before
    public void load_unit_test_file() {
        url = TestSAP_digraph3.class.getResource(DIGRAPH_FILENAME);
        In inFile = new In(url.getFile());
        assertTrue(inFile.exists());
        load_graph(inFile);
    }

    @Test(timeout=5000)
    public void test_loop_length()
    {
        int v = 0;
        int w = 1;

        int expected_length = -1;
        
        assertEquals("length:",expected_length, sap.length(v, w));
    }
    
    @Test(timeout=5000)
    public void test_loop_ancestor()
    {
        int v = 0;
        int w = 1;

        int expected_ancestor = -1;
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }
    
    @Test(timeout=5000)
    public void test_loop_length_reverse()
    {
        int v = 1;
        int w = 0;

        int expected_length = -1;
        
        assertEquals("length:",expected_length, sap.length(v, w));
    }
    
    @Test(timeout=5000)
    public void test_loop_ancestor_reverse()
    {
        int v = 1;
        int w = 0;

        int expected_ancestor = -1;
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }

    
    @Test(timeout=5000)
    public void gen_test_ancestor()
    {
        int v = 10;
        int w = 11;
        
        int expected_length = 1;
        assertEquals("length:",expected_length, sap.length(v, w));
    }
    
    @Test(timeout=5000)
    public void gen_test_ancestor_reverse()
    {
        int v = 11;
        int w = 10;
        
        int expected_length = 1;
        assertEquals("length:",expected_length, sap.length(v, w));
    }

    @Test(timeout=5000)
    public void gen_test2()
    {
        int v = 14;
        int w = 8;
        
        int expected_length = 4;
        int expected_ancestor = 8;
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
        assertEquals("length:",expected_length, sap.length(v, w));
    }

    @Test(timeout=5000)
    public void gen_test2_reverse()
    {
        int v = 8;
        int w = 14;
        
        int expected_length = 4;
        int expected_ancestor = 8;
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
        assertEquals("length:",expected_length, sap.length(v, w));
    }

    

}
