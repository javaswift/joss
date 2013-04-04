package org.javaswift.joss.client.impl;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.util.ClasspathTemplateResource;
import org.apache.commons.io.IOUtils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

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
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        InputStream inputStream = IOUtils.toInputStream(jsonString);
        when(httpEntity.getContent()).thenReturn(inputStream);
        Account account = client.authenticate("sometenant", "superuser", "somepwd", "http://auth-url");
        assertNotNull(account);
        assertFalse(((AccountImpl)account).isAllowCaching());
        assertEquals("http://bfo000024.og.cloudvps.com:80", account.getPublicURL());
    }

    @Test
    public void authenticateWithAPreferredRegion() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        InputStream inputStream = IOUtils.toInputStream(jsonString);
        when(httpEntity.getContent()).thenReturn(inputStream);
        Account account = client.authenticate("sometenant", "superuser", "somepwd", "http://auth-url", "AMS-02");
        assertNotNull(account);
        assertEquals("http://some-other-url", account.getPublicURL());
    }

    @Test
    public void setEmptyHttpClient() {
        client.setHttpClient(null);
    }

}

