package org.javaswift.joss.command.impl.identity;

import mockit.Expectations;
import mockit.Mocked;
import org.apache.http.entity.StringEntity;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class AuthenticationCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        loadSampleJson("/sample-access.json");
    }

    @Test
    public void authenticateSuccessful() throws IOException {
        AccessImpl access = new AuthenticationCommandImpl(httpClient, "someurl", "sometenant", "tenantid", "user", "pwd").call();
        assertEquals("a376b74fbdb64a4986cd3234647ff6f8", access.getToken());
    }

    @Test (expected = UnauthorizedException.class)
    public void authenticateFail() throws IOException {
        checkForError(401, new AuthenticationCommandImpl(httpClient, "someurl", "sometenant", "tenantid", "user", "pwd"));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new AuthenticationCommandImpl(httpClient, "someurl", "sometenant", "tenantid", "user", "pwd"));
    }

    @Test(expected = CommandException.class)
    public void ioException(@Mocked(stubOutClassInitialization = true) StringEntity unused) throws Exception {
        new Expectations() {{
            new StringEntity(anyString);
            result = new IOException();
        }};
        new AuthenticationCommandImpl(httpClient, "someurl", "sometenant", "tenantid", "user", "pwd");
    }
}
