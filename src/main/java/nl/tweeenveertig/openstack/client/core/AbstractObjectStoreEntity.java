package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.exception.NotFoundException;
import nl.tweeenveertig.openstack.information.AbstractInformation;
import nl.tweeenveertig.openstack.headers.Metadata;
import nl.tweeenveertig.openstack.model.ObjectStoreEntity;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractObjectStoreEntity<I extends AbstractInformation> implements ObjectStoreEntity {

    protected static final boolean ALLOW_CACHING = true;

    protected I info;

    private boolean stale = true;

    private boolean allowCaching = ALLOW_CACHING;

    public AbstractObjectStoreEntity(boolean allowCaching) {
        this.allowCaching = allowCaching;
    }

    public void setMetadata(Map<String, Object> metadata) {
        info.clear();
        for (String key : metadata.keySet()) {
            info.addMetadata(createMetadataEntry(key, metadata.get(key).toString()));
        }
        saveMetadata();
    }

    protected abstract Metadata createMetadataEntry(String name, String value);

    protected abstract void saveMetadata();

    public Map<String, Object> getMetadata() {
        checkForInfo();
        Map<String, Object> metadataValues = new TreeMap<String, Object>();
        for (Metadata metadata : this.info.getMetadata()) {
            metadataValues.put(metadata.getName(), metadata.getHeaderValue());
        }
        return metadataValues;
    }

    protected void checkForInfo() {
        if (isStale()) {
            getInfo();
            setInfoRetrieved();
        }
    }

    protected void setInfoRetrieved() {
        this.stale = false;
    }

    public boolean exists() {
        try {
            checkForInfo();
        } catch (NotFoundException err) {
            return false;
        }
        return true;
    }

    public void invalidate() {
        this.stale = true;
    }

    public boolean isStale() {
        return !isAllowCaching() || this.stale;
    }

    public boolean isAllowCaching() {
        return this.allowCaching;
    }

    public void reload() {
        invalidate();
        checkForInfo();
    }

    /**
    * Included for backwards portability reasons
    * @return true if the metadata has been retrieved
    */
    public boolean isInfoRetrieved() {
        return !this.stale;
    }

    protected abstract void getInfo();
}
