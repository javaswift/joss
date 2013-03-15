package org.javaswift.joss.command.object;

import org.javaswift.joss.exception.*;
import org.javaswift.joss.headers.object.conditional.IfModifiedSince;
import org.javaswift.joss.headers.object.conditional.IfNoneMatch;
import org.javaswift.joss.headers.object.range.FirstPartRange;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.command.core.BaseCommandTest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.cookie.DateParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.javaswift.joss.command.object.DownloadObjectAsByteArrayCommand.CONTENT_LENGTH;
import static org.javaswift.joss.command.object.DownloadObjectAsByteArrayCommand.ETAG;
import static org.javaswift.joss.headers.object.ObjectManifest.X_OBJECT_MANIFEST;
import static org.mockito.Mockito.*;

public class DownloadObjectAsByteArrayCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    protected void prepareBytes(byte[] bytes, String md5) {
        when(statusLine.getStatusCode()).thenReturn(200);
        prepareHeader(response, ETAG, md5 == null ? DigestUtils.md5Hex(bytes) : md5);
        prepareHeader(response, CONTENT_LENGTH, Long.toString(bytes.length));
        httpEntity = new ByteArrayEntity(bytes);
        when(response.getEntity()).thenReturn(httpEntity);
    }

    @Test
    public void variousDownloadInstructions() throws IOException, DateParseException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        when(statusLine.getStatusCode()).thenReturn(200);
        new DownloadObjectAsByteArrayCommand(
                this.account, httpClient, defaultAccess, getObject("objectname"),
                new DownloadInstructions()
                        .setRange(new FirstPartRange(3))
                        .setMatchConditional(new IfNoneMatch("cafebabe"))
                        .setSinceConditional(new IfModifiedSince("Tue, 02 Oct 2012 17:34:17 GMT"))).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("bytes=0-3", requestArgument.getValue().getFirstHeader("Range").getValue());
        assertEquals("cafebabe", requestArgument.getValue().getFirstHeader(IfNoneMatch.IF_NONE_MATCH).getValue());
        assertEquals("Tue, 02 Oct 2012 17:34:17 GMT", requestArgument.getValue().getFirstHeader(IfModifiedSince.IF_MODIFIED_SINCE).getValue());
    }

    @Test
    public void assertPartialContentDoesNotTriggerAnMd5Check() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        when(statusLine.getStatusCode()).thenReturn(206);
        DownloadObjectAsByteArrayCommand command =
                spy(new DownloadObjectAsByteArrayCommand(
                        this.account, httpClient, defaultAccess, getObject("objectname"),
                        new DownloadInstructions().setRange(new FirstPartRange(3))));
        byte[] result = command.call();
        assertEquals(bytes.length, result.length);
        verify(command, never()).getMd5();
    }

    @Test
    public void manifestDoesNotTriggerAnMd5Check() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        prepareHeader(response, X_OBJECT_MANIFEST, Long.toString(bytes.length));
        DownloadObjectAsByteArrayCommand command =
                spy(new DownloadObjectAsByteArrayCommand(
                        this.account, httpClient, defaultAccess, getObject("objectname"),
                        new DownloadInstructions().setRange(new FirstPartRange(3))));
        byte[] result = command.call();
        assertEquals(bytes.length, result.length);
        verify(command, never()).getMd5();
    }

    @Test
    public void downloadSuccess() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        byte[] result = new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()).call();
        assertEquals(bytes.length, result.length);
    }

    @Test (expected = Md5ChecksumException.class)
    public void md5Mismatch() throws IOException {
        prepareBytes(new byte[] { 0x01}, "cafebabe"); // non-matching MD5
        checkForError(200, new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test (expected = NotFoundException.class)
    public void objectNotFound() throws IOException {
        checkForError(404, new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test(expected = NotModifiedException.class)
    public void contentNotModified() throws IOException {
        checkForError(304, new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test(expected = ModifiedException.class)
    public void contentModified() throws IOException {
        checkForError(412, new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test
    public void isSecure() throws IOException {
        prepareBytes(new byte[] { 0x01, 0x02, 0x03}, null);
        isSecure(new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess,
                getObject("objectname"), new DownloadInstructions()));
    }
}
