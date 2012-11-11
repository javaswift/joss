package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.instructions.UploadInstructions;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoredObject;
import nl.tweeenveertig.openstack.headers.Metadata;
import nl.tweeenveertig.openstack.headers.object.ObjectManifest;
import nl.tweeenveertig.openstack.headers.object.ObjectMetadata;
import nl.tweeenveertig.openstack.information.ObjectInformation;

import java.util.Date;

public abstract class AbstractStoredObject extends AbstractObjectStoreEntity<ObjectInformation> implements StoredObject {

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

    public String getPath() {
        return getContainer().getName() + "/" + getName();
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public void uploadObject(UploadInstructions uploadInstructions) {
        if (uploadInstructions.requiresSegmentation()) {
            uploadObjectAsSegments(uploadInstructions);
        } else {
            directlyUploadObject(uploadInstructions);
        }
    }

    public void uploadObjectAsSegments(UploadInstructions uploadInstructions) {
        ((AbstractContainer)getContainer()).uploadSegmentedObjects(getName(), uploadInstructions);
        // The manifest file is the handle which allows the ObjectStore to piece the segments together as one file
        UploadInstructions manifest = new UploadInstructions(new byte[] {})
                .setObjectManifest(new ObjectManifest(getPath()))
                .setContentType(uploadInstructions.getContentType());
        uploadObject(manifest);
    }

    protected abstract void directlyUploadObject(UploadInstructions uploadInstructions);

    @SuppressWarnings("ConstantConditions")
    public boolean equals(Object o) {
        return o instanceof StoredObject && compareTo((StoredObject) o) == 0;
    }

    @SuppressWarnings("ConstantConditions")
    public int compareTo(StoredObject o) {
        int compareValue = getName().compareTo(o.getName());
        if (compareValue == 0) {
            compareValue = getContainer().compareTo(((AbstractStoredObject) o).getContainer());
        }
        return compareValue;
    }

    protected Metadata createMetadataEntry(String name, String value) {
        return new ObjectMetadata(name, value);
    }
}
