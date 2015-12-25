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
public class TestSAP_digraph1  {

    private static final String DIGRAPH_FILENAME = "digraph1.txt";
    private URL url;
    private Digraph G;
    private SAP sap;



    public void load_graph(In file) {
        G = new Digraph(file);
        sap = new SAP(G);
    }


    @Before
	public void load_unit_test_file() {
        url = TestSAP_digraph1.class.getResource(DIGRAPH_FILENAME);
        In inFile = new In(url.getFile());
        assertTrue(inFile.exists());
        load_graph(inFile);
    }

    @Test
    public void spec_test1()
    {
        int v = 3;
        int w = 11;

        int expected_length = 4;
        int expected_ancestor = 1;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }

    @Test
    public void spec_test1_reverse()
    {
        int v = 11;
        int w = 3;

        int expected_length = 4;
        int expected_ancestor = 1;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }
    
    @Test
    public void spec_test2()
    {
        int v = 9;
        int w = 12;

        int expected_length = 3;
        int expected_ancestor = 5;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }

    @Test
    public void spec_test2_reverse()
    {
        int v = 12;
        int w = 9;

        int expected_length = 3;
        int expected_ancestor = 5;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }
    
    @Test
    public void spec_test3()
    {
        int v = 7;
        int w = 2;

        int expected_length = 4;
        int expected_ancestor = 0;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }
    
    @Test
    public void spec_test3_reverse()
    {
        int v = 2;
        int w = 7;

        int expected_length = 4;
        int expected_ancestor = 0;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }

    @Test
    public void spec_test4()
    {
        int v = 1;
        int w = 6;

        int expected_length = -1;
        int expected_ancestor = -1;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }
    
    @Test
    public void spec_test4_reverse()
    {
        int v = 6;
        int w = 1;

        int expected_length = -1;
        int expected_ancestor = -1;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }
    

    @Test
    public void gen_test1()
    {
        int v = 3;
        int w = 8;

        int expected_length = 1;

        assertEquals("length:",expected_length, sap.length(v, w));
    }
    
    @Test
    public void gen_test1_reverse()
    {
        int v = 8;
        int w = 3;

        int expected_length = 1;

        assertEquals("length:",expected_length, sap.length(v, w));
    }

    
    
    @Test
    public void gen_test2()
    {
        int v = 11;
        int w = 0;

        int expected_length = 4;
        int expected_ancestor = 0;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }


    @Test
    public void gen_test2_reverse()
    {
        int v = 0;
        int w = 11;

        int expected_length = 4;
        int expected_ancestor = 0;

        assertEquals("length:",expected_length, sap.length(v, w));
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
    }
    
}
