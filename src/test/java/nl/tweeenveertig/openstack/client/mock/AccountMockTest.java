package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.*;

public class AccountMockTest {

    @Test
    public void addMetadata() {
        Account account = new AccountMock();
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("name", "Alpha");
        metadata.put("year", 1969);
        account.setMetadata(metadata);
        assertEquals("Alpha", account.getMetadata().get("name"));
        assertEquals("1969", account.getMetadata().get("year"));
    }

    @Test
    public void createContainer() {
        Account account = new AccountMock();
        Container container = account.getContainer("town1");
        container.create();
        assertTrue(container.exists());
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
            assertEquals(CommandExceptionError.CONTAINER_ALREADY_EXISTS, err.getError());
        }
    }

    @Test
    public void containerDoesNotExist() {
        Account account = new AccountMock();
        try {
            account.getContainer("somevalue").delete();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTAINER_DOES_NOT_EXIST, err.getError());
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
            assertEquals(CommandExceptionError.CONTAINER_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void deleteNonEmptyContainer() {
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
        assertEquals(3, account.listContainers().size());
    }

    @Test
    public void copyObject() {
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
    public void getInfo() {
        // TODO
    }

}
