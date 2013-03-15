package org.javaswift.joss.client.core;

import org.javaswift.joss.client.impl.AccountPaginationMap;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.headers.Metadata;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.javaswift.joss.information.AccountInformation;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.PaginationMap;

import java.util.Collection;

public abstract class AbstractAccount extends AbstractObjectStoreEntity<AccountInformation> implements Account {

    private static final Integer MAX_PAGE_SIZE = 9999;

    private boolean allowReauthenticate = true;

    private int numberOfCalls = 0;

    public Collection<Container> list() {
        return list(null, null, getMaxPageSize());
    }

    public Collection<Container> list(PaginationMap paginationMap, int page) {
        return list(paginationMap.getPrefix(), paginationMap.getMarker(page), paginationMap.getPageSize());
    }

    public PaginationMap getPaginationMap(String prefix, int pageSize) {
        return new AccountPaginationMap(this, prefix, pageSize).buildMap();
    }

    public PaginationMap getPaginationMap(int pageSize) {
        return getPaginationMap(null, pageSize);
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

    public void increaseCallCounter() {
        numberOfCalls++;
    }

    public int getNumberOfCalls() {
        return numberOfCalls;
    }

}
