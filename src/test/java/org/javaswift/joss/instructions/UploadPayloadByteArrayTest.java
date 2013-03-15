package org.javaswift.joss.instructions;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class UploadPayloadByteArrayTest {

    @Test
    public void mustBeSegmented() {
        checkForRequiredSegmentation(new byte[] { 0x01, 0x02, 0x03, 0x04 }, 3L, true);
    }

    @Test
    public void mustNotBeSegmented() {
        checkForRequiredSegmentation(new byte[] { 0x01, 0x02 }, 3L, false);
    }

    @Test
    public void getSegmentationPlan() throws IOException {
        UploadPayload payload = new UploadPayloadByteArray(new byte[] {} );
        assertNotNull(payload.getSegmentationPlan(10L));
    }

    protected void checkForRequiredSegmentation(byte[] bytes, Long segmentationSize, boolean expectSegmentation) {
        UploadPayload payload = new UploadPayloadByteArray(bytes);
        assertEquals(expectSegmentation, payload.mustBeSegmented(segmentationSize));
    }
}
