package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.headers.Metadata;
import nl.tweeenveertig.openstack.headers.container.ContainerMetadata;
import nl.tweeenveertig.openstack.model.ContainerInformation;

public abstract class AbstractContainer extends AbstractObjectStoreEntity<ContainerInformation> implements Container {

    protected String name;

    private Account account;

    public AbstractContainer(Account account, String name) {
        this.name = name;
        this.account = account;
        this.info = new ContainerInformation();
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
}
