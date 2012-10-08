package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.headers.Metadata;
import nl.tweeenveertig.openstack.headers.object.ObjectMetadata;
import nl.tweeenveertig.openstack.model.ObjectInformation;

import java.util.Date;

public abstract class AbstractStoredObject extends AbstractObjectStoreEntity<ObjectInformation> implements StoredObject, Comparable<StoredObject> {

    protected String name;

    private Container container;

    public AbstractStoredObject(Container container, String name) {
        this.container = container;
        this.name = name;
        this.info = new ObjectInformation();
    }

    public Date getLastModifiedAsDate() {
        checkForInfo();
        return info.getLastModifiedAsDate();
    }

    public String getLastModified() {
        checkForInfo();
        return info.getLastModified();
    }

    public String getEtag() {
        checkForInfo();
        return info.getEtag();
    }

    public long getContentLength() {
        checkForInfo();
        return info.getContentLength();
    }

    public String getContentType() {
        checkForInfo();
        return info.getContentType();
    }

    public Date getDeleteAtAsDate() {
        checkForInfo();
        return info.getDeleteAt() == null ? null : info.getDeleteAt().getDate();
    }

    public String getDeleteAt() {
        checkForInfo();
        return info.getDeleteAt() == null ? null : info.getDeleteAt().getHeaderValue();
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

    protected Metadata createMetadataEntry(String name, String value) {
        return new ObjectMetadata(name, value);
    }
}
