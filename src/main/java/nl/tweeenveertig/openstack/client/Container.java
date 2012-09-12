package nl.tweeenveertig.openstack.client;

import nl.tweeenveertig.openstack.client.core.ObjectStoreEntity;

import java.util.Collection;

public interface Container extends ObjectStoreEntity {

    /**
    * Takes a single Container and makes it public. ALL (!) the objects in the Container are now public, so be
    * very careful exercising this option. The public URL can now be used to access objects.
    */
    public void makePublic();

    /**
    * Takes a single Container and makes it private. All the objects in the Container are now private and can
    * no longer be accessed through the public URL.
    */
    public void makePrivate();

    /**
    * Lists all the objects in a Container
    * @return the objects in the Container
    */
    public Collection<StoredObject> listObjects();

    /**
    * Creates a Container in the Account.
    */
    public void create();

    /**
    * Deletes a Container from the Account. Note that the Container must be empty in order to be deleted.
    */
    public void delete();

    /**
    * Checks whether a container exists
    * @return whether the container exists
    */
    public boolean exists();

    /**
    * Returns a handle for an object. Note that this method DOES NOT create an object in the Object Store
    * @param name name of the object to create a handle for
    * @return the object handle
    */
    public StoredObject getObject(String name);

    public int getObjectCount();
    public long getBytesUsed();
    public boolean isPublic();
    public String getName();

    public abstract Account getAccount();

}
