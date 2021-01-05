package org.javaswift.joss.command.impl.core;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.apache.http.Header;
import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;
import org.javaswift.joss.client.impl.AccountImpl;
import org.javaswift.joss.command.impl.container.CreateContainerCommandImpl;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.command.shared.identity.access.AbstractAccess;
import org.javaswift.joss.command.shared.identity.access.AccessTenant;
import org.javaswift.joss.exception.UnauthorizedException;
import org.javaswift.joss.headers.ConnectionKeepAlive;
import org.javaswift.joss.headers.Token;
import org.junit.Before;
import org.junit.Test;

public class AbstractSecureCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void checkConnectionKeepAlive() {
        CreateContainerCommandImpl command =
                new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"));
        Header[] keepAlive = command.request.getHeaders(ConnectionKeepAlive.CONNECTION);
        assertEquals(1, keepAlive.length);
        assertEquals("Keep-Alive", keepAlive[0].getValue());
    }

    @Test
    public void checkAuthenticationToken() {
        CreateContainerCommandImpl command =
            new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"));
        Header[] xAuth = command.request.getHeaders(Token.X_AUTH_TOKEN);
        assertEquals(1, xAuth.length);
        assertEquals("cafebabe", xAuth[0].getValue());
    }

    @Test
    public void reauthenticateSuccess(@Mocked final AuthenticationCommand authCommand) {
        new NonStrictExpectations() {{
            statusLine.getStatusCode(); returns(401, 201);
            authCommand.call(); result = new AccessTenant();
        }};
        this.account = new AccountImpl(authCommand, httpClient, defaultAccess, true, TempUrlHashPrefixSource.PUBLIC_URL_PATH, '/');
        new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
    }

    @Test
    public void reauthenticateWithPreferredRegionSuccess(@Mocked final AuthenticationCommand authCommand) {
        new NonStrictExpectations() {{
            statusLine.getStatusCode(); returns(401, 201);
            authCommand.call(); result = new AccessTenant();
        }};
        this.account = new AccountImpl(authCommand, httpClient, defaultAccess, true, TempUrlHashPrefixSource.PUBLIC_URL_PATH, '/');
        final String preferredRegion = "testPreferredRegion";
        this.account.setPreferredRegion(preferredRegion);
        new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
        assertEquals(preferredRegion, ((AbstractAccess)this.account.getAccess()).getPreferredRegion());
    }

    @Test(expected = UnauthorizedException.class)
    public void reauthenticateNotAllowed() {
        expectStatusCode(401);
        this.account.setAllowReauthenticate(false);
        new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
    }

    @Test(expected = UnauthorizedException.class)
    public void reauthenticateFailTwice(@Mocked final AuthenticationCommand authCommand) {
        expectStatusCode(401, false);
        new Expectations() {{
            authCommand.call(); result = new AccessTenant();
        }};
        this.account = new AccountImpl(authCommand, httpClient, defaultAccess, true, TempUrlHashPrefixSource.PUBLIC_URL_PATH, '/');
        new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
    }
}
