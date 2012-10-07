package nl.tweeenveertig.openstack.command.core;

import nl.tweeenveertig.openstack.client.impl.AccountImpl;
import nl.tweeenveertig.openstack.command.container.CreateContainerCommand;
import nl.tweeenveertig.openstack.command.identity.AuthenticationCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.headers.Token;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AbstractSecureCommandTest extends BaseCommandTest {

    @Mock
    AuthenticationCommand authCommand;

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void checkAuthenticationToken() {
        when(statusLine.getStatusCode()).thenReturn(201);
        CreateContainerCommand command = new CreateContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"));
        Header[] xAuth = command.request.getHeaders(Token.X_AUTH_TOKEN);
        assertEquals(1, xAuth.length);
        assertEquals("cafebabe", xAuth[0].getValue());
    }

    @Test
    public void reauthenticateSuccess() {
        when(statusLine.getStatusCode()).thenReturn(401).thenReturn(201);
        when(authCommand.call()).thenReturn(new Access());
        this.account = new AccountImpl(authCommand, httpClient, defaultAccess);
        new CreateContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
    }

    @Test
    public void reauthenticateFailTwice() {
        when(statusLine.getStatusCode()).thenReturn(401);
        when(authCommand.call()).thenReturn(new Access());
        this.account = new AccountImpl(authCommand, httpClient, defaultAccess);
        try {
            new CreateContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
        } catch(CommandException err) {
            assertEquals(CommandExceptionError.UNAUTHORIZED, err.getError());
        }
    }
}
