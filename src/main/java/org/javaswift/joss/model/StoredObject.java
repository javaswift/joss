package org.javaswift.joss.model;

import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * StoredObject is a handler for the actual content in the Object Store. The method used most on this handler are
 * {@link #uploadObject(java.io.File)}, {@link #uploadObject(java.io.File)} and {@link #uploadObject(byte[])} which
 * all make sure you can upload your content to the Object Store. Downloading is done by calling
 * {@link #downloadObject()}, {@link #downloadObject(java.io.File)} and {@link #downloadObjectAsInputStream()}. To
 * make sure the object really exists, you can call {@link #exists()} which places a call to the Object Store. Deleting
 * is done by calling {@link #delete()}. Creation is done implicitly by uploading content. Two other methods deserve
 * mention here. {@link #copyObject(Container, StoredObject)} copies a StoredObject in a Container to another Container/
 * StoredObject combination. {@link #getPublicURL()} gives you the public URL where the content can be viewed -- this
 * does not work on a private Container.
 * @author Robert Bor
 */
public interface StoredObject extends ObjectStoreEntity, Comparable<StoredObject>, ListSubject {

    /**
    * Fetches the object in a Container in an InputStream. Note that for passing the information on, the
    * content-type is useful. This information can be found by fetching the object information. <b>BE AWARE</b>
    * that the original InputStream must be closed after usage, which can be accomplished by calling close
    * on the InputStream (which consumes the entire original response).
    * @return the object in an InputStreamWrapper (containing the input stream)
    */
    public InputStream downloadObjectAsInputStream();

    /**
    * See documentation of {@link #downloadObjectAsInputStream()}. This method also accepts the download
    * instructions, which can be used among others to set the "Range" and "If-*" headers.
    * @param downloadInstructions the instructions for downloading the object
    * @return the object in an InputStreamWrapper (containing the input stream)
    */
    public InputStream downloadObjectAsInputStream(DownloadInstructions downloadInstructions);

    /**
    * Fetches the object in a Container in a byte array. Note that for passing the information on, the
    * content-type is useful. This information can be found by fetching the object information.
    * @return the object in a byte array
    */
    public byte[] downloadObject();

    /**
    * See documentation of {@link #downloadObject()}. This method also accepts the download
    * instructions, which can be used among others to set the "Range" and "If-*" headers.
    * @param downloadInstructions the instructions for downloading the object
    * @return the object in a byte array
    */
    public byte[] downloadObject(DownloadInstructions downloadInstructions);

    /**
    * Reads the object and writes it to a file.
    * @param targetFile the file to write the object to
    */
    public void downloadObject(File targetFile);

    /**
    * See documentation of {@link #downloadObject(java.io.File)}. This method also accepts the download
    * instructions, which can be used among others to set the "Range" and "If-*" headers.
    * @param targetFile the file to write the object to
    * @param downloadInstructions the instructions for downloading the object
    */
    public void downloadObject(File targetFile, DownloadInstructions downloadInstructions);

    /**
    * Uploads a byte array object to a location designated by the Container and the object.
    * @param uploadInstructions not only the content to be uploaded, but also additional headers that need to be set
    */
    public void uploadObject(UploadInstructions uploadInstructions);

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
    * Explicitly sets the content type of an object. This can be useful, for example when you have content-sniffers
    * in the application that need to advise the object store (which uses a simple file extension matching instead
    * of content-sniffing) as to the real nature of the uploaded file.
    * @param contentType the content type of the object
    * @return this
    */
    public StoredObject setContentType(String contentType);

    /**
    * Schedules the object to be deleted after a fixed period of x seconds
    * @param seconds the number of seconds to wait before deleting the content
    * @return this
    */
    public StoredObject setDeleteAfter(long seconds);

    /**
    * Schedules the object to be deleted at a fixed date. Be careful using this method, since the server's date
    * may be different from yours.
    * @param date the date at which to delete the content
    * @return this
    */
    public StoredObject setDeleteAt(Date date);

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

    /**
    * Returns whether the metadata of the object has been retrieved
    * @return true if the metadata has been retrieved
    */
    public boolean isInfoRetrieved();

    /**
    * Force the Account to reload its metadata
    */
    public void reload();

    /**
    * The last modified date of the StoredObject. If the Container was read by Container.list, this
    * value will not be refetched from the server, unless caching is disabled.
    * @return last modified date
    */
    public Date getLastModifiedAsDate();

    /**
    * The last modified date of the StoredObject. If the Container was read by Container.list, this
    * value will not be refetched from the server, unless caching is disabled.
    * @return last modified date
    */
    public String getLastModified();

    /**
    * The etag (or hash) of the StoredObject. If the Container was read by Container.list, this
    * value will not be refetched from the server, unless caching is disabled.
    * @return etag or hash of the StoredObject
    */
    public String getEtag();

    /**
    * The number of the bytes used by the StoredObject. If the Container was read by Container.list, this
    * value will not be refetched from the server, unless caching is disabled.
    * @return number of bytes used
    */
    public long getContentLength();

    /**
    * The content type of the StoredObject. If the Container was read by Container.list, this
    * value will not be refetched from the server, unless caching is disabled.
    * @return content type
    */
    public String getContentType();

    /**
    * The date when the object will be deleted. Note that this value is never passed in Container.list and
    * therefore always costs an extra HTTP call to the server.
    * @return date when the StoredObject will be deleted (as a java.util.Date)
    */
    public Date getDeleteAtAsDate();

    /**
    * The date when the object will be deleted. Note that this value is never passed in Container.list and
    * therefore always costs an extra HTTP call to the server.
    * @return date when the StoredObject will be deleted (as a String)
    */
    public String getDeleteAt();
    public String getPath();

    public void setLastModified(Date date);
    public void setLastModified(String date);
    public void setEtag(String etag);
    public void setContentLength(long contentLength);
    public void setContentTypeWithoutSaving(String contentType);

}
