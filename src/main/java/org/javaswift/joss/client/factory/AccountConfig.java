package org.javaswift.joss.client.factory;

public class AccountConfig {

    /**
    * ObjectStore tenant. This is most often equated with the project
    */
    private String tenant;

    /**
    * The ObjectStore username
    */
    private String username;

    /**
    * The ObjectStore password
    */
    private String password;

    /**
    * The ObjectStore authentication URL. This is a reference to Keystone.
    */
    private String authUrl;

    /**
    * ObjectStore tokens expire after 24 hours. If reauthentication is allowed (default behaviour),
    * after expiration, Account will reauthenticate itself and get a new authentication token. If
    * reauthentication is turned off, you will have to manually arrange reauthentication. This mode
    * is recommended for web servers or otherwise long-living processes.
    */
    private boolean allowReauthenticate = true;

    /**
    * When caching is allowed (default behaviour), information is not fetched from the server when
    * it is available in an object. When it is turned off, EVERY bit of information results in a
    * call to the ObjectStore. It is possible to manually force a cache refresh by calling reload
    * on an entity.
    */
    private boolean allowCaching = true;

    /**
    * Keystone returns a public host where the images can be fetched. In real mode, you can override
    * this with your own host (eg, a CNAME reference to the public host of the ObjectStore). In mock
    * mode, you can also pass your own host. This could be your own servlet which fetches content
    * from mock Swift, simulating a public URL. By default, the public URL returned by Keystone
    * will be used.
    */
    private String host = null;

    /**
    * If mock is turned off (default behaviour), then the real ObjectStore will be accessed. If not,
    * an in-memory simulation of Swift will be used. It can be accessed as if it were a real
    * ObjectStore.
    */
    private boolean mock = false;

    /**
    * Every call to the ObjectStore will be delayed by the designated number of milliseconds. The
    * delay is fulfilled by executing Thread.sleep. MOCK only.
    */
    private int mockMillisDelay = 0;

    /**
    * An ObjectDeleter keeps tracks of removals planned for the future (DeleteAt/DeleteAfter). It
    * makes sure the objects are removed at that time. Note that ObjectDeleter runs as a separate
    * thread. If no ObjectDeleter is allowed, DeleteAt and DeleteAfter will be executed right
    * away. MOCK only.
    */
    private boolean mockAllowObjectDeleter = true;

    /**
    * If everyone is allowed in (default behaviour), no authentication check is made. If this
    * setting is false, the check will be made against a custom user store.
    */
    private boolean mockAllowEveryone  = true;

    /**
    * It is possible to pass a folder which must be loaded as an ObjectStore into the in-memory
    * representation of the ObjectStore.
    */
    private String mockOnFileObjectStore = null;

    /**
    * Socket timeout in milliseconds, ie the allowable idle time between packets. Default this will
    * not timeout. Note that this value will not be used if a custom HttpClient is supplied.
    * See also: http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/org/apache/http/params/CoreConnectionPNames.html#SO_TIMEOUT
    */
    private int socketTimeout = -1;

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

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
