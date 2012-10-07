package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.ModifiedException;
import nl.tweeenveertig.openstack.command.core.NotModifiedException;
import nl.tweeenveertig.openstack.headers.object.conditional.IfModifiedSince;
import nl.tweeenveertig.openstack.headers.object.conditional.IfNoneMatch;
import nl.tweeenveertig.openstack.headers.object.range.FirstPartRange;
import nl.tweeenveertig.openstack.model.DownloadInstructions;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.cookie.DateParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static nl.tweeenveertig.openstack.command.object.DownloadObjectAsByteArrayCommand.CONTENT_LENGTH;
import static nl.tweeenveertig.openstack.command.object.DownloadObjectAsByteArrayCommand.ETAG;
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
                this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
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
                        this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"),
                        new DownloadInstructions().setRange(new FirstPartRange(3))));
        byte[] result = command.call();
        assertEquals(bytes.length, result.length);
        verify(command, never()).getMd5();
    }

    @Test
    public void downloadSuccess() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        byte[] result = new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new DownloadInstructions()).call();
        assertEquals(bytes.length, result.length);
    }

    @Test
    public void md5Mismatch() throws IOException {
        prepareBytes(new byte[] { 0x01}, "cafebabe"); // non-matching MD5
        checkForError(200, new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new DownloadInstructions()), CommandExceptionError.MD5_CHECKSUM);
    }

    @Test
    public void objectNotFound() throws IOException {
        checkForError(404, new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new DownloadInstructions()), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new DownloadInstructions()), CommandExceptionError.UNKNOWN);
    }

    @Test(expected = NotModifiedException.class)
    public void contentNotModified() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(304);
        new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new DownloadInstructions()).call();
    }

    @Test(expected = ModifiedException.class)
    public void contentModified() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(412);
        new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectname"), new DownloadInstructions()).call();
    }

    @Test
    public void isSecure() throws IOException {
        prepareBytes(new byte[] { 0x01, 0x02, 0x03}, null);
        isSecure(new DownloadObjectAsByteArrayCommand(this.account, httpClient, defaultAccess,
                account.getContainer("containerName"), getObject("objectname"), new DownloadInstructions()));
    }
}
