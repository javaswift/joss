package org.javaswift.joss.instructions;

import mockit.*;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class UploadPayloadFileTest {

    private File file = new File("test.tmp");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @After
    public void cleanup() {
        file.delete();
    }

    @Test
    public void mustBeSegmented(@Injectable final File fileToUpload) {
        checkForRequiredSegmentation(fileToUpload, 12L, 9L, true);
    }

    @Test
    public void mustNotBeSegmented(@Injectable final File fileToUpload) {
        checkForRequiredSegmentation(fileToUpload, 9L, 12L, false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void getSegmentationPlan() throws IOException {
        file.createNewFile();
        UploadPayload payload = new UploadPayloadFile(file);
        assertNotNull(payload.getSegmentationPlan(10L));
    }

    protected UploadPayload createUploadPayload(final File fileToUpload, final Long fileSize) {
        new NonStrictExpectations(fileToUpload) {{
            fileToUpload.length(); result = fileSize;
        }};
        return new UploadPayloadFile(fileToUpload);
    }

    protected void checkForRequiredSegmentation(File fileToUpload, Long fileSize, Long segmentationSize, boolean expectSegmentation) {
        UploadPayload payload = createUploadPayload(fileToUpload, fileSize);
        assertEquals(expectSegmentation, payload.mustBeSegmented(segmentationSize));
    }
}
