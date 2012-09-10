package nl.tweeenveertig.openstack.client;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.util.ClasspathTemplateResource;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

public class OpenStackClientTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        InputStream inputStream = IOUtils.toInputStream(jsonString);
        when(httpEntity.getContent()).thenReturn(inputStream);
    }

    @Test
    public void authenticateSuccessful() throws IOException {
        OpenStackClientImpl openStackClient = new OpenStackClientImpl();
        openStackClient.setHttpClient(httpClient);
        openStackClient.authenticate("user", "pwd", "someurl");
        assertTrue("OpenStack client should have been authenticated", openStackClient.isAuthenticated());
    }

}
