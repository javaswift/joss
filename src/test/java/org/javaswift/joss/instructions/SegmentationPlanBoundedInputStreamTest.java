package org.javaswift.joss.instructions;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class SegmentationPlanBoundedInputStreamTest extends SegmentationPlanTestBase {

    @Test
    public void getSegmentNumber() throws IOException {
        String text = "Some text that needs to be read as several segments which we patch together again and compare to the original";
        InputStream inputStream = IOUtils.toInputStream(text);
        SegmentationPlan plan = new SegmentationPlanBoundedInputStream(inputStream, text.length(), 10L);
        assertEquals(Long.valueOf(0L), plan.getSegmentNumber());
    }

    @Test
    public void readItAll() throws IOException {
        String text = "Some text that needs to be read as several segments which we patch together again and compare to the original";
        InputStream inputStream = IOUtils.toInputStream(text);
        SegmentationPlan plan = new SegmentationPlanBoundedInputStream(inputStream, text.length(), 10L);
        assertTextEquals(plan, text);
        plan.close();
    }
}
