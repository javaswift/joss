package org.javaswift.joss.client.factory;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Client;
import org.javaswift.joss.client.impl.ClientImpl;
import org.javaswift.joss.client.mock.ClientMock;
import org.apache.http.client.HttpClient;

public class AccountFactory {

    private final AccountConfig config;

    private HttpClient httpClient;

    public AccountFactory() {
        this(new AccountConfig());
    }

    public AccountFactory(AccountConfig config) {
        this.config = config;
    }

    public Account createAccount() {
        final Client client;
        if (config.isMock()) {
            client = createClientMock();
        } else {
            client = createClientImpl();
        }
        return client
                .authenticate(config.getTenant(), config.getUsername(), config.getPassword(), config.getAuthUrl())
                .setHost(config.getHost())
                .setAllowReauthenticate(config.isAllowReauthenticate());
    }

    public Client createClientMock() {
        return new ClientMock()
                .setOnFileObjectStore(config.getMockOnFileObjectStore())
                .setAllowObjectDeleter(config.isMockAllowObjectDeleter())
                .setAllowEveryone(config.isMockAllowEveryone())
                .setMillisDelay(config.getMockMillisDelay());
    }

    public Client createClientImpl() {
        return new ClientImpl(config.getSocketTimeout())
                .setHttpClient(this.httpClient)
                .setAllowCaching(config.isAllowCaching());
    }

    public AccountFactory setTenant(String tenant) {
        this.config.setTenant(tenant);
        return this;
    }

    public AccountFactory setUsername(String username) {
        this.config.setUsername(username);
        return this;
    }

    public AccountFactory setPassword(String password) {
        this.config.setPassword(password);
        return this;
    }

    public AccountFactory setAuthUrl(String authUrl) {
        this.config.setAuthUrl(authUrl);
        return this;
    }

    public AccountFactory setMock(boolean mock) {
        this.config.setMock(mock);
        return this;
    }

    public AccountFactory setHost(String host) {
        this.config.setHost(host);
        return this;
    }

    public AccountFactory setMockMillisDelay(int mockMillisDelay) {
        this.config.setMockMillisDelay(mockMillisDelay);
        return this;
    }

    public AccountFactory setAllowReauthenticate(boolean allowReauthenticate) {
        this.config.setAllowReauthenticate(allowReauthenticate);
        return this;
    }

    public AccountFactory setAllowCaching(boolean allowCaching) {
        this.config.setAllowCaching(allowCaching);
        return this;
    }

    public AccountFactory setMockAllowObjectDeleter(boolean mockAllowObjectDeleter) {
        this.config.setMockAllowObjectDeleter(mockAllowObjectDeleter);
        return this;
    }

    public AccountFactory setMockAllowEveryone(boolean mockAllowEveryone) {
        this.config.setMockAllowEveryone(mockAllowEveryone);
        return this;
    }

    public AccountFactory setMockOnFileObjectStore(String mockOnFileObjectStore) {
        this.config.setMockOnFileObjectStore(mockOnFileObjectStore);
        return this;
    }

    public AccountFactory setSocketTimeout(int socketTimeout) {
        this.config.setSocketTimeout(socketTimeout);
        return this;
    }

    public AccountFactory setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

}
