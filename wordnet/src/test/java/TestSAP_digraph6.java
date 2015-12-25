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
public class TestSAP_digraph6  {

    private static final String DIGRAPH_FILENAME = "digraph6.txt";
    private URL url;
    private Digraph G;
    private SAP sap;



    public void load_graph(In file) {
        G = new Digraph(file);
        sap = new SAP(G);
    }

    @Before
    public void load_unit_test_file() {
        url = TestSAP_digraph6.class.getResource(DIGRAPH_FILENAME);
        In inFile = new In(url.getFile());
        assertTrue(inFile.exists());
        load_graph(inFile);
    }

    @Test(timeout=5000)
    public void gen_test()
    {
        int v = 4;
        int w = 5;
        
        int expected_length = 1;
        int expected_ancestor = 4;
        
        assertEquals("ancestor:",expected_ancestor, sap.ancestor(v, w));
        assertEquals("length:",expected_length, sap.length(v, w));
    }

}
