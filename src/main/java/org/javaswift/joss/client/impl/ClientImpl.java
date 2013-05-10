package org.javaswift.joss.client.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.javaswift.joss.command.impl.factory.AuthenticationCommandFactoryImpl;
import org.javaswift.joss.command.shared.factory.AuthenticationCommandFactory;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientImpl implements Client<AccountImpl> {

    public static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);

    private HttpClient httpClient;

    private boolean allowCaching = true;

    private AuthenticationCommandFactory commandFactory = new AuthenticationCommandFactoryImpl();

    public ClientImpl(int socketTimeout) {
        initHttpClient(socketTimeout);
    }

    public ClientImpl() {
        initHttpClient(-1);
    }

    private void initHttpClient(int socketTimeout) {
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(25);
        this.httpClient = new DefaultHttpClient(connectionManager);
        if (socketTimeout != -1) {
            LOG.info("JOSS / Set socket timeout on HttpClient: "+socketTimeout);
            HttpParams params = this.httpClient.getParams();
            HttpConnectionParams.setSoTimeout(params, socketTimeout);
        }
    }

    public AccountImpl authenticate(String tenantName, String tenantId, String username, String password, String authUrl) {
        return authenticate(tenantName, tenantId, username, password, authUrl, null);
    }

    public AccountImpl authenticate(String tenantName, String tenantId, String username, String password, String authUrl, String preferredRegion) {
        LOG.info("JOSS / Attempting authentication with tenant name: "+tenantName+", tenant ID: "+tenantId+", username: "+username+", Auth URL: "+authUrl);
        AuthenticationCommand command = commandFactory.createAuthenticationCommand(httpClient, authUrl, tenantName, tenantId, username, password);
        AccessImpl access = command.call();
        LOG.info("JOSS / Successfully authenticated");
        access.setPreferredRegion(preferredRegion);
        LOG.info("JOSS / Applying preferred region: "+(preferredRegion == null ? "none" : preferredRegion));
        return new AccountImpl(command, httpClient, access, allowCaching);
    }

    public ClientImpl setHttpClient(HttpClient httpClient) {
        if (httpClient != null) {
            LOG.info("JOSS / Use HTTP client set by client (overrides previous HttpClient settings)");
            this.httpClient = httpClient;
        }
        return this;
    }

    public ClientImpl setAllowCaching(boolean allowCaching) {
        LOG.info("JOSS / Allow caching: "+allowCaching);
        this.allowCaching = allowCaching;
        return this;
    }

}
