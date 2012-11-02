package nl.tweeenveertig.openstack.model;

import org.junit.Test;

import java.io.IOException;

public class SegmentationPlanByteArrayTest extends SegmentationPlanTest {

    @Test
    public void readItAll() throws IOException {
        String text =
                "Some text that needs to be read as several segments which we patch together again and compare to the original";
        SegmentationPlan plan = new SegmentationPlanByteArray(text.getBytes(), 10);
        assertTextEquals(plan, text);
    }
}
