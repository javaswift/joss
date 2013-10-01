package org.javaswift.joss.headers.container;

import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.SimpleHeader;

public class ContainerReadPermissions extends SimpleHeader {
    public static final String X_CONTAINER_READ = "X-Container-Read";
    
    public ContainerReadPermissions(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_CONTAINER_READ;
    }

    public static ContainerReadPermissions fromResponse(HttpResponse response) {
        return new ContainerReadPermissions(convertResponseHeader(response, X_CONTAINER_READ));
    }
}
