package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.core.AccountFactory;

public class AccountFactoryMock extends AccountFactory<AccountMock> {

    public AccountMock getAccount() {
        return new ClientMock().setAllowEveryone(true).authenticate(tenant, username, password, authUrl);
    }
}
