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

}
