package org.javaswift.joss.swift;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.HttpStatus;
import org.javaswift.joss.client.mock.AccountMock;
import org.javaswift.joss.client.mock.ContainerMock;
import org.javaswift.joss.client.mock.StoredObjectMock;
import org.javaswift.joss.command.mock.account.AccountInformationCommandMock;
import org.javaswift.joss.command.mock.core.CommandMock;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.object.DeleteAt;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.DirectoryOrObject;
import org.javaswift.joss.model.StoredObject;
import org.junit.Before;
import org.junit.Test;

public class SwiftTest {

    protected Swift swift;

    protected AccountMock account;

    protected ContainerMock container;

    protected StoredObjectMock object;

    protected UploadInstructions instructions;

    @Before
    public void setup() {
        this.swift = new Swift();
        this.account = new AccountMock(swift);
        this.container = new ContainerMock(account, "does-not-exist");
        this.object = new StoredObjectMock(container, "does-not-exist");
        this.instructions = new UploadInstructions(new byte[] { 0x01, 0x02, 0x03 });
    }

    @Test
    public void saveHashPassword() {
        assertEquals(HttpStatus.SC_NO_CONTENT, this.swift.saveHashPassword("somepwd").getStatus());
        assertEquals("somepwd", this.swift.getHashPassword());
    }

    @Test
    public void saveHashPasswordDoesNotExist() {
        assertEquals(null, this.swift.getHashPassword());
    }

    @Test(expected = CommandException.class)
    public void setOnFileObjectStore() throws Exception {
        new Expectations() {
            @Mocked OnFileObjectStoreLoader loader; {
            new OnFileObjectStoreLoader();
            result = loader;
            loader.createContainers(null, anyString, false);
            result = new IOException();
        }};
        new Swift().setOnFileObjectStore(null, "test", false);
    }

    @Test
    public void getPublicHost() {
        assertEquals("", new Swift().getPublicHost());
        swift = new Swift().setPublicHost("http://localhost:8080/mock");
        assertEquals("http://localhost:8080/mock", swift.getPublicHost());
    }

    @Test
    public void getPrivateHost() {
        assertEquals("", new Swift().getPrivateHost());
        swift = new Swift().setPrivateHost("api");
        assertEquals("api", swift.getPrivateHost());
    }

    @Test
    public void unauthenticated() {
        assertEquals(HttpStatus.SC_UNAUTHORIZED, swift.authenticate("", "", "", "").getStatus());
    }

    @Test
    public void saveContainerMetadataContainerNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.saveContainerMetadata("does-not-exist", null).getStatus());
    }

    @Test
    public void setContainerPrivacyNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.setContainerPrivacy("does-not-exist", false).getStatus());
    }

    @Test
    public void listObjectsNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.listObjects(container, null).getStatus());
    }

    @Test
    public void listDirectoryNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.listDirectory(container, null).getStatus());
    }

    @Test
    public void copySourceContainerDoesNotExist() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.copyObject(container, null, null, null).getStatus());
    }

    @Test
    public void copySourceObjectDoesNotExist() {
        container.create();
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.copyObject(container, object, null, null).getStatus());
    }

    @Test
    public void copyTargetContainerDoesNotExist() {
        container.create();
        object.uploadObject(new byte[]{});
        Container targetContainer = new ContainerMock(account, "the-target");
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.copyObject(container, object, targetContainer, null).getStatus());
    }

    @Test
    public void listEmptyDirectory() {
        container.create();
        assertEquals(HttpStatus.SC_NO_CONTENT, swift.listDirectory(container, new ListInstructions()).getStatus());
    }

    @Test
    public void listDirectories() {
        container.create();
        createObject(container, "abc/alpha.jpg");
        createObject(container, "abc/def/gamma.jpg");
        createObject(container, "abc/def/beta.jpg");
        createObject(container, "delta.jpg");
        createObject(container, "epsilon.jpg");

        assertDirectoryContent(container, null,   3, new String[]{"abc/", "delta.jpg", "epsilon.jpg"}, new boolean[] { true, false, false} );
        assertDirectoryContent(container, "abc/", 2, new String[]{"abc/alpha.jpg", "abc/def/"},        new boolean[] { false, true } );
    }

    private void assertDirectoryContent(ContainerMock container, String prefix, int expectedResults,
                                        String[] names, boolean[] isDirectory) {
        Collection<DirectoryOrObject> files = swift.listDirectory(container, new ListInstructions().setDelimiter('/').setPrefix(prefix)).getPayload();
        assertEquals(expectedResults, files.size());
        Iterator<DirectoryOrObject> iterator = files.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            DirectoryOrObject file = iterator.next();
            assertEquals(names[count], file.getName());
            assertEquals(isDirectory[count], file.isDirectory());
            count++;
        }
    }

    protected StoredObject createObject(Container container, String path) {
        StoredObject object = new StoredObjectMock(container, path);
        object.uploadObject(new byte[] { } );
        return object;
    }

    @Test
    public void copyTargetObjectDoesNotExist() {
        container.create();
        assertTrue(container.exists());
        object.uploadObject(new byte[]{});
        ContainerMock targetContainer = new ContainerMock(account, "the-target-container");
        targetContainer.create();
        StoredObjectMock targetObject = new StoredObjectMock(targetContainer, "the-target-object");
        assertFalse(targetObject.exists());
        swift.copyObject(container, object, targetContainer, targetObject);
        assertTrue(targetObject.exists());
    }

    @Test
    public void copyTargetObjectDoesExist() {
        container.create();
        assertTrue(container.exists());
        object.uploadObject(new byte[]{});
        ContainerMock targetContainer = new ContainerMock(account, "the-target-container");
        targetContainer.create();
        StoredObjectMock targetObject = new StoredObjectMock(targetContainer, "the-target-object");
        targetObject.uploadObject(new byte[] {});
        assertTrue(targetObject.exists());
        swift.copyObject(container, object, targetContainer, targetObject);
        assertTrue(targetObject.exists());
    }

    @Test
    public void deleteObjectContainerNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.deleteObject(container, object).getStatus());
    }

    @Test
    public void saveObjectMetadataContainerNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.saveObjectMetadata(container, object, null).getStatus());
    }

    @Test
    public void saveObjectMetadataObjectNotFound() {
        container.create();
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.saveObjectMetadata(container, object, null).getStatus());
    }

    @Test
    public void getObjectInformationContainerNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.getObjectInformation(container, object).getStatus());
    }

    @Test
    public void getDownloadObjectAsByteArrayContainerNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.downloadObject(container, object, null).getStatus());
    }

    @Test
    public void getDownloadObjectAsByteArrayObjectNotFound() {
        container.create();
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.downloadObject(container, object, null).getStatus());
    }

    @Test
    public void getDownloadObjectToFileContainerNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.downloadObject(container, object, null, null).getStatus());
    }

    @Test
    public void getDownloadObjectAsInputStreamContainerNotFound() {
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.downloadObjectAsInputStream(container, object, null).getStatus());
    }

    @Test
    public void downloadToIllegalFile() {
        container.create();
        object.uploadObject(new byte[]{});
        assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, swift.downloadObject(container, object, new File("non-existing-folder/there-you-go"), new DownloadInstructions()).getStatus());
    }

    @Test
    public void genericStatus() {
        Swift swift = new Swift();
        swift.addIgnore();
        swift.addIgnore();
        swift.addStatus(HttpStatus.SC_NOT_FOUND);
        assertEquals(-1, swift.getStatus(CommandMock.class));
        assertEquals(-1, swift.getStatus(CommandMock.class));
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.getStatus(CommandMock.class));
        assertEquals(-1, swift.getStatus(CommandMock.class));
    }

    @Test
    public void classStatus() {
        Swift swift = new Swift();
        swift.addIgnore(AccountInformationCommandMock.class);
        swift.addIgnore(AccountInformationCommandMock.class);
        swift.addStatus(AccountInformationCommandMock.class, HttpStatus.SC_NOT_FOUND);
        assertEquals(-1, swift.getStatus(CommandMock.class));
        assertEquals(-1, swift.getStatus(AccountInformationCommandMock.class));
        assertEquals(-1, swift.getStatus(AccountInformationCommandMock.class));
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.getStatus(AccountInformationCommandMock.class));
        assertEquals(-1, swift.getStatus(AccountInformationCommandMock.class));
    }

    protected void objectDeleteAt(Swift swift, int secondsFromNow) {
        swift.uploadObject(container, object, instructions);
        assertEquals(HttpStatus.SC_OK, swift.getObjectInformation(container, object).getStatus());
        DeleteAt deleteAt = new DeleteAt(new Date(new Date().getTime()+secondsFromNow));
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(deleteAt);
        swift.saveObjectMetadata(container, object, headers);
    }

    @Test
    public void initiallyObjectDeleterIsNotActive() {
        Swift swift = createObjectDeleterSwift();
        assertFalse(swift.isObjectDeleterActive());
    }

    @Test
    public void afterDelayedDeleteObjectDeleterShutsDown() throws InterruptedException {
        Swift swift = createObjectDeleterSwift();
        swift.createContainer(container.getName());
        objectDeleteAt(swift, 2);
        assertTrue(swift.isObjectDeleterActive());
        swift.getCurrentObjectDeleter(); // cover that line
        assertEquals(HttpStatus.SC_OK, swift.getObjectInformation(container, object).getStatus());
        Thread.sleep(3000);
        assertFalse(swift.isObjectDeleterActive());
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.getObjectInformation(container, object).getStatus());
    }

    @Test
    public void bothTenantFieldsSupplied() {
        Swift swift = new Swift();
        swift.setTenantSupplied("alpha", "beta");
        assertTrue(swift.isTenantSupplied());
    }

    @Test
    public void oneTenantFieldSupplied() {
        Swift swift = new Swift();
        swift.setTenantSupplied(null, "beta");
        assertTrue(swift.isTenantSupplied());
    }

    @Test
    public void noTenantSupplied() {
        Swift swift = new Swift();
        swift.setTenantSupplied(null, null);
        assertFalse(swift.isTenantSupplied());
    }

    protected Swift createObjectDeleterSwift() {
        return new Swift()
                .setAllowObjectDeleter(true)
                .setObjectDeleterStartAfterSeconds(1)
                .setObjectDeleterIntervalSeconds(1);
    }

}
