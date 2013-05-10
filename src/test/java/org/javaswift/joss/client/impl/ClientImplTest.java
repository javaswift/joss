package org.javaswift.joss.client.impl;

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
        client = new ClientImpl();
        client.setHttpClient(httpClient);
        client.setAllowCaching(false);
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
        new ClientImpl(1000);
    }


}

