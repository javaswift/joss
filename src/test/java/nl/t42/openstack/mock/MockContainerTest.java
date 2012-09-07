package nl.t42.openstack.mock;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.StoreObject;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class MockContainerTest {

    private MockContainer container;

    private StoreObject objectName;

    @Before
    public void setup() {
        this.container = new MockContainer();
        this.objectName = new StoreObject("someobject");
    }

    @Test
    public void getOrCreateDoesNotExist() {
        container.getOrCreateObject(this.objectName);
    }

    @Test
    public void getOrCreateExists() {
        container.getOrCreateObject(this.objectName);
        container.getOrCreateObject(this.objectName);
    }

    @Test
    public void getDoesNotExist() {
        try {
            container.getObject(this.objectName);
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void publicPrivate() {
        assertFalse(container.getInfo().isPublicContainer());
        container.makeContainerPublic();
        assertTrue(container.getInfo().isPublicContainer());
        container.makeContainerPrivate();
        assertFalse(container.getInfo().isPublicContainer());
    }

    @Test
    public void numberOfObjects() {
        addObjects(3);
        assertEquals(3, container.getNumberOfObjects());
    }

    @Test
    public void listObjects() {
        addObjects(3);
        assertEquals(3, container.listObjects().length);
    }

    @Test
    public void deleteObject() {
        container.getOrCreateObject(objectName);
        assertEquals(1, container.getNumberOfObjects());
        container.deleteObject(objectName);
        assertEquals(0, container.getNumberOfObjects());
    }

    @Test
    public void getInfo() {
        addObject("object1", new byte[] { 0x01, 0x02, 0x03 } );
        addObject("object2", new byte[] { 0x01, 0x02 } );
        addObject("object3", new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 } );
        assertEquals(10, container.getInfo().getBytesUsed());
        assertEquals(3, container.getInfo().getObjectCount());
    }

    protected void addObject(String name, byte[] bytes) {
        MockObject object = container.getOrCreateObject(new StoreObject(name));
        object.saveObject(bytes);
    }

    protected void addObjects(int times) {
        for (int i = 0; i < times; i++) {
            container.getOrCreateObject(new StoreObject("someobject"+i));
        }
    }
}
