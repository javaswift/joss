package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.impl.AbstractPaginationMap;
import nl.tweeenveertig.openstack.client.impl.AccountPaginationMap;
import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.headers.Metadata;
import nl.tweeenveertig.openstack.headers.account.AccountMetadata;
import nl.tweeenveertig.openstack.information.AccountInformation;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.PaginationMap;

import java.util.Collection;

public abstract class AbstractAccount extends AbstractObjectStoreEntity<AccountInformation> implements Account {

    private static final Integer MAX_PAGE_SIZE = 9999;

    private boolean allowReauthenticate = true;

    public Collection<Container> list() {
        return list((String) null, getMaxPageSize());
    }

    public Collection<Container> list(PaginationMap paginationMap, int page) {
        return list(paginationMap.getMarker(page), paginationMap.getPageSize());
    }

    public PaginationMap getPaginationMap(int pageSize) {
        return new AccountPaginationMap(this, pageSize).buildMap();
    }

    public AbstractAccount(boolean allowCaching) {
        super(allowCaching);
        this.info = new AccountInformation();
    }

    public AbstractAccount setAllowReauthenticate(boolean allowReauthenticate) {
        this.allowReauthenticate = allowReauthenticate;
        return this;
    }

    public boolean isAllowReauthenticate() {
        return this.allowReauthenticate;
    }

    public int getCount() {
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

    protected Metadata createMetadataEntry(String name, String value) {
        return new AccountMetadata(name, value);
    }

    public int getMaxPageSize() {
        return MAX_PAGE_SIZE;
    }
}
