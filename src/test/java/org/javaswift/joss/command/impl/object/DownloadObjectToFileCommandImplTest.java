package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.javaswift.joss.headers.object.Etag.ETAG;
import static org.javaswift.joss.headers.object.ObjectContentLength.CONTENT_LENGTH;
import static org.javaswift.joss.headers.object.ObjectContentType.CONTENT_TYPE;
import static org.javaswift.joss.headers.object.ObjectLastModified.LAST_MODIFIED;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DownloadObjectToFileCommandImpl.class, IOUtils.class, DigestUtils.class })
public class DownloadObjectToFileCommandImplTest extends BaseCommandTest {

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
        new DownloadObjectToFileCommandImpl(this.account, httpClient, defaultAccess, getObject("objectname"), new DownloadInstructions(), downloadedFile).call();
        assertTrue(downloadedFile.exists());
    }

    @Test
    public void closeOutputStreamNull() throws Exception {
        closeOutputStream(null);
    }

    @Test
    public void closeOutputStream() throws Exception {
        final FileOutputStream mockedOutputStream = mock(FileOutputStream.class);
        closeOutputStream(mockedOutputStream);
        verify(mockedOutputStream).close();
    }

    @Test
    public void closeOutputStreamThrowsException() throws Exception {
        final FileOutputStream mockedOutputStream = mock(FileOutputStream.class);
        doThrow(new IOException()).when(mockedOutputStream).close();
        closeOutputStream(mockedOutputStream);
        verify(mockedOutputStream).close();
    }

    protected void closeOutputStream(FileOutputStream outputStream) throws Exception {
        prepareMetadata();
        whenNew(FileOutputStream.class).withArguments(downloadedFile).thenReturn(outputStream);
        PowerMockito.mockStatic(IOUtils.class);
        Mockito.when(IOUtils.copy(any(InputStream.class), any(OutputStream.class))).thenThrow(new NullPointerException());
        try {
            new DownloadObjectToFileCommandImpl(this.account, httpClient, defaultAccess,
                    getObject("objectname"), new DownloadInstructions(), downloadedFile).call();
        } catch (NullPointerException err) {
            if (outputStream != null) {
                verify(outputStream).close();
            }
        }
    }

    @Test
    public void closeInputStreamNull() throws Exception {
        closeInputStream(null);
    }

    @Test
    public void closeInputStream() throws Exception {
        final FileInputStream mockedInputStream = mock(FileInputStream.class);
        closeInputStream(mockedInputStream);
        verify(mockedInputStream).close();
    }

    @Test
    public void closeInputStreamThrowsException() throws Exception {
        final FileInputStream mockedInputStream = mock(FileInputStream.class);
        doThrow(new IOException()).when(mockedInputStream).close();
        closeInputStream(mockedInputStream);
        verify(mockedInputStream).close();
    }

    protected void closeInputStream(FileInputStream inputStream) throws Exception {
        prepareMetadata();
        whenNew(FileInputStream.class).withArguments(downloadedFile).thenReturn(inputStream);
        PowerMockito.mockStatic(DigestUtils.class);
        Mockito.when(DigestUtils.md5Hex(any(InputStream.class))).thenThrow(new NullPointerException());
        try {
            new DownloadObjectToFileCommandImpl(this.account, httpClient, defaultAccess,
                    getObject("objectname"), new DownloadInstructions(), downloadedFile).call();
        } catch (NullPointerException err) {
            if (inputStream != null) {
                verify(inputStream).close();
            }
        }
    }

}
