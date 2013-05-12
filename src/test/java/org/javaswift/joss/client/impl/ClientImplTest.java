package org.javaswift.joss.client.impl;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.model.Account;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.*;

public class ClientImplTest extends BaseCommandTest {

    private ClientImpl client;

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        AccountConfig config = new AccountConfig();
        config.setAllowCaching(false);
        client = new ClientImpl(config);
        client.setHttpClient(httpClient);
    }

    @Test
    public void noTenantSupplied() throws IOException {
        loadSampleJson(new String[] {
                "/sample-access-no-tenant.json",
                "/sample-tenants.json",
                "/sample-access.json",
        });
        Account account = client.authenticate(null, null, "superuser", "somepwd", "http://auth-url");
        assertEquals("http://bfo000024.og.cloudvps.com:80", account.getPublicURL()); // Requires the endpoints
    }

    @Test
    public void authenticate() throws IOException {
        loadSampleJson("/sample-access.json");
        Account account = client.authenticate("sometenant", "tenantid", "superuser", "somepwd", "http://auth-url");
        assertNotNull(account);
        assertFalse(((AccountImpl)account).isAllowCaching());
        assertEquals("http://bfo000024.og.cloudvps.com:80", account.getPublicURL());
    }

    @Test
    public void authenticateWithAPreferredRegion() throws IOException {
        loadSampleJson("/sample-access.json");
        Account account = client.authenticate("sometenant", "tenantid", "superuser", "somepwd", "http://auth-url", "AMS-02");
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


}

