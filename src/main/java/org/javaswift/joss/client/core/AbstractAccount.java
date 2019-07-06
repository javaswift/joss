package org.javaswift.joss.client.core;

import org.javaswift.joss.command.shared.factory.AccountCommandFactory;
import org.javaswift.joss.command.shared.identity.bulkdelete.BulkDeleteResponse;
import org.javaswift.joss.command.shared.identity.tenant.Tenants;
import org.javaswift.joss.headers.Metadata;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.javaswift.joss.headers.account.HashPassword;
import org.javaswift.joss.information.AccountInformation;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.*;
import org.javaswift.joss.util.LocalTime;
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

    private ServerTime serverTime = new ServerTime(0);

    private String preferredRegion = null;

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
    public String getHashPassword() {
        return (String)getMetadata(HashPassword.X_ACCOUNT_TEMP_URL_KEY);
    }

    @Override
    public AbstractAccount setHashPassword(String hashPassword) {
        LOG.info("JOSS / Setting hash password");
        if (hashPassword != null && !hashPassword.equals(getHashPassword())) {
            LOG.info("JOSS / Hash password not yet saved, saving now");
            this.commandFactory.createHashPasswordCommand(this, hashPassword).call();
        }
        this.invalidate(); // Make sure the metadata is refetched from the server
        return this;
    }

    @Override
    public AbstractAccount setPublicHost(String publicHost) {
        LOG.info("JOSS / Use public host: " + publicHost);
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

    @Override
    public Account setPreferredRegion(final String preferredRegion) {
        LOG.info("JOSS / PreferredRegion: " + preferredRegion);
        this.preferredRegion = preferredRegion;
        return this;
    }

    public boolean isAllowReauthenticate() {
        return this.allowReauthenticate;
    }

    @Override
    public int getCount() {
        checkForInfo();
        return info.getContainerCount();
    }

    @Override
    public long getBytesUsed() {
        checkForInfo();
        return info.getBytesUsed();
    }

    @Override
    public int getObjectCount() {
        checkForInfo();
        return info.getObjectCount();
    }

    @Override
    public long getServerTime() {
        checkForInfo();
        return info.getServerDate();
    }

    @Override
    public long getActualServerTimeInSeconds(long seconds) {
        return this.serverTime.getServerTime(seconds);
    }

    @Override
    public void synchronizeWithServerTime() {
        this.serverTime = ServerTime.create(getServerTime(), LocalTime.currentTime());
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

    public Access authenticate() {
        return this.commandFactory.authenticate();
    }

    public Access getAccess() {
       return this.commandFactory.getAccess();
    }

    @Override
    public String getPreferredRegion() {
        return this.preferredRegion;
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
    protected void saveSpecificMetadata() {
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

    @Override
    public BulkDeleteResponse bulkDelete(Collection<ObjectIdentifier> objectsToDelete) {
        return commandFactory.createBulkDeleteCommand(this, objectsToDelete).call();
    }

}
