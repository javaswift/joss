package org.javaswift.joss.client.core;

import org.javaswift.joss.command.shared.factory.AccountCommandFactory;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.identity.tenant.Tenants;
import org.javaswift.joss.headers.Metadata;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.javaswift.joss.information.AccountInformation;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.PaginationMap;
import org.javaswift.joss.model.Website;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

public abstract class AbstractAccount extends AbstractObjectStoreEntity<AccountInformation> implements Account {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractAccount.class);

    private static final Integer MAX_PAGE_SIZE = 9999;

    private boolean allowReauthenticate = true;

    private int numberOfCalls = 0;

    private final AccountCommandFactory commandFactory;

    private final ContainerCache<Container> containerCache;

    private final ContainerCache<Website> websiteCache;

    public AbstractAccount(AccountCommandFactory commandFactory, ContainerFactory<Container> containerFactory,
                           ContainerFactory<Website> websiteFactory, boolean allowCaching) {
        super(allowCaching);
        this.containerCache = new ContainerCache<Container>(this, containerFactory);
        this.websiteCache = new ContainerCache<Website>(this, websiteFactory);
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

    @Override
    public AbstractAccount setPublicHost(String publicHost) {
        LOG.info("JOSS / Use public host: "+publicHost);
        this.commandFactory.setPublicHost(publicHost);
        return this;
    }

    @Override
    public AbstractAccount setPrivateHost(String privateHost) {
        LOG.info("JOSS / Use private host: "+privateHost);
        this.commandFactory.setPrivateHost(privateHost);
        return this;
    }

    public AbstractAccount setAllowReauthenticate(boolean allowReauthenticate) {
        LOG.info("JOSS / Allow reauthentication: " + allowReauthenticate);
        this.allowReauthenticate = allowReauthenticate;
        return this;
    }

    public AbstractAccount setAllowContainerCaching(boolean allowContainerCaching) {
        LOG.info("JOSS / Allow Container caching: "+allowContainerCaching);
        this.containerCache.setCacheEnabled(allowContainerCaching);
        this.websiteCache.setCacheEnabled(allowContainerCaching);
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
        return this.commandFactory.getPublicHost();
    }

    @Override
    public String getPrivateURL() {
        return this.commandFactory.getPrivateHost();
    }

    @Override
    protected void saveMetadata() {
        commandFactory.createAccountMetadataCommand(this, info.getMetadata()).call();
    }

    protected void getInfo(boolean allowErrorLog) {
        this.info = commandFactory.createAccountInformationCommand(this).call();
        this.setInfoRetrieved();
    }

    @Override
    public Tenants getTenants() {
        return this.commandFactory.createTenantCommand(this).call();
    }

    @Override
    public boolean isTenantSupplied() {
        return this.commandFactory.isTenantSupplied();
    }

    @Override
    public String getPathForEntity() throws UnsupportedEncodingException {
        return "";
    }

    @Override
    public Container getContainer(String containerName) {
        return this.containerCache.getContainer(containerName);
    }

    @Override
    public Website getWebsite(String containerName) {
        return this.websiteCache.getContainer(containerName);
    }

    @Override
    public void resetContainerCache() {
        this.containerCache.reset();
    }

    @Override
    public String getOriginalHost() {
        return this.commandFactory.getOriginalHost();
    }

}
