package org.javaswift.joss.headers.object;

import org.javaswift.joss.headers.SimpleHeader;
import org.apache.http.HttpResponse;

public class ObjectContentType extends SimpleHeader {

    public static final String CONTENT_TYPE = "Content-Type";

    public ObjectContentType(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return CONTENT_TYPE;
    }

    public static ObjectContentType fromResponse(HttpResponse response) {
        return new ObjectContentType(convertResponseHeader(response, CONTENT_TYPE));
    }
}
