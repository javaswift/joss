package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.model.UploadInstructions;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.when;

public class UploadObjectCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void uploadByteArray() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new UploadInstructions(new byte[] {})).call();
    }

    @Test
    public void uploadInputStream() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new UploadInstructions(inputStream)).call();
        inputStream.close();
    }

    @Test
    public void noContentTypeFoundError() throws IOException {
        checkForError(411, new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new UploadInstructions(new byte[] {})), CommandExceptionError.MISSING_CONTENT_LENGTH_OR_TYPE);
    }

    @Test
    public void md5checksumError() throws IOException {
        checkForError(422, new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new UploadInstructions(new byte[] {})), CommandExceptionError.MD5_CHECKSUM);
    }

    @Test
    public void containerNotFound() throws IOException {
        checkForError(404, new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new UploadInstructions(new byte[] {})), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new UploadInstructions(new byte[] {})), CommandExceptionError.UNKNOWN);
    }
}
