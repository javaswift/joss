package org.javaswift.joss.client.factory;

public class AccountConfig {

    private String tenant;
    private String username;
    private String password;
    private String authUrl;
    private boolean mock;
    private String mockPublicUrl;
    private int mockMillisDelay = 0;

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

    public String getMockPublicUrl() {
        return mockPublicUrl;
    }

    public void setMockPublicUrl(String mockPublicUrl) {
        this.mockPublicUrl = mockPublicUrl;
    }

    public int getMockMillisDelay() {
        return mockMillisDelay;
    }

    public void setMockMillisDelay(int mockMillisDelay) {
        this.mockMillisDelay = mockMillisDelay;
    }
}
