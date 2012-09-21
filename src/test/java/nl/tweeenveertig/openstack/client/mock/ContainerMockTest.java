package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.*;

public class ContainerMockTest {

    private ContainerMock container;

    private StoredObject object;

    @Before
    public void setup() {
        this.container = new ContainerMock(new AccountMock(), "someContainer");
        this.object = this.container.getObject("someObject");
    }

    // TODO implement exists() method before reactivating these chaps
    @Test
    public void getOrCreateDoesNotExist() {
        assertFalse(container.getObject("somevalue").exists());
    }

    @Test
    public void getDoesNotExist() {
        try {
            container.getObject("somevalue").delete();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void publicPrivate() {
        assertFalse(container.isPublic());
        container.makePublic();
        assertTrue(container.isPublic());
        container.makePrivate();
        assertFalse(container.isPublic());
    }

    @Test
    public void numberOfObjects() throws IOException {
        addObjects(3);
        assertEquals(3, container.getObjectCount());
    }

    @Test
    public void listObjects() throws IOException {
        addObjects(3);
        assertEquals(3, container.listObjects().size());
    }

    @Test
    public void deleteObject() throws IOException {
        object.uploadObject(new byte[]{});
        assertEquals(1, container.getObjectCount());
        object.delete();
        assertEquals(0, container.getObjectCount());
    }

    @Test
    public void getInfo() throws IOException {
        addObject("object1", new byte[] { 0x01, 0x02, 0x03 } );
        addObject("object2", new byte[] { 0x01, 0x02 } );
        addObject("object3", new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 } );
        assertEquals(10, container.getBytesUsed());
        assertEquals(3, container.getObjectCount());
    }

    protected void addObject(String name, byte[] bytes) throws IOException {
        StoredObject object = container.getObject(name);
        object.uploadObject(bytes);
    }

    protected void addObjects(int times) throws IOException {
        for (int i = 0; i < times; i++) {
            container.getObject("someobject"+i).uploadObject(new byte[] {});
        }
    }
}
