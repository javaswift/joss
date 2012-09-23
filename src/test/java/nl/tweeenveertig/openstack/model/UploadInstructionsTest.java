package nl.tweeenveertig.openstack.model;

import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;

import static junit.framework.Assert.assertEquals;
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
}
