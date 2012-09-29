package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static nl.tweeenveertig.openstack.headers.object.ObjectMetadata.X_OBJECT_META_PREFIX;
import static nl.tweeenveertig.openstack.headers.object.ObjectContentLength.CONTENT_LENGTH;
import static nl.tweeenveertig.openstack.headers.object.Etag.ETAG;
import static nl.tweeenveertig.openstack.headers.object.ObjectContentType.CONTENT_TYPE;
import static nl.tweeenveertig.openstack.headers.object.ObjectLastModified.LAST_MODIFIED;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoredObjectImplTest extends BaseCommandTest {

    private Container container;

    private StoredObject object;

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        when(statusLine.getStatusCode()).thenReturn(202);
        container = account.getContainer("alpha");
        object = container.getObject("image.png");
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

}
