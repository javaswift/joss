package org.javaswift.joss.headers.object;

import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.SimpleHeader;

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
