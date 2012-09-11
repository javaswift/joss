package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoreObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class DeleteObjectCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void deleteContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        new DeleteObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectName")).call();
    }

    @Test
    public void deleteContainerDoesNotExist() throws IOException {
        checkForError(404, new DeleteObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectName")), CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new DeleteObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectName")), CommandExceptionError.UNKNOWN);
    }
}
