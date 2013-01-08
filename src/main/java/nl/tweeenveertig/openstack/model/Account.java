package nl.tweeenveertig.openstack.model;

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
public interface Account extends ObjectStoreEntity {

    /**
    * Returns all the containers in an Account.
    * @return the containers in an Account
    */
    public Collection<Container> listContainers();

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

    public int getContainerCount();
    public long getBytesUsed();
    public int getObjectCount();

    public Account setAllowReauthenticate(boolean allowReauthenticate);
    public boolean isAllowReauthenticate();
}
