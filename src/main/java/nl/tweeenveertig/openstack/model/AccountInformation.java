package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.account.AccountBytesUsed;
import nl.tweeenveertig.openstack.headers.account.AccountContainerCount;
import nl.tweeenveertig.openstack.headers.account.AccountObjectCount;

public class AccountInformation extends AbstractInformation {

    private AccountContainerCount containerCount;

    private AccountObjectCount objectCount;

    private AccountBytesUsed bytesUsed;

    public int getContainerCount() {
        return Integer.parseInt(containerCount.getHeaderValue());
    }

    public void setContainerCount(AccountContainerCount containerCount) {
        this.containerCount = containerCount;
    }

    public long getBytesUsed() {
        return Long.parseLong(bytesUsed.getHeaderValue());
    }

    public void setBytesUsed(AccountBytesUsed bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    public int getObjectCount() {
        return Integer.parseInt(objectCount.getHeaderValue());
    }

    public void setObjectCount(AccountObjectCount objectCount) {
        this.objectCount = objectCount;
    }
}
