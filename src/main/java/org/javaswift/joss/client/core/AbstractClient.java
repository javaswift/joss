package org.javaswift.joss.client.core;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.shared.factory.AuthenticationCommandFactory;
import org.javaswift.joss.command.shared.identity.tenant.Tenant;
import org.javaswift.joss.command.shared.identity.tenant.Tenants;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractClient<A extends Account> implements Client<A> {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractClient.class);

    protected AccountConfig accountConfig;

    protected AuthenticationCommandFactory factory;

    public AbstractClient(AccountConfig accountConfig) {
        this.accountConfig = accountConfig;
        logSettings();
        this.factory = createFactory();
    }

    protected abstract void logSettings();

    protected abstract AuthenticationCommandFactory createFactory();

    protected abstract A createAccount();

    @SuppressWarnings("unchecked")
    @Override
    public A authenticate() {
        A account = createAccount();
        if (!account.isTenantSupplied()) {
            Tenant tenant = autoDiscoverTenant(account);
            accountConfig.setTenantId(tenant.id);
            accountConfig.setTenantName(tenant.name);
            account = createAccount();
        }
        return (A)account
                .setPublicHost(accountConfig.getPublicHost())
                .setPrivateHost(accountConfig.getPrivateHost())
                .setAllowContainerCaching(accountConfig.isAllowContainerCaching())
                .setAllowReauthenticate(accountConfig.isAllowReauthenticate());
    }

    protected Tenant autoDiscoverTenant(Account account) {
        LOG.info("JOSS / No tenant supplied, attempting auto-discovery");
        Tenants tenants = account.getTenants();
        Tenant tenant = tenants.getTenant();
        LOG.info("JOSS / Found tenant with name "+tenant.name+" and ID "+tenant.id);
        return tenant;
    }

}
