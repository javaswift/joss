package org.javaswift.joss.headers.container;

import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.SimpleHeader;

public class ContainerWritePermissions extends SimpleHeader {
    public static final String X_CONTAINER_WRITE = "X-Container-Write";

    public ContainerWritePermissions(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_CONTAINER_WRITE;
    }

    public static ContainerWritePermissions fromResponse(HttpResponse response) {
        return new ContainerWritePermissions(convertResponseHeader(response, X_CONTAINER_WRITE));
    }
}
