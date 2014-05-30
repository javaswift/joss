package org.javaswift.joss.client.factory;

import static org.javaswift.joss.client.factory.AuthenticationMethod.*;

public class AccountConfig {

    /**
    * ObjectStore tenant name. This is most often equated with the project
    */
    private String tenantName;

    /**
    * ObjectStore tenant ID. This is most often equated with the project
    */
    private String tenantId;

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
    * The hash for a TempURL is based on the object path which consists of the the pure path to an
    * object. Eg, /v1/AUTH_Account/Container/Object. The first part ("/v1/AUTH_Account") must be
    * derived from one of the URLs in an Endpoint. By default this is the Public URL. However, sometimes
    * the public URL is beautified, hiding the /v1 path. In this case, the URL must be derived from the
    * Internal or Admin URL. Options:
    * <ul>
    *     <li>PUBLIC_URL_PATH (default)</li>
    *     <li>INTERNAL_URL_PATH</li>
    *     <li>ADMIN_URL_PATH</li>
    * </ul>
    */
    private TempUrlHashPrefixSource tempUrlHashPrefixSource = TempUrlHashPrefixSource.PUBLIC_URL_PATH;

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
    private String publicHost = null;

    /**
    * Since for content in private containers, you will need a Keystone authentication token, this
    * cannot be fetched in the same way as content in public containers. A good practice is to use
    * the application server as a proxy for the ObjectStore. In this case, the application server
    * could choose to add a controller method which mimicks the ObjectStore REST API for downloading
    * content. By setting privateHost, you can facilitate this approach.
    */
    private String privateHost = null;

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
    * Allow absolute paths for the mockOnFileObjectStore. The web application can then request
    * the real path (getRealPath) from the ServletContext and pass this.
    */
    private boolean mockOnFileObjectStoreIsAbsolutePath = false;

    /**
    * Socket timeout in milliseconds, ie the allowable idle time between packets. Default this will
    * not timeout. Note that this value will not be used if a custom HttpClient is supplied.
    * See also: http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/org/apache/http/params/CoreConnectionPNames.html#SO_TIMEOUT
    */
    private int socketTimeout = -1;

    /**
    * If ContainerCaching is enabled, Account will keep track of its Container instances and reuse them. By default
    * container caching is enabled.
    */
    private boolean allowContainerCaching = true;

    /**
    * The preferred region will determine what Swift end-point will be chosen. If no preferred region is
    * set (default), the first applicable end-point will be selected.
    */
    private String preferredRegion;

    /**
    * The hash password is used to create hashes for the TempURL (both GET and PUT), which is used to compose a public
    * URL that can be used to fetch the object. The server uses the same hash password to create (hopefully) the same
    * hash. Comparison of these hashes is the first test to see if the TempURL is valid. Note that the ObjectStore is
    * notified of the hash password to use by invoking Account.saveHashPassword().
    */
    private String hashPassword;
    
    /**
    * In development environments, self-signed SSL certificates may be used in place of
    * certificates from a registered Certificate Authority.  Also, if you are connecting
    * with SSL to an IP address instead of a hostname, the name on the certificate
    * will not be validated.  You can bypass these checks by setting this to true.
    * <em>This is not suitable for production use</em> since it is trivial to implement
    * a man-in-the-middle attack if the SSL certificate is not strongly validated.
    */
    private boolean disableSslValidation = false;

    /**
    * The delimiter is used to check for directory boundaries. The default will be a '/'.
    */
    private Character delimiter = '/';

    /**
     * The method of authentication. Various options:
     * <ul>
     *     <li>
     *         <b>BASIC</b>; authenticate against Swift itself. Authentication URL, username and password
     *         must be passed.
     *     </li>
     *     <li>
     *         <b>TEMPAUTH</b>; authenticate against Swift itself. Authentication URL, username and password
     *         must be passed.
     *     </li>
     *     <li>
     *         <b>KEYSTONE</b> (default); makes use of OpenStack Compute. Authentication URL, username and
     *         password must be passed. Ideally, tenant ID and/or name are passed as well. JOSS can auto-
     *         discover the tenant if none is passed and if it can be resolved (one tenant for user).
     *     </li>
	 *     <li>
	 *         <b>EXTERNAL</b>; an implementation of the interface AccessProvider must be provided.
	 *     </li>
     * </ul>
     */
    private AuthenticationMethod authenticationMethod = KEYSTONE;
    
    private AuthenticationMethod.AccessProvider accessProvider = null ;

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return this.tenantId;
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

    public String getPublicHost() {
        return publicHost;
    }

    public void setPublicHost(String publicHost) {
        this.publicHost = publicHost;
    }

    public String getPrivateHost() {
        return privateHost;
    }

    public void setPrivateHost(String privateHost) {
        this.privateHost = privateHost;
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

    public boolean isAllowContainerCaching() {
        return allowContainerCaching;
    }

    public void setAllowContainerCaching(boolean allowContainerCaching) {
        this.allowContainerCaching = allowContainerCaching;
    }

    public String getPreferredRegion() {
        return preferredRegion;
    }

    public void setPreferredRegion(String preferredRegion) {
        this.preferredRegion = preferredRegion;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public TempUrlHashPrefixSource getTempUrlHashPrefixSource() {
        return tempUrlHashPrefixSource;
    }

    public void setTempUrlHashPrefixSource(TempUrlHashPrefixSource tempUrlHashPrefixSource) {
        this.tempUrlHashPrefixSource = tempUrlHashPrefixSource;
    }

    public void setTempUrlHashPrefixSource(String tempUrlHashPrefixSource) {
        setTempUrlHashPrefixSource(TempUrlHashPrefixSource.valueOf(tempUrlHashPrefixSource));
    }

    public boolean isDisableSslValidation() {
        return disableSslValidation;
    }

    public void setDisableSslValidation(boolean disableSslValidation) {
        this.disableSslValidation = disableSslValidation;
    }

    public AuthenticationMethod getAuthenticationMethod() {
        return authenticationMethod;
    }
    
    public void setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
    }
    
    public AuthenticationMethod.AccessProvider getAccessProvider () {
    	return accessProvider ;
    }

    public void setAccessProvider (AuthenticationMethod.AccessProvider accessProvider) {
    	this.accessProvider = accessProvider ;
    }

    public void setAuthenticationMethod(String authenticationMethod) {
        setAuthenticationMethod(AuthenticationMethod.valueOf(authenticationMethod));
    }

    public Character getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(Character delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isMockOnFileObjectStoreIsAbsolutePath() {
        return mockOnFileObjectStoreIsAbsolutePath;
    }

    public void setMockOnFileObjectStoreIsAbsolutePath(boolean mockOnFileObjectStoreIsAbsolutePath) {
        this.mockOnFileObjectStoreIsAbsolutePath = mockOnFileObjectStoreIsAbsolutePath;
    }
}
