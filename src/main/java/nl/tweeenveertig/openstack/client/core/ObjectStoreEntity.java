package nl.tweeenveertig.openstack.client.core;

import java.util.Map;

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
    public Map<String, String> getMetadata();

}
