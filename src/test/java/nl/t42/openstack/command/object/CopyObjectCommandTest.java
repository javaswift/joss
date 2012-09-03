package nl.t42.openstack.command.object;

import nl.t42.openstack.command.core.BaseCommandTest;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class CopyObjectCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void deleteContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        new CopyObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectName"),
                new Container("containerName"), new StoreObject("objectName")).execute();
    }

    @Test
    public void deleteContainerDoesNotExist() throws IOException {
        checkForError(404, new CopyObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectName"),
                new Container("containerName"), new StoreObject("objectName")), CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new CopyObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectName"),
                new Container("containerName"), new StoreObject("objectName")), CommandExceptionError.UNKNOWN);
    }
}
