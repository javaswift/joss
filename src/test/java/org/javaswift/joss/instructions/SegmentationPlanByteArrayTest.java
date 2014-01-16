package org.javaswift.joss.instructions;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class SegmentationPlanByteArrayTest extends SegmentationPlanTestBase {

    @Test
    public void getSegmentNumber() throws IOException {
        SegmentationPlan plan = new SegmentationPlanByteArray(new byte[] {}, 10);
        assertEquals(Long.valueOf(0L), plan.getSegmentNumber());
    }

    @Test
    public void readItAll() throws IOException {
        String text =
                "Some text that needs to be read as several segments which we patch together again and compare to the original";
        SegmentationPlan plan = new SegmentationPlanByteArray(text.getBytes(), 10);
        assertTextEquals(plan, text);
    }
}
