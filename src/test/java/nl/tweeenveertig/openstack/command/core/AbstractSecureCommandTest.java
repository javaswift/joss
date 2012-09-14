package nl.tweeenveertig.openstack.command.core;

import nl.tweeenveertig.openstack.client.impl.AccountImpl;
import nl.tweeenveertig.openstack.command.container.CreateContainerCommand;
import nl.tweeenveertig.openstack.command.identity.AuthenticationCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
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
