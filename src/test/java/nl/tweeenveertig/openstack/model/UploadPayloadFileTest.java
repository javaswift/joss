package nl.tweeenveertig.openstack.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UploadPayloadFileTest {

    @Test
    public void mustBeSegmented() {
        checkForRequiredSegmentation(12L, 9L, true);
    }

    @Test
    public void mustNotBeSegmented() {
        checkForRequiredSegmentation(9L, 12L, false);
    }

    protected void checkForRequiredSegmentation(Long fileSize, Long segmentationSize, boolean expectSegmentation) {
        File fileToUpload = mock(File.class);
        when(fileToUpload.length()).thenReturn(fileSize);
        UploadPayload payload = new UploadPayloadFile(fileToUpload);
        assertEquals(expectSegmentation, payload.mustBeSegmented(segmentationSize));
    }
}
