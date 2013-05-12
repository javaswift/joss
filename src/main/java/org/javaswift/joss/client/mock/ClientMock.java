package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractClient;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.mock.factory.AuthenticationCommandFactoryMock;
import org.javaswift.joss.command.shared.factory.AuthenticationCommandFactory;
import org.javaswift.joss.model.Client;
import org.javaswift.joss.swift.MockUserStore;
import org.javaswift.joss.swift.Swift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMock extends AbstractClient<AccountMock> {

    public static final Logger LOG = LoggerFactory.getLogger(ClientMock.class);

    private MockUserStore users = new MockUserStore();

    private Swift swift;

    public ClientMock(AccountConfig accountConfig) {
        super(accountConfig);
    }

    @Override
    protected AuthenticationCommandFactory createFactory() {
        return new AuthenticationCommandFactoryMock(getOrCreateSwift());
    }

    protected Swift getOrCreateSwift() {
        if (this.swift == null) {
            this.swift = new Swift()
                    .setAllowObjectDeleter(accountConfig.isMockAllowObjectDeleter())
                    .setOnFileObjectStore(accountConfig.getMockOnFileObjectStore())
                    .setUserStore(users)
                    .setMillisDelay(accountConfig.getMockMillisDelay());
        }
        return this.swift;
    }

    @Override
    protected void logSettings() {
        LOG.info("JOSS / MOCK mode");
        LOG.info("JOSS / Creating mock account instance");
        LOG.info("JOSS / * Check credentials: "+!accountConfig.isMockAllowEveryone());
        LOG.info("JOSS / * Allow objectdeleter: "+accountConfig.isMockAllowObjectDeleter());
        LOG.info("JOSS / * On File ObjectStore: "+accountConfig.getMockOnFileObjectStore());
        LOG.info("JOSS / * Use milliseconds delay: "+ accountConfig.getMockMillisDelay());
    }

    @Override
    protected AccountMock createAccount(String preferredRegion) {
        if (!accountConfig.isMockAllowEveryone()) {
            LOG.info(
                    "JOSS / Attempting authentication with tenant name: " + accountConfig.getTenantName()+
                    ", tenant ID: "+accountConfig.getTenantId()+
                    ", username: " +accountConfig.getUsername()+
                    ", Auth URL: " +accountConfig.getAuthUrl());
            this.factory.createAuthenticationCommand(
                    null,
                    null,
                    accountConfig.getTenantName(),
                    accountConfig.getTenantId(),
                    accountConfig.getUsername(),
                    accountConfig.getPassword()).call();
        }
        return new AccountMock(swift);
    }

    public MockUserStore getUsers() { return this.swift.getUserStore(); }
    public void setUsers(MockUserStore users) { this.swift.setUserStore(users); }
}
