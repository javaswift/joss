package org.javaswift.joss.instructions;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;

import java.io.*;

public class SegmentationPlanFileTest extends SegmentationPlanTest {

    private File file = new File("test.txt");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @After
    public void teardown() {
        file.delete();
    }

    @Test
    public void readItAll() throws IOException {
        OutputStream outputStream = new FileOutputStream(this.file);
        String text = "Some text that needs to be read as several segments which we patch together again and compare to the original";
        IOUtils.write(text, outputStream);
        outputStream.close();
        SegmentationPlan plan = new SegmentationPlanFile(this.file, 10);
        assertTextEquals(plan, text);
        plan.close();
    }
}
