package nl.t42.openstack.command.container;

import nl.t42.openstack.command.core.BaseCommandTest;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.ContainerInformation;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

import static nl.t42.openstack.command.container.ContainerInformationCommand.*;

public class ContainerInformationCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        prepareHeader(response, X_CONTAINER_META_DESCRIPTION, "Photo album");
        prepareHeader(response, X_CONTAINER_OBJECT_COUNT, "123");
        prepareHeader(response, X_CONTAINER_BYTES_USED, "654321");
        ContainerInformation info = new ContainerInformationCommand(httpClient, defaultAccess, new Container("containerName")).execute();
        assertEquals("Photo album", info.getDescription());
        assertEquals(123, info.getObjectCount());
        assertEquals(654321, info.getBytesUsed());
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerInformationCommand(httpClient, defaultAccess, new Container("containerName")), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ContainerInformationCommand(httpClient, defaultAccess, new Container("containerName")), CommandExceptionError.UNKNOWN);
    }
}
