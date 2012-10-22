package nl.tweeenveertig.openstack.command.identity;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandUtil;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.command.identity.authentication.Authentication;
import nl.tweeenveertig.openstack.command.object.DownloadObjectToFileCommand;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.exception.UnauthorizedException;
import nl.tweeenveertig.openstack.util.ClasspathTemplateResource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static junit.framework.Assert.*;
import static nl.tweeenveertig.openstack.command.core.CommandUtil.createObjectMapper;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AuthenticationCommand.class })
public class AuthenticationCommandTest extends BaseCommandTest {

    private String jsonString;

    @Before
    public void setup() throws IOException {
        super.setup();
        jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        InputStream inputStream = IOUtils.toInputStream(jsonString);
        when(httpEntity.getContent()).thenReturn(inputStream);
    }

    @Test
    public void authenticateSuccessful() throws IOException {
        Access access = new AuthenticationCommand(httpClient, "someurl", "sometenant", "user", "pwd").call();
        assertEquals("a376b74fbdb64a4986cd3234647ff6f8", access.getToken());
    }

    @Test (expected = UnauthorizedException.class)
    public void authenticateFail() throws IOException {
        checkForError(401, new AuthenticationCommand(httpClient, "someurl", "sometenant", "user", "pwd"));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new AuthenticationCommand(httpClient, "someurl", "sometenant", "user", "pwd"));
    }

    @Test(expected = CommandException.class)
    public void ioException() throws Exception {
        whenNew(StringEntity.class).withArguments(anyString()).thenThrow(new IOException());
        new AuthenticationCommand(httpClient, "someurl", "sometenant", "user", "pwd");
    }
}
