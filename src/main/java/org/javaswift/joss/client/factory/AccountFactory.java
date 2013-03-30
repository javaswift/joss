package org.javaswift.joss.client.factory;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Client;
import org.javaswift.joss.client.impl.ClientImpl;
import org.javaswift.joss.client.mock.ClientMock;
import org.apache.http.client.HttpClient;

public class AccountFactory {

    private AccountConfig config;

    private HttpClient httpClient;

    private boolean allowReauthenticate = true; // Default behaviour is to allow re-authentication

    private boolean allowCaching = true;

    public void setConfig(AccountConfig config) {
        this.config = config;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setAllowReauthenticate(boolean allowReauthenticate) {
        this.allowReauthenticate = allowReauthenticate;
    }

    public void setAllowCaching(boolean allowCaching) {
        this.allowCaching = allowCaching;
    }

    public Account createAccount() {
        Client client;
        if (config.isMock()) {
            client = new ClientMock()
                    .setAllowEveryone(true)
                    .setMillisDelay(config.getMockMillisDelay())
                    .setPublicUrl(config.getMockPublicUrl());
        } else {
            client = createClientImpl();
        }
        return client
                .authenticate(config.getTenant(), config.getUsername(), config.getPassword(), config.getAuthUrl())
                .setAllowReauthenticate(this.allowReauthenticate);
    }

    protected ClientImpl createClientImpl() {
        ClientImpl client = new ClientImpl();
        if (this.httpClient != null) {
            client.setHttpClient(this.httpClient);
        }
        client.setAllowCaching(this.allowCaching);
        return client;
    }
}
