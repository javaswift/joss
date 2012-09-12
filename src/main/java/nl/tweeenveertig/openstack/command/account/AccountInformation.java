package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.command.core.AbstractInformation;

public class AccountInformation extends AbstractInformation {

    private int containerCount;

    private int objectCount;

    private long bytesUsed;

    public int getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(int containerCount) {
        this.containerCount = containerCount;
    }

    public long getBytesUsed() {
        return bytesUsed;
    }

    public void setBytesUsed(long bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }
}
