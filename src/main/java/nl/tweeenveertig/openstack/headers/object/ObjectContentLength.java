package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.SimpleHeader;
import org.apache.http.HttpResponse;

public class ObjectContentLength extends SimpleHeader {

    public static final String CONTENT_LENGTH            = "Content-Length";

    public ObjectContentLength(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return CONTENT_LENGTH;
    }

    public static ObjectContentLength fromResponse(HttpResponse response) {
        return new ObjectContentLength(convertResponseHeader(response, CONTENT_LENGTH));
    }
}
