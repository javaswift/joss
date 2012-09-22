package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.SimpleHeader;
import org.apache.http.HttpResponse;

public class ObjectLastModified extends SimpleHeader {

    public static final String LAST_MODIFIED = "Last-Modified";

    public ObjectLastModified(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return LAST_MODIFIED;
    }

    public static ObjectLastModified fromResponse(HttpResponse response) {
        return new ObjectLastModified(convertResponseHeader(response, LAST_MODIFIED));
    }
}
