package org.javaswift.joss.model;

import java.util.Collection;

/**
* ListHolder instances hold lists of child entities. In the current model, only Account and Container
* are ListHolders.
* @param <Child> the entity that is held by the ListHolder
*/
public interface ListHolder<Child extends ListSubject> {

    /**
    * Returns all the containers in an Account. Note that this method returns a maxmimum of 9,999
    * Containers and no more (see http://docs.openstack.org/api/openstack-object-storage/1.0/content/list-objects.html)
    * @return the containers in an Account
    */
    public Collection<Child> list();

    /**
    * Returns a number equal to pageSize of Container elements, starting with the first element
    * after the Container named the same as marker.
    * @param prefix show only the results starting with prefix
    * @param marker the last element on the previous page
    * @param pageSize the number of elements to return
    * @return page of Containers in an Account with a total of pageSize elements
    */
    public Collection<Child> list(String prefix, String marker, int pageSize);

    /**
    * Returns a number equal to pageSize of Container elements, starting with the first element
    * after the Container named the same as marker.
    * @param paginationMap the map that is fetched with getPaginationMap and maps from page to marker
    * @param page the page to return the Containers for
    * @return page of Containers in an Account with a total of pageSize elements
    */
    public Collection<Child> list(PaginationMap paginationMap, int page);

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
    * Returns a PaginationMap of a listing of Containers. The map can be used to supply the OpenStack
    * API with a marker (ie, last record on the previous page) and a limit (ie, page size).
    * BE AWARE: this method iterates over ALL Container names to draw up the map, therefore it must
    * be considered an expensive call.
    * @param prefix the names must start with the prefix or else will be filtered out
    * @param pageSize number of elements on a single page
    * @return the pagination map for all the Container elements in Account
    */
    public PaginationMap getPaginationMap(String prefix, int pageSize);

    /**
    * The number of child entities (ListSubjects) that are being held by the ListHolder
    * @return number of child entities
    */
    public int getCount();

    /**
    * The ObjectStore server will force a maximum page size. This value helps to determine blocks
    * that must be read, which is used internally for setting up a pagination map.
    * @return maximum page size for the child entity
    */
    public int getMaxPageSize();

}
