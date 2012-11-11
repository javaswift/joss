package nl.tweeenveertig.openstack.information;

import nl.tweeenveertig.openstack.headers.Header;
import nl.tweeenveertig.openstack.headers.object.DeleteAfter;
import nl.tweeenveertig.openstack.headers.object.DeleteAt;
import nl.tweeenveertig.openstack.headers.object.ObjectContentType;
import nl.tweeenveertig.openstack.headers.object.ObjectMetadata;
import nl.tweeenveertig.openstack.information.ObjectInformation;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class ObjectInformationTest {

    @Test
    public void joinMetadataAndContentType() {
        ObjectInformation info = new ObjectInformation();
        info.addMetadata(new ObjectMetadata("title", "alpha"));
        info.addMetadata(new ObjectMetadata("description", "beta"));
        info.setDeleteAfter(new DeleteAfter(42));
        info.setDeleteAt(new DeleteAt(new Date()));
        info.setContentType(new ObjectContentType("text/plain"));
        Collection<Header> headers = info.getHeadersIncludingHeader(new ObjectContentType("image/png"));
        assertEquals(5, headers.size());
        for (Header header : headers) {
            if (ObjectContentType.CONTENT_TYPE.equals(header.getHeaderName())) {
                assertEquals("image/png", header.getHeaderValue());
            }
        }
    }

    @Test
    public void noHeader() {
        ObjectInformation info = new ObjectInformation();
        Collection<Header> headers = info.getHeaders();
        assertEquals(0, headers.size());
    }
}
