package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static nl.tweeenveertig.openstack.headers.account.AccountBytesUsed.X_ACCOUNT_BYTES_USED;
import static nl.tweeenveertig.openstack.headers.account.AccountContainerCount.X_ACCOUNT_CONTAINER_COUNT;
import static nl.tweeenveertig.openstack.headers.account.AccountMetadata.X_ACCOUNT_META_PREFIX;
import static nl.tweeenveertig.openstack.headers.account.AccountObjectCount.X_ACCOUNT_OBJECT_COUNT;
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
        InputStream inputStream = IOUtils.toInputStream(
                "Alpha\n"+
                "Beta\n" +
                "Gamma");
        when(httpEntity.getContent()).thenReturn(inputStream);
        Collection<Container> containers = account.listContainers();
        assertEquals(3, containers.size());
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
        assertEquals(7, account.getContainerCount());
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
        account.getContainerCount();
        account.getContainerCount();
        // Because caching is turned off, the call must be made twice
        verify(httpClient, times(2)).execute(requestArgument.capture());
    }

    @Test
    public void isInfoRetrieved() throws IOException {
        assertFalse(account.isInfoRetrieved());
        when(statusLine.getStatusCode()).thenReturn(204);
        prepareMetadata();
        account.getContainerCount();
        assertTrue(account.isInfoRetrieved());
    }

    @Test
    public void reload() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        prepareMetadata();
        account.getContainerCount();
        account.reload();
        verify(httpClient, times(2)).execute(requestArgument.capture());
    }

}
