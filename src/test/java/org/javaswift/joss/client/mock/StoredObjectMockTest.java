package org.javaswift.joss.client.mock;

import mockit.Expectations;
import mockit.Mocked;
import org.apache.commons.io.IOUtils;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.headers.object.ObjectManifest;
import org.javaswift.joss.headers.object.conditional.IfModifiedSince;
import org.javaswift.joss.headers.object.conditional.IfNoneMatch;
import org.javaswift.joss.headers.object.range.*;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.SegmentationPlan;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.junit.After;
import org.junit.Assert;
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

    private AccountMock account;

    private ContainerMock container;

    private StoredObjectMock object;

    private byte[] uploadBytes = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };

    private File downloadedFile = new File("download.tmp");

    @Before
    public void setup() {
        account = new AccountMock();
        container = (ContainerMock)new ContainerMock(account, "someContainer").create();
        object = new StoredObjectMock(container, "someObject");
    }

    public void uploadSomeBytes() {
        object.uploadObject(uploadBytes);
    }

    @After
    public void cleanup() {
        downloadedFile.delete();
    }

    @Test
    public void mockPublicHost() {
        AccountMock account = new AccountMock();
        account.setPublicHost("http://127.0.0.1");
        StoredObject object = account.getContainer("someContainer").getObject("someObject");
        assertEquals("http://127.0.0.1/someContainer/someObject", object.getPublicURL());
    }

    @Test
    public void mockPrivateHost() {
        AccountMock account = new AccountMock();
        account.setPrivateHost("api");
        StoredObject object = account.getContainer("someContainer").getObject("someObject");
        assertEquals("api/someContainer/someObject", object.getPrivateURL());
    }

    @Test
    public void getURLChoosesPrivateHost() {
        assertUrl(false, "api/container/object.png");
    }

    @Test
    public void getURLChoosesPublicHost() {
        assertUrl(true, "http://localhost:8080/container/object.png");
    }

    protected void assertUrl(boolean publicContainer, String expectedUrl) {
        Account account = new AccountMock()
                .setPrivateHost("api")
                .setPublicHost("http://localhost:8080");
        Container container = account.getContainer("container");
        container.create();
        if (publicContainer) {
            container.makePublic();
        }
        StoredObject object = container.getObject("object.png");
        object.uploadObject(uploadBytes);
        assertEquals(expectedUrl, object.getURL());
    }

    @Test
    public void saveObject() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.uploadObject(bytes);
        assertEquals(bytes.length, object.downloadObject().length);
    }

    @Test
    public void getInfo() throws IOException {
        object = new StoredObjectMock(container, "plain.txt");
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.uploadObject(bytes);
        assertEquals(3, object.getContentLength());
        assertEquals("5289df737df57326fcdd22597afb1fac", object.getEtag());
        assertEquals("text/plain", object.getContentType());
    }

    @Test
    public void setContentType() {
        uploadSomeBytes();
        object.setContentType("text/plain");
        assertEquals("text/plain", object.getContentType());
    }

    @Test
    public void setDeleteAtDayFromNow() {
        AccountConfig config = new AccountConfig();
        config.setMockAllowEveryone(true);
        ClientMock client = new ClientMock(config);
        Account account = client.authenticate();
        Container container = account.getContainer("images");
        container.create();
        StoredObject object = container.getObject("some-image.jpg");
        object.uploadObject(new byte[]{});
        object.setDeleteAt(new Date(new Date().getTime()+86400));
        object.reload();
        assertNotNull(object.getDeleteAt());
    }

    @Test
    public void setDeleteAfterWithNoObjectDeleter() {
        AccountConfig config = new AccountConfig();
        config.setMockAllowObjectDeleter(false);
        config.setMockAllowEveryone(true);
        Account account = new ClientMock(config).authenticate();
        Container container = account.getContainer("alpha");
        container.create();
        StoredObject object = container.getObject("somefile.png");
        object.uploadObject(new byte[] {});
        object.setDeleteAfter(10);
        assertFalse(object.exists());
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
        object = new StoredObjectMock(container, "plain.txt");
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
        uploadSomeBytes();
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
        StoredObject mockObject = new StoredObjectMock((ContainerMock)object.getContainer(), "some-object") {
            @Override
            public void getInfo(boolean allowErrorLog) {
                throw new NotFoundException(404, CommandExceptionError.ENTITY_DOES_NOT_EXIST);
            }
        };
        assertFalse(mockObject.exists());
    }

    @Test
    public void copyObjectToNonExistingContainer() throws IOException {
        object.uploadObject(uploadBytes);
        Container copyContainer = account.getContainer("copy-container");
        copyContainer.create();
        StoredObject copyObject = copyContainer.getObject("copy-object");
        object.copyObject(copyContainer, copyObject);
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
        uploadSomeBytes();
        Date firstDate = object.getLastModifiedAsDate();
        object.uploadObject(new byte[]{0x01, 0x02, 0x03});
        Date secondDate = object.getLastModifiedAsDate();
        assertNotSame(firstDate, secondDate);
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
        object = new StoredObjectMock(container, "plain.txt");
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

    @Test
    public void headersThatMustBeSetOnAnObject() {
        Container container = this.object.getContainer();
        StoredObject object = container.getObject("someObject");
        object.uploadObject(new UploadInstructions(uploadBytes)
                .setMd5("cafebabe")
                .setContentType("image/jpeg")
        );

        // Etag may not be passed to the manifest, as this will result in a 422, aka checksum failure
        assertEquals("cafebabe", object.getEtag());

        // Content-Type must be adopted
        assertEquals("image/jpeg", object.getContentType());
    }

    @Test
    public void headersThatMustBeSetOnManifest() {
        Container container = this.object.getContainer();
        StoredObject object = container.getObject("someObject");
        object.uploadObject(new UploadInstructions(uploadBytes)
                .setSegmentationSize(3L)
                .setMd5("cafebabe")
                .setContentType("image/jpeg")
        );

        // Etag may not be passed to the manifest, as this will result in a 422, aka checksum failure
        assertNotSame("cafebabe", object.getEtag());

        // Content-Type must be adopted
        assertEquals("image/jpeg", object.getContentType());
    }

    @Test
    public void uploadObjectToBeSegmented() {
        object.uploadObject(new UploadInstructions(uploadBytes).setSegmentationSize(3L));
        Container container = object.getContainer();
        assertEquals(4, container.list().size());
        assertTrue(container.getObjectSegment("someObject", 1).exists());
        assertTrue(container.getObjectSegment("someObject", 2).exists());
        assertTrue(container.getObjectSegment("someObject", 3).exists());
        StoredObject manifest = container.getObject("someObject");
        assertTrue(manifest.exists());
        assertEquals(0, manifest.getContentLength());
    }

    @Test
    public void downloadSegmentedObject() {
        Container container = account.getContainer("segment_container").create();
        StoredObject someOtherObject = container.getObject("ignore-this");
        someOtherObject.uploadObject(uploadBytes);
        StoredObject object = container.getObject("segmented_object");
        object.uploadObject(new UploadInstructions(uploadBytes).setSegmentationSize(3L));
        Assert.assertArrayEquals(uploadBytes, object.downloadObject());
    }

    @Test(expected = NotFoundException.class)
    public void uploadToContainerThatDoesNotExist() {
        Container container = account.getContainer("i-do-not-exist");
        StoredObject object = container.getObject("neither-will-i");
        object.uploadObject(uploadBytes);
    }

    @Test
    public void downloadSegmentedObjectSplitOverVariousFolders() {
        // Upload the segments and the manifest into separate containers
        Container segments = account.getContainer("segments").create();
        StoredObject object1 = segments.getObjectSegment("segment", 1);
        object1.uploadObject(new byte[] { 0x01, 0x02, 0x03 } );
        StoredObject object2 = segments.getObjectSegment("segment", 2);
        object2.uploadObject(new byte[] { 0x04, 0x05 } );
        Container manifests = account.getContainer("manifests").create();
        StoredObject manifest = manifests.getObject("manifest");
        manifest.uploadObject(new UploadInstructions(new byte[] {} ).setObjectManifest(new ObjectManifest("segments/segment")));

        Assert.assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 }, manifest.downloadObject());
    }

    @Test(expected = CommandException.class)
    public void uploadSegmentedObjectsThrowsException(@Mocked final UploadInstructions instructions,
                                                      @Mocked final SegmentationPlan plan) throws IOException {
        new Expectations() {{
            instructions.requiresSegmentation(); result = true;
            instructions.getSegmentationPlan(); result = plan;
            plan.getNextSegment(); result = new IOException();
        }};
        object.uploadObject(instructions);
    }

}