package org.javaswift.joss.command.impl.core;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.apache.http.Header;
import org.javaswift.joss.client.impl.AccountImpl;
import org.javaswift.joss.command.impl.container.CreateContainerCommandImpl;
import org.javaswift.joss.command.impl.identity.AuthenticationCommandImpl;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.exception.UnauthorizedException;
import org.javaswift.joss.headers.Token;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class AbstractSecureCommandTest extends BaseCommandTest {

//    @Injectable AuthenticationCommandImpl authCommand;

    @Before
    public void setup() throws IOException {
        super.setup();
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
            authCommand.call(); result = new AccessImpl();
        }};
        this.account = new AccountImpl(authCommand, httpClient, defaultAccess, true);
        new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
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
            authCommand.call(); result = new AccessImpl();
        }};
        this.account = new AccountImpl(authCommand, httpClient, defaultAccess, true);
        new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
    }
}
