package nl.tweeenveertig.openstack.client;

import nl.tweeenveertig.openstack.client.core.ObjectStoreEntity;

import java.util.Collection;

/**
 * A Container is the entity that holds StoredObjects. This handle allows you to access those objects and set/get
 * information on the container itself. To make sure a Container exists, you can call {@link #exists() exists} which
 * places a call to the Object Store for its information. To ascertain the creation of a Container, call its
 * {@link #create() create} method. By calling {@link #delete() delete}, the Container is removed from the Object
 * Store. Containers start out as private, which means that the StoredObjects in the Container can not be accessed
 * through the public URL. Set the Container to public to be able to do this. It is also important to understand
 * that {@link #getObject(String) getObject} does not create an StoredObject, or otherwise place a call to the Object
 * Store. It just returns a handle for a StoredObject.
 * @author Robert Bor
 */
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
