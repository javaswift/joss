package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.container.ContainerRights;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContainerRightsCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(202);
        new ContainerRightsCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals(ContainerRights.PUBLIC_CONTAINER, requestArgument.getValue().getFirstHeader(ContainerRights.X_CONTAINER_READ).getValue());
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerRightsCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ContainerRightsCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true), CommandExceptionError.UNKNOWN);
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ContainerRightsCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true), 202);
    }

}
