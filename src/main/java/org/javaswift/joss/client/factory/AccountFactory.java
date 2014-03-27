package org.javaswift.joss.client.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.impl.ClientImpl;
import org.javaswift.joss.client.mock.ClientMock;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Client;

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
        return client.authenticate();
    }

    public Client createClientMock() {
        return new ClientMock(config);
    }

    public Client createClientImpl() {
        return new ClientImpl(config)
                .setHttpClient(this.httpClient);
    }

    public AccountFactory setTenantName(String tenantName) {
        this.config.setTenantName(tenantName);
        return this;
    }

    public AccountFactory setTenantId(String tenantId) {
        this.config.setTenantId(tenantId);
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

    public AccountFactory setPublicHost(String publicHost) {
        this.config.setPublicHost(publicHost);
        return this;
    }

    public AccountFactory setPrivateHost(String privateHost) {
        this.config.setPrivateHost(privateHost);
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

    public AccountFactory setAllowContainerCaching(boolean allowContainerCaching) {
        this.config.setAllowContainerCaching(allowContainerCaching);
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

    public AccountFactory setPreferredRegion(String preferredRegion) {
        this.config.setPreferredRegion(preferredRegion);
        return this;
    }

    public AccountFactory setHashPassword(String hashPassword) {
        this.config.setHashPassword(hashPassword);
        return this;
    }

    public AccountFactory setTempUrlHashPrefixSource(TempUrlHashPrefixSource source) {
        this.config.setTempUrlHashPrefixSource(source);
        return this;
    }

    public AccountFactory setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
        this.config.setAuthenticationMethod(authenticationMethod);
        return this;
    }

    public AccountFactory setDelimiter(Character delimiter) {
        this.config.setDelimiter(delimiter);
        return this;
    }

    public AccountFactory setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

}
