package org.javaswift.joss.headers.object;

import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.SimpleHeader;

public class ObjectManifest extends SimpleHeader {

    public static String X_OBJECT_MANIFEST = "X-Object-Manifest";

    public ObjectManifest(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_OBJECT_MANIFEST;
    }

    public String getContainerName() {
        return this.getHeaderValue().substring(0, this.getHeaderValue().indexOf("/"));
    }

    public String getObjectPrefix() {
        return this.getHeaderValue().substring(this.getHeaderValue().indexOf("/") + 1);
    }

    public static ObjectManifest fromResponse(HttpResponse response) {
    	String manifest = convertResponseHeader(response, X_OBJECT_MANIFEST);
    	if (manifest == null)
    		return null;
    	return new ObjectManifest(manifest);
    }
}
