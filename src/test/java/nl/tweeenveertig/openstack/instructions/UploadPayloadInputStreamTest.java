package nl.tweeenveertig.openstack.instructions;

import nl.tweeenveertig.openstack.instructions.UploadPayloadInputStream;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertFalse;

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
