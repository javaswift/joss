package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.mock.StoredObjectMock;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.headers.Metadata;
import nl.tweeenveertig.openstack.headers.container.ContainerMetadata;
import nl.tweeenveertig.openstack.headers.object.ObjectManifest;
import nl.tweeenveertig.openstack.model.ContainerInformation;
import nl.tweeenveertig.openstack.model.SegmentationPlan;
import nl.tweeenveertig.openstack.model.UploadInstructions;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractContainer extends AbstractObjectStoreEntity<ContainerInformation> implements Container {

    protected String name;

    private Account account;

    public AbstractContainer(Account account, String name) {
        this.name = name;
        this.account = account;
        this.info = new ContainerInformation();
    }

    public StoredObject getObjectSegment(String name, int part) {
        return getObject(name + "/" + String.format("%08d", part));
    }

    public int getObjectCount() {
        checkForInfo();
        return info.getObjectCount();
    }

    public long getBytesUsed() {
        checkForInfo();
        return info.getBytesUsed();
    }

    public boolean isPublic() {
        checkForInfo();
        return info.isPublicContainer();
    }

    public String getName() {
        return name;
    }

    public Account getAccount() {
        return account;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    @SuppressWarnings("ConstantConditions")
    public boolean equals(Object o) {
        return o instanceof Container && getName().equals(((Container) o).getName());
    }

    @SuppressWarnings("ConstantConditions")
    public int compareTo(Container o) {
        return getName().compareTo(o.getName());
    }

    protected Metadata createMetadataEntry(String name, String value) {
        return new ContainerMetadata(name, value);
    }

    public void uploadSegmentedObjects(String name, UploadInstructions uploadInstructions) {

        try {
            SegmentationPlan plan = uploadInstructions.getSegmentationPlan();
            InputStream segmentStream = plan.getNextSegment();
            while (segmentStream != null) {
                StoredObject segment = getObjectSegment(name, plan.getSegmentNumber().intValue());
                segment.uploadObject(segmentStream);
                segmentStream.close();
                segmentStream = plan.getNextSegment();
            }
        } catch (IOException err) {
            throw new CommandException("Unable to upload segments", err);
        }
    }

}
