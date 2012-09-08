package nl.t42.openstack.mock;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

public class MockAccountTest {

    @Test
    public void addMetadata() {
        MockAccount account = new MockAccount();
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("name", "Alpha");
        metadata.put("year", 1969);
        account.setInfo(metadata);
        assertEquals("Alpha", account.getInfo().getMetadata().get("name"));
        assertEquals("1969", account.getInfo().getMetadata().get("year"));
    }

    @Test
    public void createContainer() {
        MockAccount account = new MockAccount();
        Container container = new Container("town1");
        account.createContainer(container);
        assertNotNull(account.getContainer(container));
    }

    @Test
    public void createContainerAlreadyExists() {
        MockAccount account = new MockAccount();
        Container container = new Container("town1");
        account.createContainer(container);
        try {
            account.createContainer(container);
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTAINER_ALREADY_EXISTS, err.getError());
        }
    }

    @Test
    public void containerDoesNotExist() {
        MockAccount account = new MockAccount();
        try {
            account.getContainer(new Container("somevalue"));
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTAINER_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void deleteContainer() {
        MockAccount account = new MockAccount();
        Container container = new Container("town1");
        account.createContainer(container);
        account.deleteContainer(container);
        try {
            account.getContainer(container);
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTAINER_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void deleteNonEmptyContainer() {
        MockAccount account = new MockAccount();
        Container containerName = new Container("town1");
        MockContainer container = account.createContainer(containerName);
        StoreObject objectName = new StoreObject("somefile");
        MockObject object = container.getOrCreateObject(objectName);
        object.saveObject(new byte[] { 0x01, 0x02, 0x03 });
        try {
            account.deleteContainer(containerName);
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTAINER_NOT_EMPTY, err.getError());
        }
    }

    @Test
    public void listContainers() {
        MockAccount account = new MockAccount();
        account.createContainer(new Container("town1"));
        account.createContainer(new Container("town2"));
        account.createContainer(new Container("town3"));
        assertEquals(3, account.listContainers().length);
    }

    @Test
    public void copyObject() {
        Container containerName = new Container("town1");
        StoreObject sourceObjectName = new StoreObject("source-object");
        StoreObject targetObjectName = new StoreObject("target-object");
        MockAccount account = new MockAccount();
        MockObject sourceObject = account.createContainer(containerName).getOrCreateObject(sourceObjectName);
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        sourceObject.saveObject(bytes);
        account.copyObject(containerName, sourceObjectName, containerName, targetObjectName);
        MockObject targetObject = account.getContainer(containerName).getObject(targetObjectName);
        assertEquals(sourceObject.getObject().length, targetObject.getObject().length);
        assertEquals(sourceObject.getInfo().getContentLength(), targetObject.getInfo().getContentLength());
        assertEquals(sourceObject.getInfo().getEtag(), targetObject.getInfo().getEtag());
    }

    @Test
    public void getInfo() {
        // TO DO
    }

}
