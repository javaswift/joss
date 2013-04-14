package org.javaswift.joss.headers.container;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.javaswift.joss.command.impl.core.BaseCommandTest.prepareHeader;
import static org.javaswift.joss.command.impl.core.BaseCommandTest.prepareHeadersForRetrieval;

public class ContainerRightsTest extends AbstractHeaderTest {

    @Mocked
    private HttpResponse response;

    protected void defaultSetup(String publicContainerSetting) {
        final List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, ContainerRights.X_CONTAINER_READ, publicContainerSetting, headers);
        prepareHeadersForRetrieval(response, headers);
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
        new NonStrictExpectations() {{
            response.getHeaders(ContainerRights.X_CONTAINER_READ);
            result = new Header[] {};
        }};
        assertFalse(ContainerRights.fromResponse(response).isPublic());
    }
}
