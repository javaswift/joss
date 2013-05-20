package org.javaswift.joss.model;

import org.javaswift.joss.command.shared.identity.tenant.Tenants;

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
    public Container getContainer(String name);

    /**
    * Returns a handle for a website. Note that this method DOES NOT create a website in the Object Store
    * or otherwise place a call to the Object Store
    * @param name name of the website to create a handle for
    * @return the website handle
    */
    public Website getWebsite(String name);

    /**
    * Trigger the authentication against Object Store. There are two use cases for this method. The first is
    * triggered pro-actively by the user by calling authenticate on the client. The second is when the token
    * has expired and AbstractSecureCommand triggers a re-authentication.
    * @return the access element including a new token
    */
    public Access authenticate();

    /**
    * Returns the public URL which is used for the underlying stored objects
    * @return the public URL of the underlying stored objects
    */
    public String getPublicURL();

    /**
    * Returns the private URL which is used for the underlying stored objects
    * @return the private URL of the underlying stored objects
    */
    public String getPrivateURL();

    /**
    * Force the Account to reload its metadata
    */
    public void reload();

    /**
    * The number of bytes stored by the StoredObjects in all Containers in the Account.
    * @return number of bytes
    */
    public long getBytesUsed();

    /**
    * The number of StoredObjects in all Containers in the Account.
    * @return number of StoredObjects
    */
    public int getObjectCount();

    /**
    * An ObjectStore authentication token will expire after 24 hours. In a long-living web application,
    * the account instance may exist longer than that. If reauthentication is allowed, a new token will
    * be fetched once the existing one has expired.
    * @param allowReauthenticate whether reauthentication is allowed
    * @return instance of Account
    */
    public Account setAllowReauthenticate(boolean allowReauthenticate);

    /**
    * Set your own public host to prefix the public URLs of objects
    * @param host host of an object's public URL
    * @return instance of Account
    */
    public Account setPublicHost(String host);

    /**
    * Set your own private host to prefix the public URLs of objects
    * @param host host of an object's private URL
    * @return instance of Account
    */
    public Account setPrivateHost(String host);

    /**
    * If ContainerCaching is enabled, Account will keep track of its Container instances and reuse them.
    */
    public Account setAllowContainerCaching(boolean containerCaching);

    /**
    * Empties the Container Cache
    */
    public void resetContainerCache();

    /**
    * Whether reauthentication on expiration of the authentication token is allowed, or this is done
    * manually by the client
    * @return whether reauthentication is allowed
    */
    public boolean isAllowReauthenticate();

    /**
    * Increase the call counter which tracks how many calls are made to the ObjectStore
    */
    public void increaseCallCounter();

    /**
    * Returns the number of HTTP calls that have been made to the ObjectStore server. This could be
    * useful to check the efficiency of the methods and configuration in use
    * @return number of calls made to the ObjectStore server
    */
    public int getNumberOfCalls();

    /**
    * Returns the original host, regardless of the configured public and private host settings
    * @return original public host
    */
    public String getOriginalHost();

    /**
    * Returns the tenants of the account. Note that this is the only Account method that interacts with the object
    * store and will work when no tenant has been set. All others will throw an exception.
    * @return the tenants belonging to the Account
    */
    public Tenants getTenants();

    /**
    * Checks whether a tenant ID and/or name were supplied. If not, the account can only be used to retrieve
    * a list of tenants.
    */
    public boolean isTenantSupplied();

    /**
    * Sets the password on the account that will be used to create server side hashes. This is required for
    * TempURL (both GET and PUT). The server will match a generated hash against the hash that is passed in a
    * tempURL. If identical, it passed the first test. Note that if password is not set, TempURLs will not work.
    * @param password the password to use for generating the hashes
    */
    public void setHashPassword(String password);

}
