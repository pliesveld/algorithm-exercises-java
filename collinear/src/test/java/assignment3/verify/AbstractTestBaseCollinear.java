import java.net.URL;
import java.io.File;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import edu.princeton.cs.algs4.In;

public abstract class AbstractTestBaseCollinear
{
    protected abstract String getTestFilename();
    protected abstract LineSegment[] getAnswer();
    protected abstract LineSegment[] getCollinearSegments();

    protected Point[] loadFromFile(String filename)
    {
        URL url = this.getClass().getResource(filename);
        File testFile = new File(url.getFile());
        assertTrue("Couldnt find " + filename,testFile.exists());

        In in = new In(testFile);

        int N = in.readInt();
        Point[] points = new Point[N];
        for(int i =0;i < N;i++)
        {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x,y);
        }

        in.close();

        return points;
    }

    protected void printArray(String message,LineSegment[] array)
    {
        System.out.println();
        System.out.println("LineSegment " + message + " for " + getTestFilename() + " (N=" + array.length + ")");
        for(LineSegment seg : array)
        {
            String line = seg.toString();
            System.out.printf("%s\n",line);
        }
        System.out.println();
    }

    @Test
    public void checkSize()
    {
        LineSegment[] segments = getCollinearSegments();
        assertNotNull("Baseclass was returned a null collinear",segments);
        LineSegment[] answer = getAnswer();
        assertNotNull("Baseclass was returned a null answer linesegment array",answer);
        assertEquals(answer.length,segments.length);
    }

    @Test
    public void checkArray()
    {
        LineSegment[] segments = getCollinearSegments();
        assertNotNull("Baseclass was returned a null collinear",segments);
        LineSegment[] answer = getAnswer();
        assertNotNull("Baseclass was returned a null answer linesegment array",answer);

        Arrays.sort(answer);
        printArray("expected", answer);

        Arrays.sort(segments);

        printArray("returned",segments);

        assertEquals(answer.length,segments.length);
        for(int i = 0;i < segments.length;++i)
        {
            assertEquals(segments[i].compareTo(answer[i]),0);
        }
    }


}
