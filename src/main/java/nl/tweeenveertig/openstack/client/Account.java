package nl.tweeenveertig.openstack.client;

import nl.tweeenveertig.openstack.client.core.ObjectStoreEntity;

import java.util.Collection;

public interface Account extends ObjectStoreEntity {

    /**
    * Returns all the containers in an Account.
    * @return the containers in an Account
    */
    public Collection<Container> listContainers();

    /**
    * Returns a handle for a container. Note that this method DOES NOT create a container in the Object Store
    * @param name name of the container to create a handle for
    * @return the container handle
    */
    public Container getContainer(String name);

    public int getContainerCount();
    public long getBytesUsed();
    public int getObjectCount();
}
