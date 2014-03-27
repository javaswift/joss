package org.javaswift.joss.client.impl;

import mockit.Verifications;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.headers.account.HashPassword;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.PaginationMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static junit.framework.Assert.*;
import static org.javaswift.joss.headers.account.AccountBytesUsed.X_ACCOUNT_BYTES_USED;
import static org.javaswift.joss.headers.account.AccountContainerCount.X_ACCOUNT_CONTAINER_COUNT;
import static org.javaswift.joss.headers.account.AccountMetadata.X_ACCOUNT_META_PREFIX;
import static org.javaswift.joss.headers.account.AccountObjectCount.X_ACCOUNT_OBJECT_COUNT;

public class AccountImplTest extends BaseCommandTest {

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
    }

    protected void prepareMetadata() {
        final List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Year", "1989", headers);
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Company", "42 BV", headers);
        prepareHeader(response, X_ACCOUNT_CONTAINER_COUNT, "7", headers);
        prepareHeader(response, X_ACCOUNT_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_ACCOUNT_BYTES_USED, "654321", headers);
        prepareHeadersForRetrieval(response, headers);
    }

    @Test
    public void listContainers() throws IOException {
        loadSampleJson("/sample-container-list.json");
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_ACCOUNT_CONTAINER_COUNT, "4", headers);
        prepareHeadersForRetrieval(response, headers);
        Collection<Container> containers = account.list();
        assertEquals(4, containers.size());
    }

    @Test
    public void setHashPassword() throws IOException {
        account.setHashPassword("somepwd");
        verifyHeaderValue("somepwd", X_ACCOUNT_META_PREFIX + HashPassword.X_ACCOUNT_TEMP_URL_KEY, "POST");
    }

    @Test
    public void paginationMap() throws IOException {
        loadSampleJson("/sample-container-list.json");
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_ACCOUNT_CONTAINER_COUNT, "4", headers);
        prepareHeadersForRetrieval(response, headers);
        PaginationMap paginationMap = account.getPaginationMap(2);
        assertEquals(2, paginationMap.getPageSize());
        assertEquals(2, paginationMap.getNumberOfPages());
        assertEquals(4, paginationMap.getNumberOfRecords());
        assertEquals(null, paginationMap.getMarker(0));
        assertEquals("Arnhem", paginationMap.getMarker(1));
    }

    @Test
    public void setMetadata() throws IOException {
        expectStatusCode(204);
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", "1989");
        metadata.put("Company", "42 BV");
        account.setMetadata(metadata);
        verifyHeaderValue("1989", X_ACCOUNT_META_PREFIX + "Year", "POST");
        verifyHeaderValue("42 BV", X_ACCOUNT_META_PREFIX + "Company", "POST");
    }

    @Test
    public void getMetadata() throws IOException {
        expectStatusCode(204);
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
    public void getPublicUROverride() {
        account = new AccountImpl(null, httpClient, defaultAccess, true, TempUrlHashPrefixSource.PUBLIC_URL_PATH, '/');
        account.setPublicHost("http://static.someurl.com");
        assertEquals("http://static.someurl.com", account.getPublicURL());
    }

    @Test
    public void isAllowCaching() throws IOException {
        account = new AccountImpl(null, httpClient, defaultAccess, false, TempUrlHashPrefixSource.PUBLIC_URL_PATH, '/'); // Caching is turned off
        prepareMetadata();
        account.getCount();
        account.getCount();
        // Because caching is turned off, the HTTP call must be made twice
        new Verifications() {{
            httpClient.execute((HttpRequestBase)any); times = 2;
        }};
    }

    @Test
    public void isInfoRetrieved() throws IOException {
        assertFalse(account.isInfoRetrieved());
        expectStatusCode(204);
        prepareMetadata();
        account.getCount();
        assertTrue(account.isInfoRetrieved());
    }

    @Test
    public void reload() throws IOException {
        prepareMetadata();
        account.getCount();
        account.reload();
        new Verifications() {{
            httpClient.execute((HttpRequestBase)any); times = 2;
        }};
    }

    @Test
    public void callCounter() {
        account.increaseCallCounter();
        assertEquals(1, account.getNumberOfCalls());
    }

    @Test
    public void getOriginalHost() {
        account.setPublicHost("not.the.right.url");
        assertEquals("http://someurl.public", account.getOriginalHost());
    }
}
