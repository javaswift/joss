package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class ContainerRightsCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(202);
        new ContainerRightsCommand(httpClient, defaultAccess, account.getContainer("containerName"), true).call();
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerRightsCommand(httpClient, defaultAccess, account.getContainer("containerName"), true), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ContainerRightsCommand(httpClient, defaultAccess, account.getContainer("containerName"), true), CommandExceptionError.UNKNOWN);
    }
}
