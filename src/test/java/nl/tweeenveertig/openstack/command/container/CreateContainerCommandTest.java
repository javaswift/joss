package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

public class CreateContainerCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        new CreateContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(202, new CreateContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")), CommandExceptionError.CONTAINER_ALREADY_EXISTS);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new CreateContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")), CommandExceptionError.UNKNOWN);
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new CreateContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")), 201);
    }
}
