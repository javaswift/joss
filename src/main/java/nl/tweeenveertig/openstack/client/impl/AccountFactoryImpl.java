package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.core.AccountFactory;

public class AccountFactoryImpl extends AccountFactory<AccountImpl> {

    @Override
    public AccountImpl getAccount() {
        return new ClientImpl().authenticate(tenant, username, password, authUrl);
    }
}
