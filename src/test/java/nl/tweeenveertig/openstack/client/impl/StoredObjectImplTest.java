package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.object.AbstractDownloadObjectCommand;
import nl.tweeenveertig.openstack.headers.object.CopyFrom;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static nl.tweeenveertig.openstack.headers.object.ObjectMetadata.X_OBJECT_META_PREFIX;
import static nl.tweeenveertig.openstack.headers.object.ObjectContentLength.CONTENT_LENGTH;
import static nl.tweeenveertig.openstack.headers.object.Etag.ETAG;
import static nl.tweeenveertig.openstack.headers.object.ObjectContentType.CONTENT_TYPE;
import static nl.tweeenveertig.openstack.headers.object.ObjectLastModified.LAST_MODIFIED;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoredObjectImplTest extends BaseCommandTest {

    private StoredObject object;

    private byte[] bytes;

    private File downloadedFile = new File("download.tmp");

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        when(statusLine.getStatusCode()).thenReturn(202);
        Container container = account.getContainer("alpha");
        object = container.getObject("image.png");
        bytes = new byte[] { 0x01, 0x02, 0x03};
        prepareBytes(bytes, null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @After
    public void teardown() {
        downloadedFile.delete();
    }

    protected void prepareMetadata() {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_OBJECT_META_PREFIX+ "Year", "1989", headers);
        prepareHeader(response, X_OBJECT_META_PREFIX+ "Company", "42 BV", headers);
        prepareHeader(response, LAST_MODIFIED, "Mon, 03 Sep 2012 05:40:33 GMT");
        prepareHeader(response, ETAG, "cae4ebb15a282e98ba7b65402a72f57c", headers);
        prepareHeader(response, CONTENT_LENGTH, "654321", headers);
        prepareHeader(response, CONTENT_TYPE, "image/png", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
    }

    protected void prepareBytes(byte[] bytes, String md5) {
        when(statusLine.getStatusCode()).thenReturn(200);
        prepareHeader(response, AbstractDownloadObjectCommand.ETAG, md5 == null ? DigestUtils.md5Hex(bytes) : md5);
        prepareHeader(response, AbstractDownloadObjectCommand.CONTENT_LENGTH, Long.toString(bytes.length));
        httpEntity = new ByteArrayEntity(bytes);
        when(response.getEntity()).thenReturn(httpEntity);
    }

    @Test
    public void downloadAsByteArray() {
        byte[] result = object.downloadObject();
        assertTrue(Arrays.equals(bytes, result));
    }

    @Test
    public void downloadAsInputStream() throws IOException {
        InputStream byteStream = object.downloadObjectAsInputStream();
        assertTrue(Arrays.equals(bytes, IOUtils.toByteArray(byteStream)));
    }

    @Test
    public void downloadToFile() {
        object.downloadObject(downloadedFile);
        assertTrue(downloadedFile.exists());
    }

    @Test
    public void uploadFromByteArray() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        this.bytes = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        object.uploadObject(this.bytes);
        verifyUploadContent(this.bytes);
    }

    @Test
    public void uploadFromInputStream() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        this.bytes = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        InputStream inputStream = new ByteArrayInputStream(bytes);
        object.uploadObject(inputStream);
        verifyUploadContent(this.bytes);
    }

    @Test
    public void uploadFromFile() throws IOException {
        OutputStream outputStream = new FileOutputStream(downloadedFile);
        IOUtils.write(this.bytes, outputStream);
        outputStream.close();
        when(statusLine.getStatusCode()).thenReturn(201);
        object.uploadObject(downloadedFile);
        verifyUploadContent(this.bytes);
    }

    protected void verifyUploadContent(byte[] bytes) throws IOException {
        verify(httpClient).execute(requestArgument.capture());
        byte[] result = IOUtils.toByteArray(((HttpPut)requestArgument.getValue()).getEntity().getContent());
        assertTrue(Arrays.equals(bytes, result));
    }

    @Test
    public void setContentType() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(202);
        prepareMetadata();
        object.setContentType("image/bmp");
        verify(httpClient, times(2)).execute(requestArgument.capture());
        assertEquals("image/bmp", requestArgument.getValue().getFirstHeader(CONTENT_TYPE).getValue());
    }

    @Test
    public void deleteObject() {
        when(statusLine.getStatusCode()).thenReturn(204);
        object.delete();
    }

    @Test
    public void copyObject() throws IOException {
        Container beta = account.getContainer("beta");
        StoredObject object2 = beta.getObject("other-image.png");
        when(statusLine.getStatusCode()).thenReturn(201);
        object.copyObject(beta, object2);
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("/alpha/image.png", requestArgument.getValue().getFirstHeader(CopyFrom.X_COPY_FROM).getValue());
    }

    @Test
    public void getPublicUrl() {
        when(defaultAccess.getPublicURL()).thenReturn("http://static.resource.com");
        assertEquals("http://static.resource.com/alpha/image.png", object.getPublicURL());
    }

    @Test
    public void setMetadata() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(202);
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", "1989");
        metadata.put("Company", "42 BV");
        object.setMetadata(metadata);
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("1989", requestArgument.getValue().getFirstHeader(X_OBJECT_META_PREFIX+ "Year").getValue());
        assertEquals("42 BV", requestArgument.getValue().getFirstHeader(X_OBJECT_META_PREFIX+ "Company").getValue());
    }

    @Test
    public void getMetadata() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(202);
        prepareMetadata();
        assertEquals("1989", object.getMetadata().get("Year"));
        assertEquals("42 BV", object.getMetadata().get("Company"));
        assertEquals("Mon, 03 Sep 2012 05:40:33 GMT", object.getLastModified());
        assertEquals(654321, object.getContentLength());
        assertEquals("image/png", object.getContentType());
        assertEquals("cae4ebb15a282e98ba7b65402a72f57c", object.getEtag());
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void compareContainers() {
        Container container = account.getContainer("alpha");
        StoredObject object1 = container.getObject("img1.png");
        StoredObject object2 = container.getObject("img2.png");
        assertFalse(object1.equals("alpha"));
        assertFalse(object1.equals(object2));
        Map<StoredObject, String> containers = new TreeMap<StoredObject, String>();
        containers.put(object1, object1.getName());
        containers.put(object2, object2.getName());
        assertEquals(object1.getName(), containers.get(object1));
        assertEquals(object2.getName(), containers.get(object2));
        assertEquals(object1.getName().hashCode(), object1.hashCode());
    }

    @Test
    public void checkWhetherANonExistingFileExists() {
        when(statusLine.getStatusCode()).thenReturn(404);
        assertFalse(object.exists());
    }

    @Test(expected = CommandException.class)
    public void checkWhetherANonExistingFileExistsButThrowAnotherError() {
        when(statusLine.getStatusCode()).thenReturn(500);
        object.exists();
    }
}
