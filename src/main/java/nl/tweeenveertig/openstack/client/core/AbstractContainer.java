package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;

public abstract class AbstractContainer extends AbstractObjectStoreEntity implements Container, Comparable<Container> {

    protected int objectCount;
    protected long bytesUsed;
    protected boolean publicContainer;
    protected String name;

    private Account account;

    public AbstractContainer(Account account, String name) {
        this.name = name;
        this.account = account;
    }

    public int getObjectCount() {
        checkForInfo();
        return objectCount;
    }

    public long getBytesUsed() {
        checkForInfo();
        return bytesUsed;
    }
    public boolean isPublic() {
        checkForInfo();
        return publicContainer;
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
}
