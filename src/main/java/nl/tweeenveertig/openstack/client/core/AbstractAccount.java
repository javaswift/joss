package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.model.AccountInformation;

public abstract class AbstractAccount extends AbstractObjectStoreEntity<AccountInformation> implements Account {

    public AbstractAccount() {
        this.info = new AccountInformation();
    }

    public int getContainerCount() {
        checkForInfo();
        return info.getContainerCount();
    }

    public long getBytesUsed() {
        checkForInfo();
        return info.getBytesUsed();
    }

    public int getObjectCount() {
        checkForInfo();
        return info.getObjectCount();
    }

}
