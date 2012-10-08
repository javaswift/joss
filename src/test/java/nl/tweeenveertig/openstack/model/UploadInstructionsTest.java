package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.object.DeleteAfter;
import nl.tweeenveertig.openstack.headers.object.DeleteAt;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

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
        assertEquals("image/png", instructions.getContentType());
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

}
