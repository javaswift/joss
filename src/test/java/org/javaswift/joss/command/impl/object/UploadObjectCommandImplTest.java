package org.javaswift.joss.command.impl.object;

import mockit.Expectations;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.params.CoreProtocolPNames;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.Md5ChecksumException;
import org.javaswift.joss.exception.MissingContentLengthOrTypeException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.headers.Token;
import org.javaswift.joss.headers.object.*;
import org.javaswift.joss.instructions.UploadInstructions;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertTrue;

public class UploadObjectCommandImplTest extends BaseCommandTest {

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void uploadByteArray() throws IOException {
        expectStatusCode(201);
        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"),
                                new UploadInstructions(new byte[]{ })).call();
    }

    @Test(expected = CommandException.class)
    public void failReadingTheEntity() throws IOException {
        byte[] bytes = new byte[]{ 0x01, 0x02, 0x03 };
        final UploadInstructions instructions = new UploadInstructions(bytes);
        new NonStrictExpectations(instructions) {{
            instructions.getEtag();
            result = new IOException();
        }};
        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), instructions)
                .call();
    }

    @Test
    public void uploadInputStream() throws IOException {
        expectStatusCode(201);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{ 0x01, 0x02, 0x03 });

        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"),
                                new UploadInstructions(inputStream)).call();
        verifyHeaderValue("cafebabe", Token.X_AUTH_TOKEN);
        // USE_EXPECT_CONTINUE is essential for uploading, since the Object Store requires it
        new Verifications() {{
            httpClient.execute((HttpRequestBase)any);
            forEachInvocation = new Object() {
                public void validate(HttpRequestBase request) {
                    assertTrue(Boolean.valueOf(request.getParams().getParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE).toString()));
                }
            };
        }};
        inputStream.close();
    }

    @Test(expected = CommandException.class)
    public void uploadInputStreamException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{ 0x01, 0x02, 0x03 });
        new Expectations() {{
            httpClient.execute((HttpRequestBase)any);
            result = new IOException("Oops");
        }};
        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"),
                                new UploadInstructions(inputStream)).call();
    }

    @Test
    public void supplyHeaders() throws IOException, DateParseException {
        expectStatusCode(201);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{ 0x01, 0x02, 0x03 });
        new UploadObjectCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"),
                                new UploadInstructions(inputStream).setMd5("ebabefac")
                                        .setContentType("image/bmp")
                                        .setDeleteAt(new DeleteAt("Sat, 22 Sep 2012 07:24:21 GMT"))
                                        .setDeleteAfter(new DeleteAfter(42))
                                        .setObjectManifest(new ObjectManifest(getObject("some-big-file.dat").getPath().replaceFirst("/","")))).call();
        verifyHeaderValue("image/bmp", ObjectContentType.CONTENT_TYPE);
        verifyHeaderValue("cafebabe", Token.X_AUTH_TOKEN);
        verifyHeaderValue("ebabefac", Etag.ETAG);
        verifyHeaderValue("1348298661", DeleteAt.X_DELETE_AT);
        verifyHeaderValue("42", DeleteAfter.X_DELETE_AFTER);
        verifyHeaderValue("container/some-big-file.dat", ObjectManifest.X_OBJECT_MANIFEST);
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
