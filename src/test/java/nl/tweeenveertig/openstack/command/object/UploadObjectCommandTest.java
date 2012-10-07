package nl.tweeenveertig.openstack.command.object;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.Token;
import nl.tweeenveertig.openstack.headers.object.Etag;
import nl.tweeenveertig.openstack.headers.object.ObjectContentType;
import nl.tweeenveertig.openstack.model.UploadInstructions;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.CoreProtocolPNames;
import org.junit.Before;
import org.junit.Test;

public class UploadObjectCommandTest extends BaseCommandTest {

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void uploadByteArray() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
                                new UploadInstructions(new byte[]{ })).call();
    }

    @Test
    public void failReadingTheEntity() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        byte[] bytes = new byte[]{ 0x01, 0x02, 0x03 };
        final HttpEntity mockedEntity = mock(HttpEntity.class);
        when(mockedEntity.getContent()).thenThrow(new IOException("some error"));
        UploadInstructions instructions = new UploadInstructions(bytes) {
            @Override
            public HttpEntity getEntity() {

                return mockedEntity;
            }
        };
        try {
            new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), instructions)
                    .call();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
        }
    }

    @Test
    public void uploadInputStream() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{ 0x01, 0x02, 0x03 });

        new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
                                new UploadInstructions(inputStream)).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("cafebabe", requestArgument.getValue().getFirstHeader(Token.X_AUTH_TOKEN).getValue());
        // USE_EXPECT_CONTINUE is essential for uploading, since the Object Store requires it
        assertTrue(Boolean.valueOf(requestArgument.getValue().getParams().getParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE).toString()));
        inputStream.close();
    }

    @Test(expected = CommandException.class)
    public void uploadInputStreamException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{ 0x01, 0x02, 0x03 });
        when(httpClient.execute(any(HttpRequestBase.class))).thenThrow(new IOException("Oops"));

        new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
                                new UploadInstructions(inputStream)).call();
    }

    @Test
    public void supplyHeaders() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{ 0x01, 0x02, 0x03 });
        new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
                                new UploadInstructions(inputStream).setMd5("ebabefac").setContentType("image/bmp")).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("image/bmp", requestArgument.getValue().getFirstHeader(ObjectContentType.CONTENT_TYPE).getValue());
        assertEquals("cafebabe", requestArgument.getValue().getFirstHeader(Token.X_AUTH_TOKEN).getValue());
        assertEquals("ebabefac", requestArgument.getValue().getFirstHeader(Etag.ETAG).getValue());
        inputStream.close();
    }


    @Test
    public void noContentTypeFoundError() throws IOException {
        checkForError(411, new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
                                                   new UploadInstructions(new byte[]{ })), CommandExceptionError.MISSING_CONTENT_LENGTH_OR_TYPE);
    }

    @Test
    public void md5checksumError() throws IOException {
        checkForError(422, new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
                                                   new UploadInstructions(new byte[]{ })), CommandExceptionError.MD5_CHECKSUM);
    }

    @Test
    public void containerNotFound() throws IOException {
        checkForError(404, new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
                                                   new UploadInstructions(new byte[]{ })), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new UploadObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
                                                   new UploadInstructions(new byte[]{ })), CommandExceptionError.UNKNOWN);
    }
}
