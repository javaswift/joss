package org.javaswift.joss.headers.container;

import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.SimpleHeader;

public class ContainerBytesUsed extends SimpleHeader {

    public static final String X_CONTAINER_BYTES_USED = "X-Container-Bytes-Used";

    public ContainerBytesUsed(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_CONTAINER_BYTES_USED;
    }

    public static ContainerBytesUsed fromResponse(HttpResponse response) {
        return new ContainerBytesUsed(convertResponseHeader(response, X_CONTAINER_BYTES_USED));
    }
}
