package nl.tweeenveertig.openstack.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UploadPayloadByteArrayTest {

    @Test
    public void mustBeSegmented() {
        checkForRequiredSegmentation(new byte[] { 0x01, 0x02, 0x03, 0x04 }, 3L, true);
    }

    @Test
    public void mustNotBeSegmented() {
        checkForRequiredSegmentation(new byte[] { 0x01, 0x02 }, 3L, false);
    }

    protected void checkForRequiredSegmentation(byte[] bytes, Long segmentationSize, boolean expectSegmentation) {
        UploadPayload payload = new UploadPayloadByteArray(bytes);
        assertEquals(expectSegmentation, payload.mustBeSegmented(segmentationSize));
    }
}
