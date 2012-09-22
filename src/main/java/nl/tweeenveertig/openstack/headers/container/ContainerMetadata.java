package nl.tweeenveertig.openstack.headers.container;

import nl.tweeenveertig.openstack.headers.Metadata;

public class ContainerMetadata extends Metadata {

    public static final String X_CONTAINER_META_PREFIX = "X-Container-Meta-";

    public ContainerMetadata(String name, String value) {
        super(name, value);
    }

    @Override
    public String getHeaderName() {
        return X_CONTAINER_META_PREFIX + getName();
    }
}
