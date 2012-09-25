package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.model.DownloadInstructions;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static nl.tweeenveertig.openstack.headers.object.Etag.ETAG;
import static nl.tweeenveertig.openstack.headers.object.ObjectContentLength.CONTENT_LENGTH;
import static nl.tweeenveertig.openstack.headers.object.ObjectContentType.CONTENT_TYPE;
import static nl.tweeenveertig.openstack.headers.object.ObjectLastModified.LAST_MODIFIED;
import static org.mockito.Mockito.when;

public class DownloadObjectToFileCommandTest extends BaseCommandTest {

    private File downloadedFile = new File("download.tmp");

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @After
    public void teardown() {
        downloadedFile.delete();
    }

    private void prepareMetadata() throws IOException {
        InputStream inputStream = IOUtils.toInputStream("SOMEFILE");
        when(httpEntity.getContent()).thenReturn(inputStream);
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, LAST_MODIFIED, "Mon, 03 Sep 2012 05:40:33 GMT");
        prepareHeader(response, ETAG, "e90629d47725dc3ad29e889d57e46106", headers);
        prepareHeader(response, CONTENT_LENGTH, "654321", headers);
        prepareHeader(response, CONTENT_TYPE, "image/png", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
    }

    @Test
    public void testDownloadedFileExists() throws IOException {
        prepareMetadata();
        new DownloadObjectToFileCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"),
                getObject("objectname"), new DownloadInstructions(), downloadedFile).call();
        assertTrue(downloadedFile.exists());
    }

}
