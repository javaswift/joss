package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.Header;
import nl.tweeenveertig.openstack.headers.object.ObjectContentType;
import nl.tweeenveertig.openstack.headers.object.ObjectMetadata;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.Assert.assertEquals;

public class ObjectInformationTest {

    @Test
    public void joinMetadataAndContentType() {
        ObjectInformation info = new ObjectInformation();
        info.addMetadata(new ObjectMetadata("title", "alpha"));
        info.addMetadata(new ObjectMetadata("description", "beta"));
        info.setContentType(new ObjectContentType("text/plain"));
        Collection<Header> headers = info.getHeadersIncludingContentType("image/png");
        assertEquals(3, headers.size());
        for (Header header : headers) {
            if (ObjectContentType.CONTENT_TYPE.equals(header.getHeaderName())) {
                assertEquals("image/png", header.getHeaderValue());
            }
        }
    }
}
