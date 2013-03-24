package org.javaswift.joss.command.impl.identity;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.UnauthorizedException;
import org.javaswift.joss.util.ClasspathTemplateResource;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AuthenticationCommandImpl.class })
public class AuthenticationCommandImplTest extends BaseCommandTest {

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
        AccessImpl access = new AuthenticationCommandImpl(httpClient, "someurl", "sometenant", "user", "pwd").call();
        assertEquals("a376b74fbdb64a4986cd3234647ff6f8", access.getToken());
    }

    @Test (expected = UnauthorizedException.class)
    public void authenticateFail() throws IOException {
        checkForError(401, new AuthenticationCommandImpl(httpClient, "someurl", "sometenant", "user", "pwd"));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new AuthenticationCommandImpl(httpClient, "someurl", "sometenant", "user", "pwd"));
    }

    @Test(expected = CommandException.class)
    public void ioException() throws Exception {
        whenNew(StringEntity.class).withArguments(anyString()).thenThrow(new IOException());
        new AuthenticationCommandImpl(httpClient, "someurl", "sometenant", "user", "pwd");
    }
}
