package org.javaswift.joss.command.impl.identity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;

import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.entity.StringEntity;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.UnauthorizedException;
import org.javaswift.joss.model.Access;
import org.junit.Before;
import org.junit.Test;

public class KeystoneAuthenticationCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        loadSampleJson("/sample-access.json");
    }

    @Test
    public void getUrl() {
        AuthenticationCommand command = new KeystoneAuthenticationCommandImpl(httpClient, "someurl", null, null, "user", "pwd");
        assertEquals("someurl", command.getUrl());
    }

    @Test
    public void noTenantSupplied() throws IOException {
        AuthenticationCommand command = new KeystoneAuthenticationCommandImpl(httpClient, "someurl", null, null, "user", "pwd");
        Access access = command.call();
        assertFalse(access.isTenantSupplied());
    }

    @Test
    public void emptyTenantNameSupplied() {
        AuthenticationCommand command = new KeystoneAuthenticationCommandImpl(httpClient, "someurl", "", null, "user", "pwd");
        Access access = command.call();
        assertFalse(access.isTenantSupplied());
    }

    @Test
    public void emptyTenantIdSupplied() {
        AuthenticationCommand command = new KeystoneAuthenticationCommandImpl(httpClient, "someurl", null, "", "user", "pwd");
        Access access = command.call();
        assertFalse(access.isTenantSupplied());
    }

    @Test
    public void noTenantNameSupplied() throws IOException {
        AuthenticationCommand command = new KeystoneAuthenticationCommandImpl(httpClient, "someurl", null, "tenantid", "user", "pwd");
        Access access = command.call();
        assertTrue(access.isTenantSupplied());
    }

    @Test
    public void authenticateSuccessful() throws IOException {
        Access access = new KeystoneAuthenticationCommandImpl(httpClient, "someurl", "sometenant", "tenantid", "user", "pwd").call();
        assertEquals("a376b74fbdb64a4986cd3234647ff6f8", access.getToken());
    }

    @Test (expected = UnauthorizedException.class)
    public void authenticateFail() throws IOException {
        checkForError(401, new KeystoneAuthenticationCommandImpl(httpClient, "someurl", "sometenant", "tenantid", "user", "pwd"));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new KeystoneAuthenticationCommandImpl(httpClient, "someurl", "sometenant", "tenantid", "user", "pwd"));
    }

    @Test(expected = CommandException.class)
    public void ioException(@Mocked(stubOutClassInitialization = true) StringEntity unused) throws Exception {
        new Expectations() {{
            new StringEntity(anyString);
            result = new IOException();
        }};
        new KeystoneAuthenticationCommandImpl(httpClient, "someurl", "sometenant", "tenantid", "user", "pwd");
    }
}
