package nl.t42.openstack.command.account;

import nl.t42.openstack.command.container.ContainerInformationCommand;
import nl.t42.openstack.command.core.BaseCommandTest;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.AccountInformation;
import nl.t42.openstack.model.Container;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static nl.t42.openstack.command.account.AccountInformationCommand.*;
import static org.mockito.Mockito.when;

public class AccountInformationCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        prepareHeader(response, X_ACCOUNT_CONTAINER_COUNT, "7");
        prepareHeader(response, X_ACCOUNT_OBJECT_COUNT, "123");
        prepareHeader(response, X_ACCOUNT_BYTES_USED, "654321");
        AccountInformation info = new AccountInformationCommand(httpClient, defaultAccess).execute();
        assertEquals(7, info.getContainerCount());
        assertEquals(123, info.getObjectCount());
        assertEquals(654321, info.getBytesUsed());
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ContainerInformationCommand(httpClient, defaultAccess, new Container("containerName")), CommandExceptionError.UNKNOWN);
    }
}
