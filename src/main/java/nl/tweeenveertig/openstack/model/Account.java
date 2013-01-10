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
    * Returns all the containers in an Account. Note that this method returns a maxmimum of 9,999
    * Containers and no more (see http://docs.openstack.org/api/openstack-object-storage/1.0/content/list-objects.html)
    * @return the containers in an Account
    */
    public Collection<Container> listContainers();

    /**
    * Returns a number equal to pageSize of Container elements, starting with the first element
    * after the Container named the same as marker.
    * @param marker the last element on the previous page
    * @param pageSize the number of elements to return
    * @return page of Containers in an Account with a total of pageSize elements
    */
    public Collection<Container> listContainers(String marker, int pageSize);

    /**
    * Returns a number equal to pageSize of Container elements, starting with the first element
    * after the Container named the same as marker.
    * @param paginationMap the map that is fetched with getPaginationMap and maps from page to marker
    * @param page the page to return the Containers for
    * @return page of Containers in an Account with a total of pageSize elements
    */
    public Collection<Container> listContainers(PaginationMap paginationMap, int page);

    /**
    * Returns a PaginationMap of a listing of Containers. The map can be used to supply the OpenStack
    * API with a marker (ie, last record on the previous page) and a limit (ie, page size).
    * BE AWARE: this method iterates over ALL Container names to draw up the map, therefore it must
    * be considered an expensive call.
    * @param pageSize number of elements on a single page
    * @return the pagination map for all the Container elements in Account
    */
    public PaginationMap getPaginationMap(int pageSize);

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
    public int getMaxPageSize();

    public Account setAllowReauthenticate(boolean allowReauthenticate);
    public boolean isAllowReauthenticate();
}
