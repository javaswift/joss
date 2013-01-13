package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.impl.ContainerPaginationMap;
import nl.tweeenveertig.openstack.headers.container.ContainerBytesUsed;
import nl.tweeenveertig.openstack.headers.container.ContainerObjectCount;
import nl.tweeenveertig.openstack.instructions.SegmentationPlan;
import nl.tweeenveertig.openstack.instructions.UploadInstructions;
import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.PaginationMap;
import nl.tweeenveertig.openstack.model.StoredObject;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.headers.Metadata;
import nl.tweeenveertig.openstack.headers.container.ContainerMetadata;
import nl.tweeenveertig.openstack.information.ContainerInformation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public abstract class AbstractContainer extends AbstractObjectStoreEntity<ContainerInformation> implements Container {

    private static final Integer MAX_PAGE_SIZE = 9999;

    protected String name;

    private Account account;

    public AbstractContainer(Account account, String name, boolean allowCaching) {
        super(allowCaching);
        this.name = name;
        this.account = account;
        this.info = new ContainerInformation();
    }

    public void metadataSetFromHeaders() {
        this.staleHeaders = false;
    }

    public Collection<StoredObject> list() {
        return list(null, null, getMaxPageSize());
    }

    public Collection<StoredObject> list(PaginationMap paginationMap, int page) {
        return list(paginationMap.getPrefix(), paginationMap.getMarker(page), paginationMap.getPageSize());
    }

    public PaginationMap getPaginationMap(String prefix, int pageSize) {
        return new ContainerPaginationMap(this, prefix, pageSize).buildMap();
    }

    public PaginationMap getPaginationMap(int pageSize) {
        return getPaginationMap(null, pageSize);
    }

    public StoredObject getObjectSegment(String name, int part) {
        return getObject(name + "/" + String.format("%08d", part));
    }

    public int getCount() {
        checkForInfoAndAllowHeaderSet();
        return info.getObjectCount();
    }

    public long getBytesUsed() {
        checkForInfoAndAllowHeaderSet();
        return info.getBytesUsed();
    }

    public void setCount(int count) {
        info.setObjectCount(new ContainerObjectCount(Integer.toString(count)));
    }

    public void setBytesUsed(long bytesUsed) {
        info.setBytesUsed(new ContainerBytesUsed(Long.toString(bytesUsed)));
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

    public int getMaxPageSize() {
        return MAX_PAGE_SIZE;
    }
}
