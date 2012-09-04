package nl.t42.openstack.client;

import nl.t42.openstack.model.*;

import java.io.File;
import java.util.Map;

/**
* The OpenStackClient is a <b>stateful</b> client with a Facade to the REST API of the OpenStack implementation.
* The underlying implementation relies on 1 account with multiple Containers and in every Container
* multiple object (call StoreObject to prevent confusion with Object). The only call leading to a state
* change is the authenticate call. Note that security tokens last for a maximum of 24 hours, after which
* a new one must be fetched.
* @author Robert Bor
*/
public interface OpenStackClient {

    /**
    * Calls the identity module of the OpenStack implementation to authenticate itself against. If
    * successful, it returns the internal URL to work with and the authentication token, which will
    * automatically be added to all calls made by the client henceforth.
    * @param username username of the account
    * @param password password of the account
    * @param authUrl URL to call the authenticate against, supplied by your OpenStack implementation vendor
    */
    public void authenticate(String username, String password, String authUrl);

    /**
    * Provides information on the Account such as the number of containers, objects and the total amount
    * of storage used up. Furthermore, any added metadata is also returned.
    * @return the account information
    */
    public AccountInformation getAccountInformation();

    /**
    * Sets the metadata headers of an Account.
    * @param metadata contains all metadata name/value pairs that must be set on the Account
    */
    public void setAccountInformation(Map<String, Object> metadata);

    /**
    * Returns all the containers in an Account.
    * @return the containers in an Account
    */
    public Container[] listContainers();

    /**
    * Creates a Container in the Account.
    * @param container the Container to create
    */
    public void createContainer(Container container);

    /**
    * Deletes a Container from the Account. Note that the Container must be empty in order to be deleted.
    * @param container the Container to delete
    */
    public void deleteContainer(Container container);

    /**
    * Takes a single Container and makes it public. ALL (!) the objects in the Container are now public, so be
    * very careful exercising this option. The public URL can now be used to access objects.
    * @param container the Container to make public
    */
    public void makeContainerPublic(Container container);

    /**
    * Takes a single Container and makes it private. All the objects in the Container are now private and can
    * no longer be accessed through the public URL.
    * @param container the Container to make private
    */
    public void makeContainerPrivate(Container container);

    /**
    * Provides information on a Container, such as the number of objects, the storage usage in bytes, whether
    * the Container is public and all the metadata key/value pairs.
    * @param container the Container to report on
    * @return the information on the Container
    */
    public ContainerInformation getContainerInformation(Container container);

    /**
    * Sets the metadata headers on a Container
    * @param container the Container the metadata changes must be made to
    * @param metadata contains all metadata name/value pairs that must be set on the Container
    */
    public void setContainerInformation(Container container, Map<String, Object> metadata);

    /**
    * Lists all the objects in a Container
    * @param container the owning Container of the objects
    * @return the objects in the Container
    */
    public StoreObject[] listObjects(Container container);

    /**
    * Fetches the object in a Container in a byte array. Note that for passing the information on, the
    * content-type is useful. This information can be found by fetching the object information.
    * @param container the owning Container of the object
    * @param object the object's name
    * @return the object in a byte array
    */
    public byte[] downloadObject(Container container, StoreObject object);

    /**
    * Reads the object and writes it to a file.
    * @param container the owning Container of the object
    * @param object the object's name
    * @param targetFile the file to write the object to
    */
    public void downloadObject(Container container, StoreObject object, File targetFile);

    /**
    * Uploads a byte array object to a location designated by the Container and the object.
    * @param container the owning Container of the object
    * @param target the object's name
    * @param fileToUpload the actual content that must be uploaded to the object
    */
    public void uploadObject(Container container, StoreObject target, byte[] fileToUpload);

    /**
    * Uploads a file from a designated location to a location designated by the Container and the
    * object.
    * @param container the owning Container of the object
    * @param target the object's name
    * @param fileToUpload the file containing the content that must be uploaded to the object
    */
    public void uploadObject(Container container, StoreObject target, File fileToUpload);

    /**
    * Provides information on an object, such as when the object was last modified, its MD5 hash (etag),
    * size, content type and all the metadata key/value pairs.
    * @param container the owning Container of the object
    * @param object the object to report on
    * @return the information on the object
    */
    public ObjectInformation getObjectInformation(Container container, StoreObject object);

    /**
    * Sets the metadata headers on an object.
    * @param container the owning Container of the object
    * @param object the object having its metadata changed
    * @param metadata contains all metadata name/value pairs that must be set on the Container
    */
    public void setObjectInformation(Container container, StoreObject object, Map<String, Object> metadata);

    /**
    * Deletes an object in a container. In combination with copyObject, this is effectively the "Best Practice'
    * move method.
    * @param container the owning Container of the object
    * @param object the object which must be deleted
    */
    public void deleteObject(Container container, StoreObject object);

    /**
    * Copies the content of an object to another object. In combination with deleteObject, this is effectively
    * the "Best Practice" move method.
    * @param sourceContainer the owning Container of the object that must be copied
    * @param sourceObject the object which must be copied
    * @param targetContainer the owning Container of the target location of the copy
    * @param targetObject the object location where the copy must be placed
    */
    public void copyObject(Container sourceContainer, StoreObject sourceObject, Container targetContainer, StoreObject targetObject);

    /**
    * Checks whether the client has already authenticated. Note that this call does NOT check whether the token is
    * still valid.
    * @return true if the client is authenticated; false if not
    */
    public boolean isAuthenticated();
}
