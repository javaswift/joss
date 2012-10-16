package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Client;
import nl.tweeenveertig.openstack.command.identity.AuthenticationCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class ClientImpl implements Client<AccountImpl> {

    private HttpClient httpClient = new DefaultHttpClient();

    public AccountImpl authenticate(String tenant, String username, String password, String authUrl) {
        return authenticate(tenant, username, password, authUrl, null);
    }

    public AccountImpl authenticate(String tenant, String username, String password, String authUrl, String preferredRegion) {
        AuthenticationCommand command = new AuthenticationCommand(httpClient, authUrl, tenant, username, password);
        Access access = command.call();
        access.setPreferredRegion(preferredRegion);
        return new AccountImpl(command, httpClient, access);
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
