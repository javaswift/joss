package nl.tweeenveertig.openstack.command.identity;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.util.ClasspathTemplateResource;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

public class AuthenticationCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        InputStream inputStream = IOUtils.toInputStream(jsonString);
        when(httpEntity.getContent()).thenReturn(inputStream);
    }

    @Test
    public void unable() {

    }

    @Test
    public void authenticateSuccessful() throws IOException {
        Access access = new AuthenticationCommand(httpClient, "sometenant", "someurl", "user", "pwd").call();
        assertEquals("a376b74fbdb64a4986cd3234647ff6f8", access.getToken());
    }

    @Test
    public void authenticateFail() throws IOException {
        checkForError(401, new AuthenticationCommand(httpClient, "sometenant", "someurl", "user", "pwd"), CommandExceptionError.UNAUTHORIZED);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new AuthenticationCommand(httpClient, "sometenant", "someurl", "user", "pwd"), CommandExceptionError.UNKNOWN);
    }
}
