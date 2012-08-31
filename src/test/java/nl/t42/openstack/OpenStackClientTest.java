package nl.t42.openstack;

import nl.t42.openstack.util.ClasspathTemplateResource;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

public class OpenStackClientTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        InputStream inputStream = new StringBufferInputStream(jsonString);
        when(httpEntity.getContent()).thenReturn(inputStream);
    }

    @Test
    public void authenticateSuccessful() throws IOException {
        OpenStackClient openStackClient = new OpenStackClient();
        openStackClient.setHttpClient(httpClient);
        openStackClient.authenticate("user", "pwd", "someurl");
        assertTrue("OpenStack client should have been authenticated", openStackClient.isAuthenticated());
    }

}
