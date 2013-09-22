package org.javaswift.joss.client.impl;

import mockit.NonStrictExpectations;
import org.apache.http.Header;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.model.Account;
import org.junit.Before;
import org.junit.Test;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.javaswift.joss.headers.account.AccountBytesUsed.X_ACCOUNT_BYTES_USED;
import static org.javaswift.joss.headers.account.AccountContainerCount.X_ACCOUNT_CONTAINER_COUNT;
import static org.javaswift.joss.headers.account.AccountMetadata.X_ACCOUNT_META_PREFIX;
import static org.javaswift.joss.headers.account.AccountObjectCount.X_ACCOUNT_OBJECT_COUNT;
import static org.javaswift.joss.headers.account.ServerDate.DATE;

public class ClientImplTest extends BaseCommandTest {

    private ClientImpl client;

    private AccountConfig config;

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        config = new AccountConfig();
        config.setAllowCaching(false);
        client = new ClientImpl(config);
        client.setHttpClient(httpClient);
        prepareMetadata();
    }

    private void prepareMetadata() {
        final List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Description", "Photo album", headers);
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Year", "1984", headers);
        prepareHeader(response, X_ACCOUNT_CONTAINER_COUNT, "7", headers);
        prepareHeader(response, X_ACCOUNT_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_ACCOUNT_BYTES_USED, "654321", headers);
        prepareHeader(response, DATE, "Tue, 28 May 2013 12:17:28 GMT", headers);
        new NonStrictExpectations() {{
            response.getAllHeaders();
            result = headers.toArray(new Header[headers.size()]);
        }};
    }

    @Test
    public void noTenantSupplied() throws IOException {
        loadSampleJson(new String[] {
                "/sample-access-no-tenant.json",
                "/sample-tenants.json",
                "/sample-access.json",
        });
        config.setUsername("superuser");
        config.setPassword("somepwd");
        config.setAuthUrl("http://auth-url");
        Account account = client.authenticate();
        assertEquals("bfo000024id", config.getTenantId());
        assertEquals("bfo000024name", config.getTenantName());
        assertEquals("http://bfo000024.og.cloudvps.com:80", account.getPublicURL()); // Requires the endpoints
    }

    @Test
    public void authenticate() throws IOException {
        loadSampleJson("/sample-access.json");
        config.setTenantName("sometenant");
        config.setTenantId("tenantid");
        config.setUsername("superuser");
        config.setPassword("somepwd");
        config.setAuthUrl("http://auth-url");
        Account account = client.authenticate();
        assertNotNull(account);
        assertFalse(((AccountImpl)account).isAllowCaching());
        assertEquals("http://bfo000024.og.cloudvps.com:80", account.getPublicURL());
    }

    @Test
    public void authenticateWithAPreferredRegion() throws IOException {
        loadSampleJson("/sample-access.json");
        config.setTenantName("sometenant");
        config.setTenantId("tenantid");
        config.setUsername("superuser");
        config.setPassword("somepwd");
        config.setAuthUrl("http://auth-url");
        config.setPreferredRegion("AMS-02");
        Account account = client.authenticate();
        assertNotNull(account);
        assertEquals("http://some-other-url", account.getPublicURL());
    }

    @Test
    public void coverSetters() {
        client.setHttpClient(null);
        AccountConfig config = new AccountConfig();
        config.setSocketTimeout(1000);
        new ClientImpl(config);
    }

    @Test
    public void coverSslValidationDisabled() throws GeneralSecurityException {
        ((X509TrustManager)ClientImpl.gullibleManagers[0]).checkClientTrusted(null, null);
        ((X509TrustManager)ClientImpl.gullibleManagers[0]).checkServerTrusted(null, null);
        ((X509TrustManager)ClientImpl.gullibleManagers[0]).getAcceptedIssuers();
        assertNotNull(ClientImpl.createGullibleSslContext());
        config = new AccountConfig();
        config.setDisableSslValidation(true);
        new ClientImpl(config);
    }

    @Test (expected = RuntimeException.class)
    public void throwSecurityException() throws GeneralSecurityException {
        final PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        new NonStrictExpectations(connectionManager) {{
            connectionManager.getSchemeRegistry();
            result = new GeneralSecurityException();
        }};
        client.disableSslValidation(connectionManager);
    }

}

