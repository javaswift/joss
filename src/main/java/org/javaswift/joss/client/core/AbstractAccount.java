package org.javaswift.joss.client.core;

import org.javaswift.joss.command.shared.factory.AccountCommandFactory;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.headers.Metadata;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.javaswift.joss.information.AccountInformation;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.PaginationMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractAccount extends AbstractObjectStoreEntity<AccountInformation> implements Account {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractAccount.class);

    private static final Integer MAX_PAGE_SIZE = 9999;

    private boolean allowReauthenticate = true;

    private int numberOfCalls = 0;

    private final AccountCommandFactory commandFactory;

    public AbstractAccount(AccountCommandFactory commandFactory, boolean allowCaching) {
        super(allowCaching);
        this.commandFactory = commandFactory;
        this.info = new AccountInformation();
    }

    public Collection<Container> list() {
        return new AccountPaginationMap(this, null, MAX_PAGE_SIZE).listAllItems();
    }

    public Collection<Container> list(PaginationMap paginationMap, int page) {
        return list(paginationMap.getPrefix(), paginationMap.getMarker(page), paginationMap.getPageSize());
    }

    public Collection<Container> list(String prefix, String marker, int pageSize) {
        ListInstructions listInstructions = new ListInstructions()
                .setPrefix(prefix)
                .setMarker(marker)
                .setLimit(pageSize);
        return commandFactory.createListContainersCommand(this, listInstructions).call();
    }

    public PaginationMap getPaginationMap(String prefix, int pageSize) {
        return new AccountPaginationMap(this, prefix, pageSize).buildMap();
    }

    public PaginationMap getPaginationMap(int pageSize) {
        return getPaginationMap(null, pageSize);
    }

    public AbstractAccount setHost(String host) {
        LOG.info("JOSS / Use host: "+host);
        this.commandFactory.setHost(host);
        return this;
    }

    public AbstractAccount setAllowReauthenticate(boolean allowReauthenticate) {
        LOG.info("JOSS / Allow reauthentication: "+allowReauthenticate);
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

    public AccountCommandFactory getFactory() {
        return this.commandFactory;
    }

    public AccessImpl authenticate() {
        return this.commandFactory.authenticate();
    }

    @Override
    public String getPublicURL() {
        return this.commandFactory.getPublicURL();
    }

    @Override
    protected void saveMetadata() {
        commandFactory.createAccountMetadataCommand(this, info.getMetadata()).call();
    }

    protected void getInfo(boolean allowErrorLog) {
        this.info = commandFactory.createAccountInformationCommand(this).call();
        this.setInfoRetrieved();
    }

}
