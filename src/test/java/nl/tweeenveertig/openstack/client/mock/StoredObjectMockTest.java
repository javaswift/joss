package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.mock.scheduled.ObjectDeleter;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.exception.NotFoundException;
import nl.tweeenveertig.openstack.headers.object.conditional.IfModifiedSince;
import nl.tweeenveertig.openstack.headers.object.conditional.IfNoneMatch;
import nl.tweeenveertig.openstack.model.DownloadInstructions;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.headers.object.range.*;
import nl.tweeenveertig.openstack.model.UploadInstructions;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ StoredObjectMock.class, IOUtils.class })
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
    public void setDeleteAfterWithNoObjectDeleter() {
        Account account = new ClientMock().setAllowObjectDeleter(false).setAllowEveryone(true).authenticate(null, null, null, null);
        StoredObject object = account.getContainer("alpha").getObject("somefile.png");
        object.setDeleteAfter(10);
    }

    @Test
    public void setDeleteAfter() {
        Account account = new ClientMock().setAllowObjectDeleter(false).setAllowEveryone(true).authenticate(null, null, null, null);
        ObjectDeleter objectDeleter = Mockito.mock(ObjectDeleter.class);
        ((AccountMock) account).setObjectDeleter(objectDeleter);
        StoredObject object = account.getContainer("alpha").getObject("somefile.png");
        object.setDeleteAfter(10);
        verify(objectDeleter).scheduleForDeletion(same(object), isA(Date.class));
        assertNotNull(object.getDeleteAt());
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
                throw new NotFoundException(404, CommandExceptionError.ENTITY_DOES_NOT_EXIST);
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

    @Test(expected = CommandException.class)
    public void downloadObjectThrowsIOException() throws Exception {
        final ByteArrayInputStream inputStream = mock(ByteArrayInputStream.class);
        whenNew(ByteArrayInputStream.class).withArguments(null).thenReturn(inputStream);
        final FileOutputStream outputStream = mock(FileOutputStream.class);
        whenNew(FileOutputStream.class).withArguments(downloadedFile).thenReturn(outputStream);
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(IOUtils.copy(any(InputStream.class), any(OutputStream.class))).thenThrow(new IOException());
        StoredObjectMock storedObject = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        storedObject.downloadObject(null, new DownloadInstructions());
    }

    @Test(expected = CommandException.class)
    public void downloadObjectThrowsIOExceptionNoStreamsToClose() throws Exception {
        whenNew(ByteArrayInputStream.class).withArguments(null).thenThrow(new IOException());
        StoredObjectMock storedObject = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        storedObject.downloadObject(null, new DownloadInstructions());
    }

    @Test(expected = CommandException.class)
    public void uploadObjectWithInstructionsThrowsIOException() throws Exception {
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(IOUtils.toByteArray((any(InputStream.class)))).thenThrow(new IOException());
        StoredObjectMock storedObject = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        storedObject.uploadObject(new UploadInstructions(new byte[] { }));
    }

    @Test(expected = CommandException.class)
    public void uploadObjectWithInputStreamThrowsIOException() throws Exception {
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(IOUtils.toByteArray((any(InputStream.class)))).thenThrow(new IOException());
        StoredObjectMock storedObject = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        storedObject.uploadObject(new ByteArrayInputStream(new byte[] { }));
    }

    @Test(expected = CommandException.class)
    public void uploadObjectWithFileThrowsIOException() throws Exception {
        final FileInputStream outputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withArguments(downloadedFile).thenReturn(outputStream);
        final ByteArrayOutputStream inputStream = mock(ByteArrayOutputStream.class);
        whenNew(ByteArrayOutputStream.class).withNoArguments().thenReturn(inputStream);
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(IOUtils.copy(any(InputStream.class), any(OutputStream.class))).thenThrow(new IOException());
        StoredObjectMock storedObject = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        storedObject.uploadObject(downloadedFile);
    }

    @Test(expected = CommandException.class)
    public void uploadObjectWithFileThrowsIOExceptionNoStreamsToClose() throws Exception {
        whenNew(FileInputStream.class).withArguments(downloadedFile).thenThrow(new IOException());
        StoredObjectMock storedObject = new StoredObjectMock(new ContainerMock(new AccountMock(), "someContainer"), "plain.txt");
        storedObject.uploadObject(downloadedFile);
    }

}