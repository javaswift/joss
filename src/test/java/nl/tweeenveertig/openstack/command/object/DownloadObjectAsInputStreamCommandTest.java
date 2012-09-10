package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoreObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static nl.tweeenveertig.openstack.command.object.DownloadObjectAsByteArrayCommand.CONTENT_LENGTH;
import static nl.tweeenveertig.openstack.command.object.DownloadObjectAsByteArrayCommand.ETAG;
import static org.mockito.Mockito.when;

public class DownloadObjectAsInputStreamCommandTest extends BaseCommandTest {

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
        InputStreamWrapper result = new DownloadObjectAsInputStreamCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname")).execute();
        byte[] downloaded = IOUtils.toByteArray(result.getInputStream());
        result.closeStream();
        assertEquals(bytes.length, downloaded.length);
    }

    @Test
    public void md5Mismatch() throws IOException {
        prepareBytes(new byte[] { 0x01}, "cafebabe"); // non-matching MD5
        try {
            new DownloadObjectAsInputStreamCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname"));
        } catch (CommandException err) {
            fail("Downloading as an inputstream does not check for the MD5 checksum, so therefore should not throw an error");
        }
    }
}
