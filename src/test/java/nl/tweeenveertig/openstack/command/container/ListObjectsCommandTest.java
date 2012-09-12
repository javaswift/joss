package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class ListObjectsCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void listObjects() throws IOException {
        new ListObjectsCommand(httpClient, defaultAccess, account.getContainer("containername")).call();
    }

    @Test
    public void listObjectsWithNoneThere() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        new ListObjectsCommand(httpClient, defaultAccess, account.getContainer("containername")).call();
    }

    @Test
    public void containerDoesNotExist() throws IOException {
        checkForError(404, new ListObjectsCommand(httpClient, defaultAccess, account.getContainer("containername")), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ListObjectsCommand(httpClient, defaultAccess, account.getContainer("containername")), CommandExceptionError.UNKNOWN);
    }
}
