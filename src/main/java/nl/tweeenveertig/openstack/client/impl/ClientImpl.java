package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.model.Client;
import nl.tweeenveertig.openstack.command.identity.AuthenticationCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public class ClientImpl implements Client<AccountImpl> {

    private HttpClient httpClient;

    public ClientImpl() {
        initHttpClient();
    }

    private void initHttpClient() {
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(25);
        this.httpClient = new DefaultHttpClient(connectionManager);
    }

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
