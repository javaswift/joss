package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoreObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static nl.tweeenveertig.openstack.command.object.DownloadObjectAsByteArrayCommand.CONTENT_LENGTH;
import static nl.tweeenveertig.openstack.command.object.DownloadObjectAsByteArrayCommand.ETAG;
import static org.mockito.Mockito.when;

public class DownloadObjectCommandTest extends BaseCommandTest {

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
        byte[] result = new DownloadObjectAsByteArrayCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname")).call();
        assertEquals(bytes.length, result.length);
    }

    @Test
    public void md5Mismatch() throws IOException {
        prepareBytes(new byte[] { 0x01}, "cafebabe"); // non-matching MD5
        checkForError(200, new DownloadObjectAsByteArrayCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname")), CommandExceptionError.MD5_CHECKSUM);
    }

    @Test
    public void objectNotFound() throws IOException {
        checkForError(404, new DownloadObjectAsByteArrayCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname")), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new DownloadObjectAsByteArrayCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectname")), CommandExceptionError.UNKNOWN);
    }
}
