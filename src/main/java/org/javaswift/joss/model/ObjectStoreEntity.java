package org.javaswift.joss.model;

import java.util.Map;

/**
* Top-level entity for all entities in the ObjectStore. The sharing characteristic of the entities
* is custom metadata.
*/
public interface ObjectStoreEntity {

    /**
    * Sets the metadata headers
    * @param metadata contains all metadata name/value pairs that must be set
    */
    public void setMetadata(Map<String, Object> metadata);

    /**
    * Sets a single metadata field and immediately places a call to the server to save
    * @param key metadata field to add
    * @param value value of the metadata field
    */
    public void setAndSaveMetadata(String key, Object value);

    /**
    * Sets a single metadata field, but does not place a call to the server to save. This must be explicitly called
    * with saveMetadata.
    * @param key metadata field to add
    * @param value value of the metadata field
    */
    public void setAndDoNotSaveMetadata(String key, Object value);

    /**
    * Removes a single metadata field and immediately places a call to the server to save
    * @param key metadata field to remove
    */
    public void removeAndSaveMetadata(String key);

    /**
    * Removes a single metadata field, but does not place a call to the server to save. This must be explicitly
    * called with saveMetadata.
    * @param key metadata field to remove
    */
    public void removeAndDoNotSaveMetadata(String key);

    /**
    * Returns a single value of the designated key, or null if it does not exist.
    * @param key the key of the key/value pair
    * @return the value belonging to the key
    */
    public Object getMetadata(String key);

    /**
    * Places a call to the server to save all metadata, previously set with setMetadata excluding the save option
    */
    public void saveMetadata();

    /**
    * Gets the metadata headers
    * @return the metadata on the entity
    */
    public Map<String, Object> getMetadata();

    /**
    * Returns the path to be appended to the host
    * @return the path to append to the host
    */
    public String getPath();

}
