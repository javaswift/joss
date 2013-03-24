package org.javaswift.joss.command.impl.object;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.Md5ChecksumException;
import org.javaswift.joss.exception.MissingContentLengthOrTypeException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.headers.Token;
import org.javaswift.joss.headers.object.*;
import org.javaswift.joss.instructions.UploadInstructions;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.params.CoreProtocolPNames;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UploadObjectCommandImplTest extends BaseCommandTest {

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void uploadByteArray() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"),
                                new UploadInstructions(new byte[]{ })).call();
    }

    @Test(expected = CommandException.class)
    public void failReadingTheEntity() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        byte[] bytes = new byte[]{ 0x01, 0x02, 0x03 };
        UploadInstructions instructions = spy(new UploadInstructions(bytes));
        when(instructions.getEtag()).thenThrow(new IOException());
        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), instructions)
                .call();
    }

    @Test
    public void uploadInputStream() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{ 0x01, 0x02, 0x03 });

        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"),
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

        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"),
                                new UploadInstructions(inputStream)).call();
    }

    @Test
    public void supplyHeaders() throws IOException, DateParseException {
        when(statusLine.getStatusCode()).thenReturn(201);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{ 0x01, 0x02, 0x03 });
        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"),
                                new UploadInstructions(inputStream).setMd5("ebabefac")
                                        .setContentType("image/bmp")
                                        .setDeleteAt(new DeleteAt("Sat, 22 Sep 2012 07:24:21 GMT"))
                                        .setDeleteAfter(new DeleteAfter(42))
                                        .setObjectManifest(new ObjectManifest(getObject("some-big-file.dat").getPath()))).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("image/bmp", requestArgument.getValue().getFirstHeader(ObjectContentType.CONTENT_TYPE).getValue());
        assertEquals("cafebabe", requestArgument.getValue().getFirstHeader(Token.X_AUTH_TOKEN).getValue());
        assertEquals("ebabefac", requestArgument.getValue().getFirstHeader(Etag.ETAG).getValue());
        assertEquals("1348298661", requestArgument.getValue().getFirstHeader(DeleteAt.X_DELETE_AT).getValue());
        assertEquals("42", requestArgument.getValue().getFirstHeader(DeleteAfter.X_DELETE_AFTER).getValue());
        assertEquals("container/some-big-file.dat", requestArgument.getValue().getFirstHeader(ObjectManifest.X_OBJECT_MANIFEST).getValue());
        inputStream.close();
    }

    @Test(expected = MissingContentLengthOrTypeException.class)
    public void noContentTypeFoundError() throws IOException {
        checkForError(411, new UploadObjectCommandImpl(this.account, httpClient, defaultAccess,
                getObject("objectname"), new UploadInstructions(new byte[]{})));
    }

    @Test(expected = Md5ChecksumException.class)
    public void md5checksumError() throws IOException {
        checkForError(422, new UploadObjectCommandImpl(this.account, httpClient, defaultAccess,
                getObject("objectname"), new UploadInstructions(new byte[]{})));
    }

    @Test(expected = NotFoundException.class)
    public void containerNotFound() throws IOException {
        checkForError(404, new UploadObjectCommandImpl(this.account, httpClient, defaultAccess,
                getObject("objectname"), new UploadInstructions(new byte[]{})));
    }

    @Test(expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new UploadObjectCommandImpl(this.account, httpClient, defaultAccess,
                getObject("objectname"), new UploadInstructions(new byte[]{})));
    }
}
