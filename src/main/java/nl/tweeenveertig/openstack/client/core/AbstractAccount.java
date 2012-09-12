package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.Account;

public abstract class AbstractAccount extends AbstractObjectStoreEntity implements Account {

    protected int containerCount;
    protected int objectCount;
    protected long bytesUsed;

    public int getContainerCount() {
        checkForInfo();
        return containerCount;
    }

    public long getBytesUsed() {
        checkForInfo();
        return bytesUsed;
    }

    public int getObjectCount() {
        checkForInfo();
        return objectCount;
    }

}
