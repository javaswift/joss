package org.javaswift.joss.headers.container;

import org.javaswift.joss.headers.SimpleHeader;
import org.apache.http.HttpResponse;

public class ContainerObjectCount extends SimpleHeader {

    public static final String X_CONTAINER_OBJECT_COUNT = "X-Container-Object-Count";

    public ContainerObjectCount(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_CONTAINER_OBJECT_COUNT;
    }

    public static ContainerObjectCount fromResponse(HttpResponse response) {
        return new ContainerObjectCount(convertResponseHeader(response, X_CONTAINER_OBJECT_COUNT));
    }
}
