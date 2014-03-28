package org.javaswift.joss.client.mock;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.model.*;
import org.javaswift.joss.swift.Swift;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.*;

public class ContainerMockTest {

    private ContainerMock container;

    private StoredObject object;

    @Before
    public void setup() {
        this.container = new ContainerMock(new AccountMock(), "someContainer");
        this.container.create();
        this.object = this.container.getObject("someObject");
    }

    // TODO implement exists() method before reactivating these chaps
    @Test
    public void getOrCreateDoesNotExist() {
        assertFalse(container.getObject("somevalue").exists());
    }

    @Test
    public void fetchMetadataAfterSet() {
        Swift swift = new Swift();
        Account account = new AccountMock(swift)
                .setAllowContainerCaching(false);
        Container container = account.getContainer("use-this");
        container.create();
        container.setMetadata(convertToMetadata(new String[][] { { "Field-1", "value 1" } } ));
        Container container2 = account.getContainer("use-this");
        container2.setMetadata(convertToMetadata(new String[][] { { "Field-2", "value 2" } } ));
        assertEquals(2, container2.getMetadata().size());
    }

    @Test
    public void mustNotDeleteExistingMetadata() {
        Account account = new AccountMock();
        Container container = account.getContainer("use-this");
        container.create();
        container.setMetadata(convertToMetadata(new String[][]{{"Field-1", "value 1"}}));
        container.setMetadata(convertToMetadata(new String[][]{{"Field-2", "value 2"}}));
        assertEquals(2, container.getMetadata().size());
    }

    @Test
    public void mustDeleteEmptyMetadata() {
        Account account = new AccountMock();
        Container container = account.getContainer("use-this");
        container.create();
        container.setMetadata(convertToMetadata(new String[][]{{"Field-1", "value 1"}}));
        container.setMetadata(convertToMetadata(new String[][]{{"Field-1", ""}}));
        assertEquals(0, container.getMetadata().size());
    }

    protected Map<String, Object> convertToMetadata(String[][] keyValuePairs) {
        Map<String, Object> metadata = new TreeMap<String, Object>();
        for (String[] keyValuePair : keyValuePairs) {
            metadata.put(keyValuePair[0], keyValuePair[1]);
        }
        return metadata;
    }

    @Test
    public void getDoesNotExist() {
        try {
            container.getObject("somevalue").delete();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.ENTITY_DOES_NOT_EXIST, err.getError());
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
        assertEquals(3, container.getCount());
    }

    @Test
    public void listObjects() throws IOException {
        addObjects(3);
        assertEquals(3, container.list().size());
    }

    @Test
    public void listDirectory() throws IOException {
        addObjects(3);
        assertEquals(3, container.listDirectory(new Directory("", '/')).size());
    }

    @Test
    public void deleteObject() throws IOException {
        object.uploadObject(new byte[]{});
        assertEquals(1, container.list().size());
        object.delete();
        assertEquals(0, container.list().size());
    }

    @Test
    public void getInfo() throws IOException {
        addObject("object1", new byte[] { 0x01, 0x02, 0x03 } );
        addObject("object2", new byte[] { 0x01, 0x02 } );
        addObject("object3", new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 } );
        assertEquals(10, container.getBytesUsed());
        assertEquals(3, container.getCount());
    }

    @Test
    public void existence() {
        AccountMock account = new AccountMock();
        Container container = account.getContainer("someContainer");
        assertFalse(container.exists());
        container.create();
        assertTrue(container.exists());
        Container newContainer = new ContainerMock(account, "test") {
            @Override
            protected void checkForInfo() {
                throw new NotFoundException(404, CommandExceptionError.ENTITY_DOES_NOT_EXIST);
            }
        };
        assertFalse(newContainer.exists());
    }

    @Test
    public void listObjectsPaged() {
        container.getObject("A").uploadObject(new byte[]{});
        container.getObject("B").uploadObject(new byte[]{});
        StoredObject object3 = container.getObject("C");
        object3.uploadObject(new byte[]{});
        StoredObject object4 = container.getObject("D");
        object4.uploadObject(new byte[]{});
        Collection<StoredObject> objects = container.list(null, "B", 2);
        assertEquals(2, objects.size());
        objects.contains(object3);
        objects.contains(object4);
    }

    @Test
    public void listContainersUsePaginationMap() {
        container.getObject("A").uploadObject(new byte[]{});
        container.getObject("B").uploadObject(new byte[]{});
        StoredObject object3 = container.getObject("C");
        object3.uploadObject(new byte[]{});
        StoredObject object4 = container.getObject("D");
        object4.uploadObject(new byte[]{});
        PaginationMap paginationMap = container.getPaginationMap(2);
        Collection<StoredObject> objects = container.list(paginationMap, 1);
        assertEquals(2, objects.size());
        objects.contains(object3);
        objects.contains(object4);
    }

    @Test
    public void getObject() throws IOException {
        StoredObject object1 = container.getObject("some-object");
        assertFalse(object1.exists());
        object1.uploadObject(new byte[]{0x01});
        StoredObject object2 = container.getObject("some-object");
        assertEquals(object1, object2);
        assertTrue(object1.exists());
    }

    @Test
    public void addMetadata() {
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("name", "value");
        container.setMetadata(metadata);
        assertEquals(1, container.getMetadata().size());
    }

    @Test
    public void getObjectSegment() {
        StoredObject object = container.getObjectSegment("alpha", 14);
        assertEquals("alpha/00000014", object.getName());
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
