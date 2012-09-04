package nl.t42.openstack.command.object;

import nl.t42.openstack.command.core.BaseCommandTest;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class UploadObjectCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        new UploadObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname"), new byte[] {}).execute();
    }

    @Test
    public void noContentTypeFoundError() throws IOException {
        checkForError(411, new UploadObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname"), new byte[] {}), CommandExceptionError.MISSING_CONTENT_LENGTH_OR_TYPE);
    }

    @Test
    public void md5checksumError() throws IOException {
        checkForError(422, new UploadObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname"), new byte[] {}), CommandExceptionError.MD5_CHECKSUM);
    }

    @Test
    public void containerNotFound() throws IOException {
        checkForError(404, new UploadObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname"), new byte[] {}), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new UploadObjectCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname"), new byte[] {}), CommandExceptionError.UNKNOWN);
    }
}
