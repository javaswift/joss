package org.javaswift.joss.client.impl;

import org.apache.http.Header;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.headers.container.ContainerRights;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static junit.framework.Assert.*;
import static org.javaswift.joss.headers.container.ContainerBytesUsed.X_CONTAINER_BYTES_USED;
import static org.javaswift.joss.headers.container.ContainerMetadata.X_CONTAINER_META_PREFIX;
import static org.javaswift.joss.headers.container.ContainerObjectCount.X_CONTAINER_OBJECT_COUNT;
import static org.javaswift.joss.headers.container.ContainerRights.X_CONTAINER_READ;

public class ContainerImplTest extends BaseCommandTest {

    private Container container;

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        container = account.getContainer("alpha");
    }

    protected void prepareMetadata() {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Year", "1989", headers);
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Company", "42 BV", headers);
        prepareHeader(response, X_CONTAINER_READ, ContainerRights.PUBLIC_CONTAINER, headers);
        prepareHeader(response, X_CONTAINER_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_CONTAINER_BYTES_USED, "654321", headers);
        prepareHeadersForRetrieval(response, headers);
    }

    @Test
    public void listObjects() throws IOException {
        loadSampleJson("/sample-object-list.json");
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_CONTAINER_OBJECT_COUNT, "4", headers);
        prepareHeadersForRetrieval(response, headers);
        Collection<StoredObject> objects = container.list();
        assertEquals(4, objects.size());
    }

    @Test
    public void makePublic() throws IOException {
        expectStatusCode(202);
        container.makePublic();
        verifyHeaderValue(ContainerRights.PUBLIC_CONTAINER, X_CONTAINER_READ);
    }

    @Test
    public void makePrivate() throws IOException {
        expectStatusCode(202);
        container.makePrivate();
        verifyHeaderValue("", X_CONTAINER_READ);
    }

    @Test
    public void createDelete() {
        expectStatusCode(201);
        container.create();
        expectStatusCode(204);
        container.delete();
    }

    @Test
    public void setMetadata() throws IOException {
        expectStatusCode(204);
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", "1989");
        metadata.put("Company", "42 BV");
        container.setMetadata(metadata);
        verifyHeaderValue("1989", X_CONTAINER_META_PREFIX + "Year", "POST");
        verifyHeaderValue("42 BV", X_CONTAINER_META_PREFIX + "Company", "POST");
    }

    @Test
    public void getMetadata() throws IOException {
        expectStatusCode(204);
        prepareMetadata();
        assertEquals("1989", container.getMetadata().get("Year"));
        assertEquals("42 BV", container.getMetadata().get("Company"));
        assertTrue(container.isPublic());
        assertEquals(123, container.getCount());
        assertEquals(654321, container.getBytesUsed());
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void compareContainers() {
        Container container1 = account.getContainer("alpha");
        Container container2 = account.getContainer("beta");
        assertFalse(container1.equals("alpha"));
        assertFalse(container1.equals(container2));
        Map<Container, String> containers = new TreeMap<Container, String>();
        containers.put(container1, container1.getName());
        containers.put(container2, container2.getName());
        assertEquals(container1.getName(), containers.get(container1));
        assertEquals(container2.getName(), containers.get(container2));
        assertEquals(container1.getName().hashCode(), container1.hashCode());
    }

}
