package org.javaswift.joss.command.impl.object;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.information.ObjectInformation;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.javaswift.joss.headers.object.ObjectMetadata.X_OBJECT_META_PREFIX;
import static org.javaswift.joss.headers.object.ObjectLastModified.LAST_MODIFIED;
import static org.javaswift.joss.headers.object.Etag.ETAG;
import static org.javaswift.joss.headers.object.ObjectContentLength.CONTENT_LENGTH;
import static org.javaswift.joss.headers.object.ObjectContentType.CONTENT_TYPE;
import static org.javaswift.joss.headers.object.DeleteAt.X_DELETE_AT;
import static org.mockito.Mockito.when;

public class ObjectInformationCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        prepareMetadata();
    }

    private void prepareMetadata() {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_OBJECT_META_PREFIX+ "Description", "Photo album", headers);
        prepareHeader(response, X_OBJECT_META_PREFIX+ "Year", "1984", headers);
        prepareHeader(response, LAST_MODIFIED, "Mon, 03 Sep 2012 05:40:33 GMT");
        prepareHeader(response, ETAG, "cae4ebb15a282e98ba7b65402a72f57c", headers);
        prepareHeader(response, CONTENT_LENGTH, "654321", headers);
        prepareHeader(response, CONTENT_TYPE, "image/png", headers);
        prepareHeader(response, X_DELETE_AT, "1339429105", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
    }

    @Test(expected = CommandException.class)
    public void illegalDate() {
        prepareHeader(response, LAST_MODIFIED, "I'm not a date!");
        when(statusLine.getStatusCode()).thenReturn(200);
        new ObjectInformationCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName")).call();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        ObjectInformation info = new ObjectInformationCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName")).call();
        assertEquals("Photo album", info.getMetadata("Description"));
        assertEquals("1984", info.getMetadata("Year"));
        assertEquals("Mon, 03 Sep 2012 05:40:33 GMT", info.getLastModified());
        assertEquals("cae4ebb15a282e98ba7b65402a72f57c", info.getEtag());
        assertEquals(654321, info.getContentLength());
        assertEquals("image/png", info.getContentType());
        assertEquals("1339429105", info.getDeleteAt().getHeaderValue());
    }

    @Test (expected = NotFoundException.class)
    public void createContainerFail() throws IOException {
        checkForError(404, new ObjectInformationCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName")));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ObjectInformationCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName")));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ObjectInformationCommandImpl(this.account, httpClient, defaultAccess, getObject("objectName")));
    }

}
