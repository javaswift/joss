package org.javaswift.joss.model;

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
public interface Container extends ObjectStoreEntity, Comparable<Container>, ListHolder<StoredObject>, ListSubject {

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
    * Creates a Container in the Account.
    */
    public Container create();

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

    /**
    * Returns an object that represents a segment of a single large object
    * @param name the name of the object to create a handle for
    * @param part the part of the large object that the segment represents
    * @return the object handle
    */
    public StoredObject getObjectSegment(String name, int part);

    /**
    * Returns whether the metadata of the container has been retrieved
    * @return true if the metadata has been retrieved
    */
    public boolean isInfoRetrieved();

    /**
    * Force the Account to reload its metadata
    */
    public void reload();

    /**
    * The number of StoredObjects in the Container. If the Container was read by Account.list, this
    * value will not be refetched from the server, unless caching is disabled.
    * @return number of StoredObjects
    */
    public int getCount();

    /**
    * The number of bytes stored by the StoredObjects in the Container. If the Container was read by
    * Account.list, this value will not be refetched from the server, unless caching is disabled.
    * @return number of bytes
    */
    public long getBytesUsed();

    /**
    * Returns a signature that can be used to upload files directly from the browser, using a basic form
    * element.
    * @param redirect redirection URL after the upload has succeeded
    * @param maxFileSize max number of bytes allowed to upload
    * @param maxFileCount max number of files allowed to upload
    * @param seconds number of seconds for the temporary URL to be valid
    * @return FormPost signature for uploading an object directly from the browser
    */
    public FormPost getFormPost(String redirect, long maxFileSize, long maxFileCount, long seconds);

    public boolean isPublic();

    public void setCount(int count);
    public void setBytesUsed(long bytesUsed);

    public abstract Account getAccount();
    
    
    public void setContainerRights(String writePermissions, String readPermissions);

    public String getContainerReadPermission();
    public String getcontainerWritePermission();
}
