package nl.tweeenveertig.openstack.model;

import org.junit.Test;
import static junit.framework.Assert.assertFalse;

public class UploadPayloadInputStreamTest {

    @Test
    public void mustBeSegmented() {
        UploadPayloadInputStream uploadPayload = new UploadPayloadInputStream(null);
        assertFalse(uploadPayload.mustBeSegmented(0L)); // Never segment an inputstream
    }

}
