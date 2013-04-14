package org.javaswift.joss.headers.container;

import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.Metadata;

import java.util.Map;
import java.util.TreeMap;

public class ContainerMetadata extends Metadata {

    public static final String X_CONTAINER_META_PREFIX = "X-Container-Meta-";

    public ContainerMetadata(String name, String value) {
        super(name, value);
    }

    @Override
    public String getHeaderName() {
        return X_CONTAINER_META_PREFIX + getName();
    }

    public static Map<String, Metadata> fromResponse(HttpResponse response) {
        Map<String, Metadata> metadata = new TreeMap<String, Metadata>();
        for (org.apache.http.Header header : getResponseHeadersStartingWith(response, X_CONTAINER_META_PREFIX)) {
            ContainerMetadata containerMetadata = new ContainerMetadata(header.getName().substring(X_CONTAINER_META_PREFIX.length()), header.getValue());
            metadata.put(containerMetadata.getName(), containerMetadata);
        }
        return metadata;
    }

}
