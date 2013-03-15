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
    * Gets the metadata headers
    * @return the metadata on the entity
    */
    public Map<String, Object> getMetadata();

}
