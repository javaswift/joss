package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.headers.container.ContainerRights;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static nl.tweeenveertig.openstack.headers.container.ContainerBytesUsed.X_CONTAINER_BYTES_USED;
import static nl.tweeenveertig.openstack.headers.container.ContainerObjectCount.X_CONTAINER_OBJECT_COUNT;
import static nl.tweeenveertig.openstack.headers.container.ContainerMetadata.X_CONTAINER_META_PREFIX;
import static nl.tweeenveertig.openstack.headers.container.ContainerRights.X_CONTAINER_READ;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContainerImplTest extends BaseCommandTest {

    private Container container;

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        when(statusLine.getStatusCode()).thenReturn(202);
        container = account.getContainer("alpha");
    }

    protected void prepareMetadata() {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Year", "1989", headers);
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Company", "42 BV", headers);
        prepareHeader(response, X_CONTAINER_READ, ContainerRights.PUBLIC_CONTAINER, headers);
        prepareHeader(response, X_CONTAINER_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_CONTAINER_BYTES_USED, "654321", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
    }

    @Test
    public void listObjects() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        InputStream inputStream = IOUtils.toInputStream(
                "alpha.jpg\n" +
                "beta.png\n" +
                "gamma.docx");
        when(httpEntity.getContent()).thenReturn(inputStream);
        Collection<StoredObject> objects = container.listObjects();
        assertEquals(3, objects.size());
    }

    @Test
    public void makePublic() throws IOException {
        container.makePublic();
        checkContainerRights(ContainerRights.PUBLIC_CONTAINER);
    }

    @Test
    public void makePrivate() throws IOException {
        container.makePrivate();
        checkContainerRights("");
    }

    @Test
    public void createDelete() {
        when(statusLine.getStatusCode()).thenReturn(201);
        container.create();
        when(statusLine.getStatusCode()).thenReturn(204);
        container.delete();
    }

    @Test
    public void setMetadata() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", "1989");
        metadata.put("Company", "42 BV");
        container.setMetadata(metadata);
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("1989", requestArgument.getValue().getFirstHeader(X_CONTAINER_META_PREFIX + "Year").getValue());
        assertEquals("42 BV", requestArgument.getValue().getFirstHeader(X_CONTAINER_META_PREFIX + "Company").getValue());
    }

    @Test
    public void getMetadata() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        prepareMetadata();
        assertEquals("1989", container.getMetadata().get("Year"));
        assertEquals("42 BV", container.getMetadata().get("Company"));
        assertTrue(container.isPublic());
        assertEquals(123, container.getObjectCount());
        assertEquals(654321, container.getBytesUsed());
    }

    protected void checkContainerRights(String expectedRightsValue) throws IOException {
        verify(httpClient).execute(requestArgument.capture());
        assertEquals(expectedRightsValue, requestArgument.getValue().getFirstHeader(ContainerRights.X_CONTAINER_READ).getValue());
    }
}
