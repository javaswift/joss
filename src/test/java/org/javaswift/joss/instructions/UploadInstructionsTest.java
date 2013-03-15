package org.javaswift.joss.instructions;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.object.DeleteAfter;
import org.javaswift.joss.headers.object.DeleteAt;
import org.javaswift.joss.headers.object.ObjectManifest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { UploadInstructions.class, UploadPayloadFile.class } )
public class UploadInstructionsTest {

    @Test
    public void fileEntity() {
        UploadInstructions instructions = new UploadInstructions(new File("/tmp"));
        assertTrue(instructions.getEntity() instanceof FileEntity);
    }

    @Test
    public void byteArrayEntity() {
        UploadInstructions instructions = new UploadInstructions(new byte[] { 0x01, 0x02} );
        assertTrue(instructions.getEntity() instanceof ByteArrayEntity);
    }

    @Test
    public void inputStreamEntity() {
        UploadInstructions instructions = new UploadInstructions(new ByteArrayInputStream(new byte[] { 0x01 }));
        assertTrue(instructions.getEntity() instanceof InputStreamEntity);
    }

    @Test
    public void md5() {
        UploadInstructions instructions = new UploadInstructions(new File("/tmp")).setMd5("cafebabe");
        assertEquals("cafebabe", instructions.getMd5());
    }

    @Test
    public void contentType() {
        UploadInstructions instructions = new UploadInstructions(new File("/tmp")).setContentType("image/png");
        assertEquals("image/png", instructions.getContentType().getHeaderValue());
    }

    @Test
    public void deleteAt() {
        UploadInstructions instructions = new UploadInstructions(new File("/tmp")).setDeleteAt(new DeleteAt(new Date()));
        assertNotNull(instructions.getDeleteAt());
    }

    @Test
    public void deleteAfter() {
        UploadInstructions instructions = new UploadInstructions(new File("/tmp")).setDeleteAfter(new DeleteAfter(42));
        assertNotNull(instructions.getDeleteAfter());
    }

    @Test
    public void objectManifest() {
        UploadInstructions instructions = new UploadInstructions(new File("/tmp")).setObjectManifest(new ObjectManifest("images/big.dat"));
        assertNotNull(instructions.getObjectManifest());
    }

    @Test
    public void segmentationSize() {
        UploadInstructions instructions = new UploadInstructions(new File("/tmp")).setSegmentationSize(4000000000L);
        assertEquals((Long)4000000000L, instructions.getSegmentationSize());
    }

    @Test
    public void requiresSegmentation() {
        File fileToUpload = mock(File.class);
        when(fileToUpload.length()).thenReturn(12L);
        UploadInstructions instructions = new UploadInstructions(fileToUpload).setSegmentationSize(9L);
        assertTrue(instructions.requiresSegmentation());
    }

    @Test (expected = CommandException.class )
    public void getSegmentationPlanThrowsIOException() throws Exception {
        UploadPayloadFile uploadPayload = mock(UploadPayloadFile.class);
        whenNew(UploadPayloadFile.class).withArguments(null).thenReturn(uploadPayload);
        when(uploadPayload.getSegmentationPlan(anyLong())).thenThrow(new IOException());
        UploadInstructions instructions = new UploadInstructions((File)null);
        instructions.getSegmentationPlan();
    }

}
