package org.javaswift.joss.instructions;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UploadPayloadFileTest {

    private File file = new File("test.tmp");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @After
    public void cleanup() {
        file.delete();
    }

    @Test
    public void mustBeSegmented() {
        checkForRequiredSegmentation(12L, 9L, true);
    }

    @Test
    public void mustNotBeSegmented() {
        checkForRequiredSegmentation(9L, 12L, false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void getSegmentationPlan() throws IOException {
        file.createNewFile();
        UploadPayload payload = new UploadPayloadFile(file);
        assertNotNull(payload.getSegmentationPlan(10L));
    }

    protected UploadPayload createUploadPayload(Long fileSize) {
        File fileToUpload = mock(File.class);
        when(fileToUpload.length()).thenReturn(fileSize);
        return new UploadPayloadFile(fileToUpload);
    }

    protected void checkForRequiredSegmentation(Long fileSize, Long segmentationSize, boolean expectSegmentation) {
        UploadPayload payload = createUploadPayload(fileSize);
        assertEquals(expectSegmentation, payload.mustBeSegmented(segmentationSize));
    }
}
