package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.SimpleHeader;

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

}
