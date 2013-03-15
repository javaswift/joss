package org.javaswift.joss.client.impl;

import org.javaswift.joss.command.identity.access.AccessImpl;
import org.javaswift.joss.model.Client;
import org.javaswift.joss.command.identity.AuthenticationCommand;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientImpl implements Client<AccountImpl> {

    public static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);

    private HttpClient httpClient;

    private boolean allowCaching = true;

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
        LOG.info("JOSS / Attempting authentication with tenant: "+tenant+", username: "+username+", Auth URL: "+authUrl);
        AuthenticationCommand command = new AuthenticationCommand(httpClient, authUrl, tenant, username, password);
        AccessImpl access = command.call();
        LOG.info("JOSS / Successfully authenticated");
        access.setPreferredRegion(preferredRegion);
        LOG.info("JOSS / Applying preferred region: "+(preferredRegion == null ? "none" : preferredRegion));
        return new AccountImpl(command, httpClient, access, allowCaching);
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        LOG.info("JOSS / Use HTTP client set by client");
    }

    public void setAllowCaching(boolean allowCaching) {
        this.allowCaching = allowCaching;
        LOG.info("JOSS / Allow caching: "+allowCaching);
    }

}
