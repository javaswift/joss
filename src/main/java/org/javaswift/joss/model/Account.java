package org.javaswift.joss.model;

import org.javaswift.joss.command.shared.identity.bulkdelete.BulkDeleteResponse;
import org.javaswift.joss.command.shared.identity.tenant.Tenants;

import java.util.Collection;

/**
 * Account is the root entity in Object Store. It allows you access to the various containers underneath. Note
 * that you need to call {@link #getContainer(String) getContainer} to work on Containers. The method returns
 * a stub for dealing with Containers, but does not create a container in the Object Store. The creation only
 * takes place when you run the {@link Container#create() create} method on a
 * Container. Information on the container will not be retrieved until the time you actually call on that
 * information - ie, information is lazily loaded.
 * @author Robert Bor
 */
public interface Account extends ObjectStoreEntity, ListHolder<Container> {

    /**
    * Returns a handle for a container. Note that this method DOES NOT create a container in the Object Store
    * or otherwise place a call to the Object Store
    * @param name name of the container to create a handle for
    * @return the container handle
    */
    Container getContainer(String name);

    /**
    * Returns a handle for a website. Note that this method DOES NOT create a website in the Object Store
    * or otherwise place a call to the Object Store
    * @param name name of the website to create a handle for
    * @return the website handle
    */
    Website getWebsite(String name);

    /**
    * Trigger the authentication against Object Store. There are two use cases for this method. The first is
    * triggered pro-actively by the user by calling authenticate on the client. The second is when the token
    * has expired and AbstractSecureCommand triggers a re-authentication.
    * @return the access element including a new token
    */
    Access authenticate();

    /**
    * Obtain the Access interface
    * @return the access element including the existing token
    */
    Access getAccess();

    /**
    * Returns the URL which is used for the underlying stored objects
    * @return the URL of the underlying stored objects
    */
    String getPublicURL();

    /**
    * Returns the private URL which is used for the underlying stored objects
    * @return the private URL of the underlying stored objects
    */
    String getPrivateURL();

    /**
    * Force the Account to reload its metadata
    */
    void reload();

    /**
    * The number of bytes stored by the StoredObjects in all Containers in the Account.
    * @return number of bytes
    */
    long getBytesUsed();

    /**
    * The number of StoredObjects in all Containers in the Account.
    * @return number of StoredObjects
    */
    int getObjectCount();

    /**
    * Returns the server time in milliseconds since 1970
    * @return server time
    */
    long getServerTime();

    /**
    * Compares the local time to the server time and maintains a number of milliseconds to account for the
    * difference. This number is taken into account when an absolute expiry time is passed to the server.
    */
    void synchronizeWithServerTime();

    /**
    * Calculate the server time taking into account the number of seconds passed. This method makes use
    * of the server time modifier which is calculated in synchronizeWithServerTime().
    * @param seconds number of seconds after the actual server time
    * @return calculated server time, including the number of seconds in the future
    */
    long getActualServerTimeInSeconds(long seconds);

    /**
    * An ObjectStore authentication token will expire after 24 hours. In a long-living web application,
    * the account instance may exist longer than that. If reauthentication is allowed, a new token will
    * be fetched once the existing one has expired.
    * @param allowReauthenticate whether reauthentication is allowed
    * @return instance of Account
    */
    Account setAllowReauthenticate(boolean allowReauthenticate);

    /**
    * Set your own host to prefix the URLs of objects
    * @param host host of an object's URL
    * @return instance of Account
    */
    Account setPublicHost(String host);

    /**
    * Set your own private host to prefix the URLs of objects
    * @param host host of an object's private URL
    * @return instance of Account
    */
    Account setPrivateHost(String host);

    /**
    * If ContainerCaching is enabled, Account will keep track of its Container instances and reuse them.
    * @param containerCaching whether container caching is allowed
    * @return instance of Account
    */
    Account setAllowContainerCaching(boolean containerCaching);

    /**
    * Saves the password to the Account. The password will be used to create server side hashes. This is required for
    * TempURL (both GET and PUT). The server will match a generated hash against the hash that is passed in a
    * tempURL. If identical, it passed the first test. Note that if password is not set, TempURLs will not work.
    * Note that the password saved is the one set either in AccountConfig, or set with Account.setHashPassword(String).
    * @param hashPassword the password to use for generating the hashes
    * @return instance of Account
    */
    Account setHashPassword(String hashPassword);

    /**
    * Returns the hash password originally set on Account.
    * @return hash password
    */
    String getHashPassword();

    /**
    * Empties the Container Cache
    */
    void resetContainerCache();

    /**
    * Whether reauthentication on expiration of the authentication token is allowed, or this is done
    * manually by the client
    * @return whether reauthentication is allowed
    */
    boolean isAllowReauthenticate();

    /**
    * Increase the call counter which tracks how many calls are made to the ObjectStore
    */
    void increaseCallCounter();

    /**
    * Returns the number of HTTP calls that have been made to the ObjectStore server. This could be
    * useful to check the efficiency of the methods and configuration in use
    * @return number of calls made to the ObjectStore server
    */
    int getNumberOfCalls();

    /**
    * Returns the original host, regardless of the configured and private host settings
    * @return original host
    */
    String getOriginalHost();

    /**
    * Returns the tenants of the account. Note that this is the only Account method that interacts with the object
    * store and will work when no tenant has been set. All others will throw an exception.
    * @return the tenants belonging to the Account
    */
    Tenants getTenants();

    /**
    * Checks whether a tenant ID and/or name were supplied. If not, the account can only be used to retrieve
    * a list of tenants.
    * @return whether the tenant has been configured
    */
    boolean isTenantSupplied();

    /**
     * Returns the preferred region of the account. 
     * @return the account's preferred region
     */
    String getPreferredRegion();

    /**
     * Set the preferred region for the account.
     * @param preferredRegion the preferred region for the account
     * @return instance of Account
     */
    Account setPreferredRegion(String preferredRegion);

    /**
     * Deletes a list of objects or containers in a single operation. ObjectIdentifier may be both,
     * an object or a container.
     *
     * Note: Make sure that the container is empty. If it contains objects, Object Storage cannot
     * delete the container.
     *
     * The operation will only throw an exception, if the operation failed completely. To make sure
     * all containers/objects were deleted successfully, have a look at the response object.
     *
     * @param objectsToDelete the objects to delete
     * @return as the whole operation may complete successfully with some sub-operations having
     *         failed, this response object contains the details, what was deleted and which errors
     *         did occur
     */
    BulkDeleteResponse bulkDelete(Collection<ObjectIdentifier> objectsToDelete);
}
