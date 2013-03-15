package org.javaswift.joss.model;

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

}
