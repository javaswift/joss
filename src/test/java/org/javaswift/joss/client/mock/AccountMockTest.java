package org.javaswift.joss.client.mock;

import mockit.Expectations;
import mockit.Mocked;
import org.javaswift.joss.command.mock.container.ContainerInformationCommandMock;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.PaginationMap;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.swift.Swift;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.*;

public class AccountMockTest {

    @Test(expected = CommandException.class)
    public void sleep() throws InterruptedException {
        Swift swift = new Swift().setMillisDelay(1);
        Account account = new AccountMock(swift);
        account.reload();
        new Expectations() {
            @Mocked(stubOutClassInitialization = true) Thread unused; {
            Thread.sleep(anyLong);
            result = new InterruptedException(null);
        }};
        account.reload();
    }

    @Test
    public void addMetadata() {
        Account account = new AccountMock();
        account.authenticate(); // Not used in mock implementation
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("name", "Alpha");
        metadata.put("year", 1969);
        account.setMetadata(metadata);
        assertEquals("Alpha", account.getMetadata().get("Name"));
        assertEquals("1969", account.getMetadata().get("Year"));
    }

    @Test
    public void createContainer() {
        Account account = new AccountMock();
        Container container = account.getContainer("town1");
        container.create();
        assertTrue(container.exists());
    }

    @Test
    public void resetContainerCache() {
        Account account = new AccountMock();
        Container container1 = account.getContainer("town1");
        account.resetContainerCache();
        Container container2 = account.getContainer("town1");
        assertNotSame(container1, container2);
    }

    @Test
    public void useContainerCache() {
        Account account = new AccountMock();
        Container container1 = account.getContainer("town1");
        Container container2 = account.getContainer("town1");
        assertSame(container1, container2);
    }

    @Test
    public void doNotUseContainerCache() {
        Account account = new AccountMock()
                .setAllowContainerCaching(false);
        Container container1 = account.getContainer("town1");
        Container container2 = account.getContainer("town1");
        assertNotSame(container1, container2);
    }

    @Test
    public void createContainerAlreadyExists() {
        Account account = new AccountMock();
        Container container = account.getContainer("town1");
        container.create();
        try {
            container.create();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.ENTITY_ALREADY_EXISTS, err.getError());
        }
    }

    @Test
    public void containerDoesNotExist() {
        Account account = new AccountMock();
        try {
            account.getContainer("somevalue").delete();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.ENTITY_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void doubleDeleteContainer() {
        Account account = new AccountMock();
        Container container = account.getContainer("town1");
        container.create();
        container.delete();
        try {
            container.delete();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.ENTITY_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void deleteNonEmptyContainer() throws IOException {
        Account account = new AccountMock();
        Container container = account.getContainer("town1");
        container.create();
        StoredObject object = container.getObject("somefile");
        object.uploadObject(new byte[] { 0x01, 0x02, 0x03 });
        try {
            container.delete();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTAINER_NOT_EMPTY, err.getError());
        }
    }

    @Test
    public void listContainers() {
        Account account = new AccountMock();
        account.getContainer("town1").create();
        account.getContainer("town2").create();
        account.getContainer("town3").create();
        assertEquals(3, account.list().size());
    }

    @Test
    public void listContainersPaged() {
        Account account = new AccountMock();
        account.getContainer("town1").create();
        account.getContainer("town2").create();
        Container town3 = account.getContainer("town3").create();
        Container town4 = account.getContainer("town4").create();
        account.getContainer("town5").create();
        Collection<Container> towns = account.list(null, "town2", 2);
        assertEquals(2, towns.size());
        towns.contains(town3);
        towns.contains(town4);
    }

    @Test
    public void listContainersUsePaginationMap() {
        Account account = new AccountMock();
        account.getContainer("town1").create();
        account.getContainer("town2").create();
        Container town3 = account.getContainer("town3").create();
        Container town4 = account.getContainer("town4").create();
        account.getContainer("town5").create();
        PaginationMap paginationMap = account.getPaginationMap(2);
        assertEquals(3, paginationMap.getNumberOfPages());
        assertEquals(5, paginationMap.getNumberOfRecords());
        Collection<Container> towns = account.list(paginationMap, 1);
        assertEquals(2, towns.size());
        towns.contains(town3);
        towns.contains(town4);
    }

    @Test
    public void copyObject() throws IOException {
        Container container = new AccountMock().getContainer("town1");
        container.create();
        StoredObject sourceObject = container.getObject("source-object");
        StoredObject targetObject = container.getObject("target-object");
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        sourceObject.uploadObject(bytes);
        sourceObject.copyObject(container, targetObject);
        assertEquals(sourceObject.downloadObject().length, targetObject.downloadObject().length);
        assertEquals(sourceObject.getContentLength(), targetObject.getContentLength());
        assertEquals(sourceObject.getEtag(), targetObject.getEtag());
    }

    @Test
    public void getInfo() throws IOException {
        Account account = new AccountMock();
        Container container1 = account.getContainer("alpha");
        container1.create();
        StoredObject object = container1.getObject("1");
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        object.uploadObject(bytes);
        assertEquals(3 , account.getBytesUsed());
        assertEquals(1, account.getCount());
        assertEquals(1, account.getObjectCount());
    }

    @Test
    public void getContainer() {
        Account account = new AccountMock();
        Container container1 = account.getContainer("Alpha");
        container1.create();
        Container container2 = account.getContainer("Alpha");
        assertEquals(container1, container2);
    }

    @Test(expected = NotFoundException.class)
    public void forceGenericErrorStatus() {
        Swift swift = new Swift();
        swift.addIgnore(); // Container create is fine
        swift.addStatus(404); // The get info must fail
        Account account = new AccountMock(swift);
        Container container = account.getContainer("alpha");
        container.create();
        container.getCount();
    }

    @Test(expected = NotFoundException.class)
    public void forceSpecificErrorStatus() {
        Swift swift = new Swift();
        swift.addStatus(ContainerInformationCommandMock.class, 404);
        Account account = new AccountMock(swift);
        Container container = account.getContainer("alpha");
        container.create();
        container.getCount();
    }

    @Test
    public void getOriginalHost() {
        Swift swift = new Swift();
        swift.setPublicHost("http://configured.url.because.there.is.no.other");
        Account account = new AccountMock(swift);
        assertEquals("http://configured.url.because.there.is.no.other", account.getOriginalHost());
    }

    @Test
    public void setHashPassword() {
        Swift swift = new Swift();
        Account account = new AccountMock(swift);
        account.setHashPassword("somepwd");
        assertEquals("somepwd", swift.getHashPassword());
    }

}
