package org.javaswift.joss.command.impl.object;

import mockit.Expectations;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.cookie.DateParseException;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.*;
import org.javaswift.joss.headers.object.conditional.IfModifiedSince;
import org.javaswift.joss.headers.object.conditional.IfNoneMatch;
import org.javaswift.joss.headers.object.range.FirstPartRange;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.javaswift.joss.command.impl.object.DownloadObjectAsByteArrayCommandImpl.CONTENT_LENGTH;
import static org.javaswift.joss.command.impl.object.DownloadObjectAsByteArrayCommandImpl.ETAG;
import static org.javaswift.joss.headers.object.ObjectManifest.X_OBJECT_MANIFEST;

public class DownloadObjectAsByteArrayCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    protected void prepareBytes(byte[] bytes, String md5) {
        final List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, ETAG, md5 == null ? DigestUtils.md5Hex(bytes) : md5);
        prepareHeader(response, CONTENT_LENGTH, Long.toString(bytes.length));
        prepareHeadersForRetrieval(response, headers);
        setHttpEntity(new ByteArrayEntity(bytes));
    }

    @Test
    public void variousDownloadInstructions() throws IOException, DateParseException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        expectStatusCode(200, false);
        new DownloadObjectAsByteArrayCommandImpl(
                this.account, httpClient, defaultAccess, getObject("objectname"),
                new DownloadInstructions()
                        .setRange(new FirstPartRange(3))
                        .setMatchConditional(new IfNoneMatch("cafebabe"))
                        .setSinceConditional(new IfModifiedSince("Tue, 02 Oct 2012 17:34:17 GMT"))).call();
        verifyHeaderValue("bytes=0-3", "Range");
        verifyHeaderValue("cafebabe", IfNoneMatch.IF_NONE_MATCH);
        verifyHeaderValue("Tue, 02 Oct 2012 17:34:17 GMT", IfModifiedSince.IF_MODIFIED_SINCE);
    }

    @Test
    public void assertPartialContentDoesNotTriggerAnMd5Check() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        expectStatusCode(206, false);
        final DownloadObjectAsByteArrayCommandImpl command =
                new DownloadObjectAsByteArrayCommandImpl(
                        this.account, httpClient, defaultAccess, getObject("objectname"),
                        new DownloadInstructions().setRange(new FirstPartRange(3)));
        new Expectations(command) {{
            command.getMd5();
            times = 0;
        }};
        byte[] result = command.call();
        assertEquals(bytes.length, result.length);

    }

    @Test
    public void nullManifestDoesNotTriggerAnMd5Check() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        prepareHeader(response, X_OBJECT_MANIFEST, Long.toString(bytes.length));
        final DownloadObjectAsByteArrayCommandImpl command =
                new DownloadObjectAsByteArrayCommandImpl(
                        this.account, httpClient, defaultAccess, getObject("objectname"),
                        new DownloadInstructions().setRange(new FirstPartRange(3)));
        new Expectations(command) {{
            command.getMd5();
            times = 0;
        }};
        byte[] result = command.call();
        assertEquals(bytes.length, result.length);
    }

    @Test
    public void downloadSuccess() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        byte[] result = new DownloadObjectAsByteArrayCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()).call();
        assertEquals(bytes.length, result.length);
    }

    @Test (expected = Md5ChecksumException.class)
    public void md5Mismatch() throws IOException {
        prepareBytes(new byte[] { 0x01}, "cafebabe"); // non-matching MD5
        expectStatusCode(200);
        checkForError(200, new DownloadObjectAsByteArrayCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test (expected = NotFoundException.class)
    public void objectNotFound() throws IOException {
        checkForError(404, new DownloadObjectAsByteArrayCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new DownloadObjectAsByteArrayCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test(expected = NotModifiedException.class)
    public void contentNotModified() throws IOException {
        checkForError(304, new DownloadObjectAsByteArrayCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test(expected = ModifiedException.class)
    public void contentModified() throws IOException {
        checkForError(412, new DownloadObjectAsByteArrayCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()));
    }

    @Test
    public void isSecure() throws IOException {
        prepareBytes(new byte[] { 0x01, 0x02, 0x03}, null);
        isSecure(new DownloadObjectAsByteArrayCommandImpl(this.account, httpClient, defaultAccess,
                getObject("objectname"), new DownloadInstructions()));
    }
}
