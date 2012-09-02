package nl.t42.openstack.command.container;

import nl.t42.openstack.command.core.BaseCommandTest;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.Container;
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
        new ListObjectsCommand(httpClient, defaultAccess, new Container("containername")).execute();
    }

    @Test
    public void listObjectsWithNoneThere() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        new ListObjectsCommand(httpClient, defaultAccess, new Container("containername")).execute();
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ListObjectsCommand(httpClient, defaultAccess, new Container("containername")), CommandExceptionError.UNKNOWN);
    }
}
