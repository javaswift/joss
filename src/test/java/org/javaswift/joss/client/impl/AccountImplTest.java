package org.javaswift.joss.client.impl;

import org.javaswift.joss.model.Container;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.model.PaginationMap;
import org.javaswift.joss.util.ClasspathTemplateResource;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.javaswift.joss.headers.account.AccountBytesUsed.X_ACCOUNT_BYTES_USED;
import static org.javaswift.joss.headers.account.AccountContainerCount.X_ACCOUNT_CONTAINER_COUNT;
import static org.javaswift.joss.headers.account.AccountMetadata.X_ACCOUNT_META_PREFIX;
import static org.javaswift.joss.headers.account.AccountObjectCount.X_ACCOUNT_OBJECT_COUNT;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountImplTest extends BaseCommandTest {

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
    }

    protected void prepareMetadata() {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Year", "1989", headers);
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Company", "42 BV", headers);
        prepareHeader(response, X_ACCOUNT_CONTAINER_COUNT, "7", headers);
        prepareHeader(response, X_ACCOUNT_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_ACCOUNT_BYTES_USED, "654321", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
    }

    @Test
    public void listContainers() throws IOException {
        when(httpEntity.getContent()).thenReturn(
                IOUtils.toInputStream(new ClasspathTemplateResource("/sample-container-list.json").loadTemplate()));
        Collection<Container> containers = account.list();
        assertEquals(4, containers.size());
    }

    @Test
    public void paginationMap() throws IOException {
        when(httpEntity.getContent()).thenReturn(
                IOUtils.toInputStream(new ClasspathTemplateResource("/sample-container-list.json").loadTemplate()));
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_ACCOUNT_CONTAINER_COUNT, "4", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
        when(statusLine.getStatusCode()).thenReturn(204);
        PaginationMap paginationMap = account.getPaginationMap(2);
        assertEquals(2, paginationMap.getPageSize());
        assertEquals(2, paginationMap.getNumberOfPages());
        assertEquals(4, paginationMap.getNumberOfRecords());
        assertEquals(null, paginationMap.getMarker(0));
        assertEquals("Arnhem", paginationMap.getMarker(1));
    }

    @Test
    public void setMetadata() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", "1989");
        metadata.put("Company", "42 BV");
        account.setMetadata(metadata);
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("1989", requestArgument.getValue().getFirstHeader(X_ACCOUNT_META_PREFIX + "Year").getValue());
        assertEquals("42 BV", requestArgument.getValue().getFirstHeader(X_ACCOUNT_META_PREFIX + "Company").getValue());
    }

    @Test
    public void getMetadata() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        prepareMetadata();
        assertEquals("1989", account.getMetadata().get("Year"));
        assertEquals("42 BV", account.getMetadata().get("Company"));
        assertEquals(7, account.getCount());
        assertEquals(123, account.getObjectCount());
        assertEquals(654321, account.getBytesUsed());
    }

    @Test
    public void getPublicURL() {
        assertEquals("http://someurl.public", account.getPublicURL());
    }

    @Test
    public void isAllowCaching() throws IOException {
        account = new AccountImpl(null, httpClient, defaultAccess, false); // Caching is turned off
        when(statusLine.getStatusCode()).thenReturn(204);
        prepareMetadata();
        account.getCount();
        account.getCount();
        // Because caching is turned off, the HTTP call must be made twice
        verify(httpClient, times(2)).execute(requestArgument.capture());
    }

    @Test
    public void isInfoRetrieved() throws IOException {
        assertFalse(account.isInfoRetrieved());
        when(statusLine.getStatusCode()).thenReturn(204);
        prepareMetadata();
        account.getCount();
        assertTrue(account.isInfoRetrieved());
    }

    @Test
    public void reload() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        prepareMetadata();
        account.getCount();
        account.reload();
        verify(httpClient, times(2)).execute(requestArgument.capture());
    }

    @Test
    public void callCounter() {
        account.increaseCallCounter();
        assertEquals(1, account.getNumberOfCalls());
    }
}
