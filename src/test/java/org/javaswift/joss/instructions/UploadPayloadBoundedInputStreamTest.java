package org.javaswift.joss.instructions;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import mockit.Injectable;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class UploadPayloadBoundedInputStreamTest extends SegmentationPlanTestBase {

    @Test
    public void mustBeSegmented(@Injectable final InputStream inputStream) {
        checkForRequiredSegmentation(inputStream, 12L, 9L, true);
    }

    @Test
    public void mustNotBeSegmented(@Injectable final InputStream inputStream) {
        checkForRequiredSegmentation(inputStream, 9L, 12L, false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void getSegmentationPlan() throws IOException {
        String text = "Some text that needs to be read as several segments which we patch together again and compare to the original";
        InputStream inputStream = IOUtils.toInputStream(text);
        UploadPayload payload = new UploadPayloadBoundedInputStream(inputStream, text.length());
        assertNotNull(payload.getSegmentationPlan(10L));
    }

    protected UploadPayload createUploadPayload(final InputStream inputStream, final Long length) {
        return new UploadPayloadBoundedInputStream(inputStream, length);
    }

    protected void checkForRequiredSegmentation(InputStream inputStream, Long length, Long segmentationSize, boolean expectSegmentation) {
        UploadPayload payload = createUploadPayload(inputStream, length);
        assertEquals(expectSegmentation, payload.mustBeSegmented(segmentationSize));
    }
}
