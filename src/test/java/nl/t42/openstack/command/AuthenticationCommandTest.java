package nl.t42.openstack.command;

import nl.t42.openstack.BaseCommandTest;
import nl.t42.openstack.OpenStackClient;
import nl.t42.openstack.model.access.Access;
import nl.t42.openstack.util.ClasspathTemplateResource;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

public class AuthenticationCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        InputStream inputStream = new StringBufferInputStream(jsonString);
        when(httpEntity.getContent()).thenReturn(inputStream);
    }

    @Test
    public void authenticateSuccessful() throws IOException {
        Access access = new AuthenticationCommand(httpClient, "someurl", "user", "pwd").execute();
        assertEquals("a376b74fbdb64a4986cd3234647ff6f8", access.getToken());
    }

    @Test
    public void authenticateFail() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(401);
        try {
            new AuthenticationCommand(httpClient, "someurl", "user", "pwd").execute();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.UNAUTHORIZED, err.getError());
        }
    }

    @Test
    public void unknownError() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(500);
        try {
            new AuthenticationCommand(httpClient, "someurl", "user", "pwd").execute();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.UNKNOWN, err.getError());
        }
    }
}
