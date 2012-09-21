package nl.tweeenveertig.openstack.headers;

/**
 * Allows the rights change of a container to and from public/private
 */
public class ContainerRights extends Header {

    public static final String X_CONTAINER_READ = "X-Container-Read";

    private boolean publicContainer;

    public ContainerRights(boolean publicContainer) {
        this.publicContainer = publicContainer;
    }

    @Override
    public String getHeaderValue() {
        return publicContainer ? ".r:*" : "";
    }

    @Override
    public String getHeaderName() {
        return X_CONTAINER_READ;
    }
}
