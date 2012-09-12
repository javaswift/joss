package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.StoredObject;
import org.junit.Before;
import org.junit.Test;

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
//    @Test
//    public void getOrCreateDoesNotExist() {
//        container.getOrCreateObject(this.object);
//    }
//
//    @Test
//    public void getOrCreateExists() {
//        container.getOrCreateObject(this.object);
//        container.getOrCreateObject(this.object);
//    }

//    @Test
//    public void getDoesNotExist() {
//        try {
//            container.getObject(this.object);
//            fail("Should have thrown an exception");
//        } catch (CommandException err) {
//            assertEquals(CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST, err.getError());
//        }
//    }

    @Test
    public void publicPrivate() {
        assertFalse(container.isPublic());
        container.makePublic();
        assertTrue(container.isPublic());
        container.makePrivate();
        assertFalse(container.isPublic());
    }

    @Test
    public void numberOfObjects() {
        addObjects(3);
        assertEquals(3, container.getObjectCount());
    }

    @Test
    public void listObjects() {
        addObjects(3);
        assertEquals(3, container.listObjects().size());
    }

    @Test
    public void deleteObject() {
        object.uploadObject(new byte[]{});
        assertEquals(1, container.getObjectCount());
        object.delete();
        assertEquals(0, container.getObjectCount());
    }

    @Test
    public void getInfo() {
        addObject("object1", new byte[] { 0x01, 0x02, 0x03 } );
        addObject("object2", new byte[] { 0x01, 0x02 } );
        addObject("object3", new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 } );
        assertEquals(10, container.getBytesUsed());
        assertEquals(3, container.getObjectCount());
    }

    protected void addObject(String name, byte[] bytes) {
        StoredObject object = container.getObject(name);
        object.uploadObject(bytes);
    }

    protected void addObjects(int times) {
        for (int i = 0; i < times; i++) {
            container.getObject("someobject"+i).uploadObject(new byte[] {});
        }
    }
}
