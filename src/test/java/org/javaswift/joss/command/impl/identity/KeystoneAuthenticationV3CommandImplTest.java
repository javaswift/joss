package org.javaswift.joss.command.impl.identity;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import mockit.NonStrictExpectations;

import org.apache.http.Header;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.UnauthorizedException;
import org.javaswift.joss.model.Access;
import org.junit.Before;
import org.junit.Test;

public class KeystoneAuthenticationV3CommandImplTest extends BaseCommandTest {

    private AccountConfig config;

    @Before
    public void setup() throws IOException {
        super.setup();
        loadSampleJson("/sample-v3-auth-response.json");

        config = new AccountConfig();
        config.setAuthUrl("authUrl");
    }

    @Test
    public void getUrl() {
        AuthenticationCommand command = new KeystoneAuthenticationV3CommandImpl(httpClient, config);
        assertEquals(config.getAuthUrl(), command.getUrl());
    }

    @Test
    public void authenticateSuccessful() throws IOException {
        final Header header = createHeader("X-Subject-Token", "a376b74fbdb64a4986cd3234647ff6f8");

        new NonStrictExpectations() {{
            response.getFirstHeader("X-Subject-Token");
            result = header;
        }};

        Access access = new KeystoneAuthenticationV3CommandImpl(httpClient, config).call();
        assertEquals("a376b74fbdb64a4986cd3234647ff6f8", access.getToken());
    }

    @Test (expected = UnauthorizedException.class)
    public void authenticateFail() throws IOException {
        checkForError(401, new KeystoneAuthenticationV3CommandImpl(httpClient, config));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new KeystoneAuthenticationV3CommandImpl(httpClient, config));
    }

}
