package nl.tweeenveertig.openstack.client;

import nl.tweeenveertig.openstack.client.core.ObjectStoreEntity;

import java.io.File;
import java.io.InputStream;

public interface StoredObject extends ObjectStoreEntity {

    /**
    * Fetches the object in a Container in an InputStream. Note that for passing the information on, the
    * content-type is useful. This information can be found by fetching the object information. <b>BE AWARE</b>
    * that the original InputStream must be closed after usage, which can be accomplished by calling the
    * closeStream() method on InputStreamWrapper (which consumes the entire original response).
    * @return the object in an InputStreamWrapper (containing the input stream)
    */
    public InputStream downloadObjectAsInputStream();

    /**
    * Fetches the object in a Container in a byte array. Note that for passing the information on, the
    * content-type is useful. This information can be found by fetching the object information.
    * @return the object in a byte array
    */
    public byte[] downloadObject();

    /**
    * Reads the object and writes it to a file.
    * @param targetFile the file to write the object to
    */
    public void downloadObject(File targetFile);

    /**
    * Uploads a byte array object to a location designated by the Container and the object.
    * @param inputStream the actual content that must be uploaded to the object, in InputStream format
    */
    public void uploadObject(InputStream inputStream);

    /**
    * Uploads a byte array object to a location designated by the Container and the object.
    * @param fileToUpload the actual content that must be uploaded to the object
    */
    public void uploadObject(byte[] fileToUpload);

    /**
    * Uploads a file from a designated location to a location designated by the Container and the
    * object.
    * @param fileToUpload the file containing the content that must be uploaded to the object
    */
    public void uploadObject(File fileToUpload);

    /**
    * Deletes an object in a container. In combination with copyObject, this is effectively the "Best Practice'
    * move method.
    */
    public void delete();

    /**
    * Copies the content of an object to another object. In combination with deleteObject, this is effectively
    * the "Best Practice" move method.
    * @param targetContainer the owning Container of the target location of the copy
    * @param targetObject the object location where the copy must be placed
    */
    public void copyObject(Container targetContainer, StoredObject targetObject);

    /**
    * Returns the public URL through which the resource can be viewed
    * @return the public URL of the resource
    */
    public String getPublicURL();

    /**
    * Checks whether a container exists
    * @return whether the container exists
    */
    public boolean exists();

    public String getLastModified();
    public String getEtag();
    public long getContentLength();
    public String getContentType();
    public String getName();

}