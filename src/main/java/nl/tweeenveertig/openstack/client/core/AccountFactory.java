package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.client.Account;

public abstract class AccountFactory<A extends Account> {

    protected String tenant;
    protected String username;
    protected String password;
    protected String authUrl;

    public abstract Account getAccount();

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }
}
