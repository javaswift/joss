package org.javaswift.joss.headers.object;

import org.javaswift.joss.headers.Metadata;
import org.apache.http.HttpResponse;

import java.util.Map;
import java.util.TreeMap;

public class ObjectMetadata extends Metadata {

    public static final String X_OBJECT_META_PREFIX = "X-Object-Meta-";

    public ObjectMetadata(String name, String value) {
        super(name, value);
    }

    @Override
    public String getHeaderName() {
        return X_OBJECT_META_PREFIX + getName();
    }

    public static Map<String, Metadata> fromResponse(HttpResponse response) {
        Map<String, Metadata> metadata = new TreeMap<String, Metadata>();
        for (org.apache.http.Header header : getResponseHeadersStartingWith(response, X_OBJECT_META_PREFIX)) {
            ObjectMetadata objectMetadata = new ObjectMetadata(header.getName().substring(X_OBJECT_META_PREFIX.length()), header.getValue());
            metadata.put(objectMetadata.getName(), objectMetadata);
        }
        return metadata;
    }
}
