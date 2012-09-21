package nl.tweeenveertig.openstack.headers.metadata;

public class ObjectMetadata extends Metadata {

    public static final String X_OBJECT_META_PREFIX = "X-Object-Meta-";

    public ObjectMetadata(String name, String value) {
        super(name, value);
    }

    @Override
    public String getHeaderName() {
        return X_OBJECT_META_PREFIX + getName();
    }
}
