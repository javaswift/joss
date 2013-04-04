package org.javaswift.joss.client.factory;

public class AccountConfig {

    private String tenant;
    private String username;
    private String password;
    private String authUrl;
    private boolean allowReauthenticate = true;
    private boolean allowCaching = true;
    private String host = null;
    private boolean mock = false;
    private int mockMillisDelay = 0;
    private boolean mockAllowObjectDeleter = true;
    private boolean mockAllowEveryone  = true;
    private String mockOnFileObjectStore = null;

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getTenant() {
        return tenant;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setMock(boolean mock) {
        this.mock = mock;
    }

    public boolean isMock() {
        return mock;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getMockMillisDelay() {
        return mockMillisDelay;
    }

    public void setMockMillisDelay(int mockMillisDelay) {
        this.mockMillisDelay = mockMillisDelay;
    }

    public boolean isAllowReauthenticate() {
        return allowReauthenticate;
    }

    public void setAllowReauthenticate(boolean allowReauthenticate) {
        this.allowReauthenticate = allowReauthenticate;
    }

    public boolean isAllowCaching() {
        return allowCaching;
    }

    public void setAllowCaching(boolean allowCaching) {
        this.allowCaching = allowCaching;
    }

    public boolean isMockAllowObjectDeleter() {
        return mockAllowObjectDeleter;
    }

    public void setMockAllowObjectDeleter(boolean mockAllowObjectDeleter) {
        this.mockAllowObjectDeleter = mockAllowObjectDeleter;
    }

    public boolean isMockAllowEveryone() {
        return mockAllowEveryone;
    }

    public void setMockAllowEveryone(boolean mockAllowEveryone) {
        this.mockAllowEveryone = mockAllowEveryone;
    }

    public String getMockOnFileObjectStore() {
        return mockOnFileObjectStore;
    }

    public void setMockOnFileObjectStore(String mockOnFileObjectStore) {
        this.mockOnFileObjectStore = mockOnFileObjectStore;
    }
}
