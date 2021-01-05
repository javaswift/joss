package org.javaswift.joss.instructions;

import static junit.framework.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

public class UploadPayloadInputStreamTest {

    @Test
    public void mustBeSegmented() {
        UploadPayloadInputStream uploadPayload = new UploadPayloadInputStream(null);
        assertFalse(uploadPayload.mustBeSegmented(0L)); // Never segment an inputstream
    }

    @Test (expected = UnsupportedOperationException.class )
    public void noSegmentationPlanForInputStream() throws IOException {
        new UploadPayloadInputStream(null).getSegmentationPlan(0L);
    }

}
