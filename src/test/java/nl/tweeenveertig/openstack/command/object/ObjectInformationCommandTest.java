package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.model.ObjectInformation;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static nl.tweeenveertig.openstack.headers.object.ObjectMetadata.X_OBJECT_META_PREFIX;
import static nl.tweeenveertig.openstack.headers.object.ObjectLastModified.LAST_MODIFIED;
import static nl.tweeenveertig.openstack.headers.object.Etag.ETAG;
import static nl.tweeenveertig.openstack.headers.object.ObjectContentLength.CONTENT_LENGTH;
import static nl.tweeenveertig.openstack.headers.object.ObjectContentType.CONTENT_TYPE;
import static org.mockito.Mockito.when;

public class ObjectInformationCommandTest extends BaseCommandTest {

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
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(200);
        ObjectInformation info = new ObjectInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName")).call();
        assertEquals("Photo album", info.getMetadata("Description"));
        assertEquals("1984", info.getMetadata("Year"));
        assertEquals("Mon, 03 Sep 2012 05:40:33 GMT", info.getLastModified());
        assertEquals("cae4ebb15a282e98ba7b65402a72f57c", info.getEtag());
        assertEquals(654321, info.getContentLength());
        assertEquals("image/png", info.getContentType());
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(404, new ObjectInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName")), CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ObjectInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName")), CommandExceptionError.UNKNOWN);
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ObjectInformationCommand(this.account, httpClient, defaultAccess,
                account.getContainer("containerName"), getObject("objectName")));
    }

}
