package nl.t42.openstack.command;

import nl.t42.openstack.BaseCommandTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

public class DeleteContainerCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void deleteContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        new DeleteContainerCommand(httpClient, defaultAccess, "containerName").execute();
    }

    @Test
    public void deleteContainerDoesNotExist() throws IOException {
        checkForError(404, new DeleteContainerCommand(httpClient, defaultAccess, "containerName"), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void deleteContainerNotEmpty() throws IOException {
        checkForError(409, new DeleteContainerCommand(httpClient, defaultAccess, "containerName"), CommandExceptionError.CONTAINER_NOT_EMPTY);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new DeleteContainerCommand(httpClient, defaultAccess, "containerName"), CommandExceptionError.UNKNOWN);
    }
}
