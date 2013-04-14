package org.javaswift.joss.client.impl;

import mockit.Expectations;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.javaswift.joss.client.core.AbstractContainer;
import org.javaswift.joss.client.core.AbstractStoredObject;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.command.impl.object.AbstractDownloadObjectCommand;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.object.CopyFrom;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.Assert.*;
import static org.javaswift.joss.headers.object.DeleteAfter.X_DELETE_AFTER;
import static org.javaswift.joss.headers.object.DeleteAt.X_DELETE_AT;
import static org.javaswift.joss.headers.object.Etag.ETAG;
import static org.javaswift.joss.headers.object.ObjectContentLength.CONTENT_LENGTH;
import static org.javaswift.joss.headers.object.ObjectContentType.CONTENT_TYPE;
import static org.javaswift.joss.headers.object.ObjectLastModified.LAST_MODIFIED;
import static org.javaswift.joss.headers.object.ObjectMetadata.X_OBJECT_META_PREFIX;

public class StoredObjectImplTest extends BaseCommandTest {

    private StoredObject object;

    private byte[] bytes;

    private File downloadedFile = new File("download.tmp");

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
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
        prepareHeader(response, LAST_MODIFIED, "Mon, 03 Sep 2012 05:40:33 GMT", headers);
        prepareHeader(response, ETAG, "cae4ebb15a282e98ba7b65402a72f57c", headers);
        prepareHeader(response, CONTENT_LENGTH, "654321", headers);
        prepareHeader(response, CONTENT_TYPE, "image/png", headers);
        prepareHeader(response, X_DELETE_AT, "1339429105", headers);
        prepareHeadersForRetrieval(response, headers);
    }

    protected void prepareBytes(byte[] bytes, String md5) {
        expectStatusCode(200, false);
        prepareHeader(response, AbstractDownloadObjectCommand.ETAG, md5 == null ? DigestUtils.md5Hex(bytes) : md5);
        prepareHeader(response, AbstractDownloadObjectCommand.CONTENT_LENGTH, Long.toString(bytes.length));
        httpEntity = new ByteArrayEntity(bytes);
        new NonStrictExpectations() {{
            response.getEntity();
            result = httpEntity;
        }};
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
        expectStatusCode(201);
        this.bytes = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        object.uploadObject(this.bytes);
        verifyUploadContent(this.bytes);
    }

    @Test
    public void uploadFromInputStream() throws IOException {
        expectStatusCode(201);
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
        expectStatusCode(201);
        object.uploadObject(downloadedFile);
        verifyUploadContent(this.bytes);
    }

    protected void verifyUploadContent(final byte[] bytes) throws IOException {
        new Verifications() {{
            httpClient.execute((HttpRequestBase)any);
            forEachInvocation = new Object() {
                public void validate(HttpRequestBase request) throws IOException {
                    byte[] result = IOUtils.toByteArray(((HttpPut)request).getEntity().getContent());
                    assertTrue(Arrays.equals(bytes, result));
                }
            };
        }};
    }

    @Test
    public void setContentType() throws IOException {
        expectStatusCode(202, false);
        prepareMetadata();
        object.setContentType("image/bmp");
        verifyHeaderValue("image/bmp", CONTENT_TYPE, "POST");
    }

    @Test
    public void setDeleteAt() throws IOException, DateParseException {
        expectStatusCode(202, false);
        prepareMetadata();
        final Date date = DateUtils.parseDate("Mon, 03 Sep 2001 05:40:33 GMT");
        object.setDeleteAt(date);
        verifyHeaderValue(Long.toString(date.getTime() / 1000), X_DELETE_AT, "POST");
    }

    @Test
    public void setDeleteAfter() throws IOException {
        expectStatusCode(202, false);
        prepareMetadata();
        object.setDeleteAfter(42);
        verifyHeaderValue("42", X_DELETE_AFTER, "POST");
        verifyHeaderValue(null, X_DELETE_AT, "POST");
   }

    @Test
    public void deleteObject() {
        expectStatusCode(204);
        object.delete();
    }

    @Test
    public void copyObject() throws IOException {
        Container beta = account.getContainer("beta");
        StoredObject object2 = beta.getObject("other-image.png");
        expectStatusCode(201);
        object.copyObject(beta, object2);
        verifyHeaderValue("/alpha/image.png", CopyFrom.X_COPY_FROM);
    }

    protected void expectPublicUrl(final String url) {
        new Expectations() {{
            defaultAccess.getPublicURL();
            result = url;
        }};
    }

    @Test(expected = CommandException.class)
    public void getPublicUrlThrowsException() {
        Container container = account.getContainer("alpha");
        object = container.getObject(null);
        expectPublicUrl("http://static.resource.com");
        assertEquals("http://static.resource.com/alpha/a+n%C3%A4m%C3%BC+with+spaces.png", object.getPublicURL());
    }

    @Test
    public void getPublicUrlEncoded() {
        Container container = account.getContainer("alpha");
        object = container.getObject("a nämü with spaces.png");
        expectPublicUrl("http://static.resource.com");
        assertEquals("http://static.resource.com/alpha/a+n%C3%A4m%C3%BC+with+spaces.png", object.getPublicURL());
    }

    @Test
    public void getPublicUrl() {
        expectPublicUrl("http://static.resource.com");
        assertEquals("http://static.resource.com/alpha/image.png", object.getPublicURL());
    }

    @Test
    public void setMetadata() throws IOException {
        expectStatusCode(202);
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", "1989");
        metadata.put("Company", "42 BV");
        object.setMetadata(metadata);
        verifyHeaderValue("1989", X_OBJECT_META_PREFIX + "Year");
        verifyHeaderValue("42 BV", X_OBJECT_META_PREFIX + "Company");
    }

    @Test
    public void getMetadata() throws IOException, DateParseException {
        expectStatusCode(202);
        prepareMetadata();
        assertEquals("1989", object.getMetadata().get("Year"));
        assertEquals("42 BV", object.getMetadata().get("Company"));
        assertEquals("Mon, 03 Sep 2012 05:40:33 GMT", object.getLastModified());
        assertEquals(DateUtils.parseDate("Mon, 03 Sep 2012 05:40:33 GMT"), object.getLastModifiedAsDate());
        assertEquals(654321, object.getContentLength());
        assertEquals("image/png", object.getContentType());
        assertEquals("cae4ebb15a282e98ba7b65402a72f57c", object.getEtag());
        assertEquals("1339429105", object.getDeleteAt());
        assertEquals(DateUtils.parseDate("Mon, 11 Jun 2012 15:38:25 GMT"), object.getDeleteAtAsDate());
    }

    @Test
    public void emptyDeleteAt() {
        expectStatusCode(202);
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, LAST_MODIFIED, "Mon, 03 Sep 2012 05:40:33 GMT", headers);
        prepareHeadersForRetrieval(response, headers);
        assertNull(object.getDeleteAt());
        assertNull(object.getDeleteAtAsDate());
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
    public void compareObjectsWithSameNameInDifferentContainers() {
        Container container1 = account.getContainer("alpha");
        StoredObject object1 = container1.getObject("img1.png");
        Container container2 = account.getContainer("beta");
        StoredObject object2 = container2.getObject("img1.png");
        assertNotSame(object1, object2);
        assertEquals(-1, object1.compareTo(object2));
        assertEquals(1, object2.compareTo(object1));
    }

    @Test
    public void checkWhetherANonExistingFileExists() {
        expectStatusCode(404);
        assertFalse(object.exists());
    }

    @Test(expected = CommandException.class)
    public void checkWhetherANonExistingFileExistsButThrowAnotherError() {
        expectStatusCode(500);
        object.exists();
    }

    @Test
    public void requiresSegmentation() {
        final UploadInstructions instructions = new UploadInstructions(new byte[] { 0x01 });
        new NonStrictExpectations(instructions) {{
            instructions.requiresSegmentation();
            result = true;
        }};
        Container container1 = account.getContainer("alpha");
        final AbstractStoredObject object = (AbstractStoredObject)container1.getObject("alpha");
        new Expectations(object) {{
            object.uploadObjectAsSegments(instructions);
            result = null;
            times = 1;
        }};
        object.uploadObject(instructions);
    }

    @Test
    public void setLastModified() {
        String dateText = "2012-12-05T14:57:00.165930";
        StoredObject object = createStoredObject("alpha");
        object.setLastModified(dateText);
        object.metadataSetFromHeaders();
        Date date = object.getLastModifiedAsDate();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        assertEquals("2012-12-05T14:57:00", formatter.format(date));
    }

    @Test(expected = CommandException.class)
    public void setLastModifiedFalseDate() {
        String dateText = "2012/12/05";
        StoredObject object = createStoredObject("alpha");
        object.setLastModified(dateText);
    }

    protected StoredObject createStoredObject(String name) {
        AccountImpl account = new AccountImpl(null, null, null, false);
        ContainerImpl container = new ContainerImpl(account, "", false);
        return new StoredObjectImpl(container, "alpha", true);
    }

}