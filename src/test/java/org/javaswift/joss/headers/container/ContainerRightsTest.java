package org.javaswift.joss.headers.container;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.javaswift.joss.command.impl.core.BaseCommandTest.prepareHeader;
import static org.mockito.Mockito.when;

public class ContainerRightsTest extends AbstractHeaderTest {

    @Mock
    private HttpResponse response;

    protected void defaultSetup(String publicContainerSetting) {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, ContainerRights.X_CONTAINER_READ, publicContainerSetting, headers);
        when(response.getHeaders(ContainerRights.X_CONTAINER_READ)).thenReturn(headers.toArray(new Header[headers.size()]));
    }

    @Test
    public void addHeader() {
        testHeader(new ContainerRights(true));
    }

    @Test
    public void testPublicContainer() {
        assertEquals(ContainerRights.PUBLIC_CONTAINER, new ContainerRights(true).getHeaderValue());
    }

    @Test
    public void testPrivateContainer() {
        assertEquals("", new ContainerRights(false).getHeaderValue());
    }

    @Test
    public void createPublicContainerFromResponse() {
        defaultSetup(ContainerRights.PUBLIC_CONTAINER);
        assertTrue(ContainerRights.fromResponse(response).isPublic());
    }

    @Test
    public void createPrivateContainerFromResponse() {
        defaultSetup("");
        assertFalse(ContainerRights.fromResponse(response).isPublic());
    }

    @Test
    public void createPrivateContainerFromResponseNoHeadersSet() {
        assertFalse(ContainerRights.fromResponse(response).isPublic());
    }

    @Test
    public void createPrivateContainerFromResponseEmptyHeaderList() {
        when(response.getHeaders(ContainerRights.X_CONTAINER_READ)).thenReturn(new Header[] {} );
        assertFalse(ContainerRights.fromResponse(response).isPublic());
    }
}
