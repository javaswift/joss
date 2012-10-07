package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.object.conditional.IfModifiedSince;
import nl.tweeenveertig.openstack.headers.object.conditional.IfNoneMatch;
import nl.tweeenveertig.openstack.model.DownloadInstructions;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.headers.object.range.*;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.*;

public class StoredObjectMockTest {

    private StoredObjectMock object;

    private byte[] uploadBytes = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };

    private File downloadedFile = new File("download.tmp");

    @Before
    public void setup() {
        object = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "someObject");
    }

    @After
    public void cleanup() {
        downloadedFile.delete();
    }

    @Test
    public void eliminateFluff() { // Pick out the items which are not useful in mock mode
        assertEquals("", object.getPublicURL());
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
    public void setContentType() {
        object = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        object.setContentType("text/plain");
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
        verifyRangeDownload(uploadBytes, new byte[]{0x04, 0x05, 0x06, 0x07, 0x08}, new ExcludeStartRange(3));
    }

    @Test
    public void downloadWithIfNoneMatch() throws IOException {
        object = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.uploadObject(bytes);
        try {
            object.downloadObject(new DownloadInstructions().setMatchConditional(new IfNoneMatch("5289df737df57326fcdd22597afb1fac")));
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTENT_NOT_MODIFIED, err.getError());
        }
    }

    @Test
    public void addMetadata() {
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("name", "value");
        object.setMetadata(metadata);
        assertEquals(1, object.getMetadata().size());
    }

    @Test
    public void exists() throws IOException {
        assertFalse(object.exists());
        object.uploadObject(uploadBytes);
        assertTrue(object.exists());
        StoredObject mockObject = new StoredObjectMock(object.getContainer(), "some-object") {
            @Override
            public void getInfo() {
                throw new CommandException(404, CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
            }
        };
        assertFalse(mockObject.exists());
    }

    @Test
    public void copyObjectToNonExistingContainer() throws IOException {
        object.uploadObject(uploadBytes);
        Container copyContainer = object.getContainer().getAccount().getContainer("copy-container");
        StoredObject copyObject = copyContainer.getObject("copy-object");
        object.copyObject(object.getContainer().getAccount().getContainer("copy-container"), copyObject);
        assertTrue(copyObject.exists());
    }

    @Test
    public void downloadAsInputStream() throws IOException {
        object.uploadObject(uploadBytes);
        assertTrue(Arrays.equals(uploadBytes, IOUtils.toByteArray(object.downloadObjectAsInputStream())));
        assertTrue(Arrays.equals(uploadBytes, IOUtils.toByteArray(object.downloadObjectAsInputStream(new DownloadInstructions()))));
    }

    @Test
    public void checkForLastModificationTime() {
        InputStream bytes = new ByteArrayInputStream(uploadBytes);
        assertNull(object.getLastModified());
        assertNull(object.getLastModifiedAsDate());
        object.uploadObject(bytes);
        assertNotNull(object.getLastModified());
        assertNotNull(object.getLastModifiedAsDate());
    }

    @Test
    public void downloadIfModifiedSinceOnChangedContent() {
        InputStream bytes = new ByteArrayInputStream(uploadBytes);
        object.uploadObject(bytes);
        Date sinceDate = new Date((new Date()).getTime()-86400); // one day before now
        assertNotNull(object.downloadObject(new DownloadInstructions().setSinceConditional(new IfModifiedSince(sinceDate))));
    }

    @Test
    public void downloadIfModifiedSinceOnUnchangedContent() {
        InputStream bytes = new ByteArrayInputStream(uploadBytes);
        object.uploadObject(bytes);
        Date sinceDate = new Date((new Date()).getTime()+86400); // one day after now
        try {
            object.downloadObject(new DownloadInstructions().setSinceConditional(new IfModifiedSince(sinceDate)));
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTENT_NOT_MODIFIED, err.getError());
        }
    }

    @Test
    public void uploadAsInputStream() throws IOException {
        InputStream bytes = new ByteArrayInputStream(uploadBytes);
        object.uploadObject(bytes);
        assertTrue(Arrays.equals(uploadBytes, object.downloadObject()));
    }

    @Test
    public void downloadToFile() throws IOException {
        object.uploadObject(uploadBytes);
        object.downloadObject(downloadedFile);
        assertTrue(downloadedFile.exists());
    }

    @Test
    public void uploadFromFile() throws IOException {
        object.uploadObject(uploadBytes);
        object.downloadObject(downloadedFile);
        object.delete();
        object.uploadObject(downloadedFile);
        assertTrue(Arrays.equals(uploadBytes, object.downloadObject()));
    }

    @Test
    public void reuploadToAnExistingObject() throws IOException {
        object.uploadObject(uploadBytes);
        object.uploadObject(uploadBytes); // re-upload
        assertTrue(Arrays.equals(uploadBytes, object.downloadObject()));
    }

    protected void verifyRangeDownload(byte[] uploadBytes, byte[] expectedBytes, AbstractRange range) throws IOException {
        object = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        object.uploadObject(uploadBytes);
        byte[] part = object.downloadObject(new DownloadInstructions().setRange(range));
        assertEquals(expectedBytes.length, part.length);
        assertTrue(Arrays.equals(expectedBytes, part));
    }

    @Test
    public void testMimeTypes() {
        assertEquals("application/andrew-inset", new MimetypesFileTypeMap().getContentType("somefile.ez"));
        assertEquals("video/x-f4v", new MimetypesFileTypeMap().getContentType("somefile.f4v"));
        assertEquals("test/42", new MimetypesFileTypeMap().getContentType("somefile.42")); // added fake format to test reading of mime.types file
    }
}
