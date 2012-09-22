package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.model.DownloadInstructions;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.headers.range.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class StoredObjectMockTest {

    private StoredObject object;

    private byte[] uploadBytes = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };

    @Before
    public void setup() {
        object = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "someObject");
    }

    @Test
    public void saveObject() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.uploadObject(bytes);
        assertEquals(bytes.length, object.downloadObject().length);
    }

    @Test
    public void getInfo() throws IOException {
        object = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.uploadObject(bytes);
        assertEquals(3, object.getContentLength());
        assertEquals("5289df737df57326fcdd22597afb1fac", object.getEtag());
        assertEquals("text/plain", object.getContentType());
    }

    @Test
    public void downloadFirstPartRange() throws IOException {
        verifyRangeDownload(uploadBytes, new byte[]{0x01, 0x02, 0x03, 0x04}, new FirstPartRange(4));
    }

    @Test
    public void downloadMidPartRange() throws IOException {
        verifyRangeDownload(uploadBytes, new byte[] { 0x03, 0x04, 0x05, 0x06}, new MidPartRange(2, 6) );
    }

    @Test
    public void downloadLastPartRange() throws IOException {
        verifyRangeDownload(uploadBytes, new byte[] { 0x06, 0x07, 0x08}, new LastPartRange(3) );
    }

    @Test
    public void downloadExcludeStartRange() throws IOException {
        verifyRangeDownload(uploadBytes, new byte[] { 0x04, 0x05, 0x06, 0x07, 0x08}, new ExcludeStartRange(3) );
    }

    protected void verifyRangeDownload(byte[] uploadBytes, byte[] expectedBytes, AbstractRange range) throws IOException {
        object = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        object.uploadObject(uploadBytes);
        byte[] part = object.downloadObject(new DownloadInstructions().setRange(range));
        assertEquals(expectedBytes.length, part.length);
        assertTrue(Arrays.equals(expectedBytes, part));
    }

}
