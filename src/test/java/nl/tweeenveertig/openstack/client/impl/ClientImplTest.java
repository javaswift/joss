package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.util.ClasspathTemplateResource;
import org.apache.commons.io.IOUtils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

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
    }

    @Test
    public void authenticate() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        InputStream inputStream = IOUtils.toInputStream(jsonString);
        when(httpEntity.getContent()).thenReturn(inputStream);
        Account account = client.authenticate("sometenant", "superuser", "somepwd", "someregion");
        assertNotNull(account);
    }

}
