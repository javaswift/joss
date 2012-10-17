package nl.tweeenveertig.openstack.client.factory;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Client;
import nl.tweeenveertig.openstack.client.impl.ClientImpl;
import nl.tweeenveertig.openstack.client.mock.ClientMock;

public class AccountFactory {

    private AccountConfig config;

    public void setConfig(AccountConfig config) {
        this.config = config;
    }

    public Account createAccount() {
        Client client;
        if (config.isMock()) {
            client = new ClientMock().setAllowEveryone(true);
        } else {
            client = new ClientImpl();
        }
        return client.authenticate(config.getTenant(), config.getUsername(), config.getPassword(), config.getAuthUrl());
    }
}
