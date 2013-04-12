package org.javaswift.joss.swift;

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
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Container;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest( Swift.class )
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

    @Test(expected = CommandException.class)
    public void setOnFileObjectStore() throws Exception {
        OnFileObjectStoreLoader loader = mock(OnFileObjectStoreLoader.class);
        whenNew(OnFileObjectStoreLoader.class).withNoArguments().thenReturn(loader);
        doThrow(new IOException()).when(loader).createContainers(anyString());
        new Swift().setOnFileObjectStore("test");
    }

    @Test
    public void getPublicUrl() {
        assertEquals("", new Swift().getPublicURL());
        swift = new Swift().setHost("http://localhost:8080/mock");
        assertEquals("http://localhost:8080/mock", swift.getPublicURL());
    }

    @Test
    public void unauthenticated() {
        assertEquals(HttpStatus.SC_UNAUTHORIZED, swift.authenticate("", "", "").getStatus());
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

    @Test
    public void scheduledDeleteForcingShutdown() throws InterruptedException {
        Swift swift = new Swift()
                .setAllowObjectDeleter(true)
                .setObjectDeleterStartAfterSeconds(1)
                .setObjectDeleterIntervalSeconds(1);
        swift.createContainer(container.getName());
        objectDeleteAt(swift);
        objectDeleteAt(swift);
    }

    protected void objectDeleteAt(Swift swift) throws InterruptedException {
        swift.uploadObject(container, object, instructions);
        assertEquals(HttpStatus.SC_OK, swift.getObjectInformation(container, object).getStatus());
        DeleteAt deleteAt = new DeleteAt(new Date(new Date().getTime()+2)); // 2 seconds from now
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(deleteAt);
        swift.saveObjectMetadata(container, object, headers);
        Thread.sleep(3000);
        assertEquals(HttpStatus.SC_NOT_FOUND, swift.getObjectInformation(container, object).getStatus());
    }

}
