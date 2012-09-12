package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;

public abstract class AbstractStoredObject extends AbstractObjectStoreEntity implements StoredObject, Comparable<StoredObject> {

    protected String lastModified;
    protected String etag;
    protected long contentLength;
    protected String contentType;
    protected String name;

    private Container container;

    public AbstractStoredObject(Container container, String name) {
        this.container = container;
        this.name = name;
    }

    public String getLastModified() {
        checkForInfo();
        return lastModified;
    }

    public String getEtag() {
        checkForInfo();
        return etag;
    }

    public long getContentLength() {
        checkForInfo();
        return contentLength;
    }

    public String getContentType() {
        checkForInfo();
        return contentType;
    }

    public String getName() {
        return name;
    }

    public Container getContainer() {
        return container;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    @SuppressWarnings("ConstantConditions")
    public boolean equals(Object o) {
        return o instanceof StoredObject && getName().equals(((StoredObject) o).getName());
    }

    @SuppressWarnings("ConstantConditions")
    public int compareTo(StoredObject o) {
        return getName().compareTo(o.getName());
    }
}
