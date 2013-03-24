package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.javaswift.joss.command.impl.object.DownloadObjectAsByteArrayCommandImpl.CONTENT_LENGTH;
import static org.javaswift.joss.command.impl.object.DownloadObjectAsByteArrayCommandImpl.ETAG;
import static org.mockito.Mockito.when;

public class DownloadObjectAsInputStreamCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    protected void prepareBytes(byte[] bytes, String md5) {
        when(statusLine.getStatusCode()).thenReturn(200);
        prepareHeader(response, ETAG, md5 == null ? DigestUtils.md5Hex(bytes) : md5);
        prepareHeader(response, CONTENT_LENGTH, "3");
        httpEntity = new ByteArrayEntity(bytes);
        when(response.getEntity()).thenReturn(httpEntity);
    }

    @Test
    public void downloadSuccess() throws IOException {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
        InputStream result = new DownloadObjectAsInputStreamCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions()).call();
        byte[] downloaded = IOUtils.toByteArray(result);
        result.close();
        assertEquals(bytes.length, downloaded.length);
    }

    @Test
    public void md5Mismatch() throws IOException {
        prepareBytes(new byte[] { 0x01}, "cafebabe"); // non-matching MD5
        try {
            new DownloadObjectAsInputStreamCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions());
        } catch (CommandException err) {
            fail("Downloading as an inputstream does not check for the MD5 checksum, so therefore should not throw an error");
        }
    }

    @Test
    public void isSecure() throws IOException {
        prepareBytes(new byte[] { 0x01, 0x02, 0x03}, null);
        isSecure(new DownloadObjectAsInputStreamCommandImpl(this.account, httpClient, defaultAccess,
                getObject("objectname"), new DownloadInstructions()));
    }
}
