package org.javaswift.joss.client.core;

import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.information.AbstractInformation;
import org.javaswift.joss.headers.Metadata;
import org.javaswift.joss.model.ObjectStoreEntity;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractObjectStoreEntity<I extends AbstractInformation> implements ObjectStoreEntity {

    protected static final boolean ALLOW_CACHING = true;

    protected I info;

    private boolean allowCaching = ALLOW_CACHING;

    private boolean stale = true;

    protected boolean staleHeaders = true;

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

    protected void checkForInfoAndAllowHeaderSet() {
        checkForInfo(true);
    }

    protected void checkForInfo() {
        checkForInfo(false);
    }

    protected void checkForInfo(boolean readHeader) {
        if (isStale(readHeader)) {
            getInfo();
            setInfoRetrieved();
        }
    }

    protected void setInfoRetrieved() {
        this.stale = false;
    }

    public boolean exists() {
        try {
            invalidate();
            checkForInfo();
        } catch (NotFoundException err) {
            return false;
        }
        return true;
    }

    public void invalidate() {
        this.stale = true;
        this.staleHeaders = true;
    }

    public boolean isStale(boolean readHeader) {
        if (!isAllowCaching()) {
            return true;
        }
        if (readHeader) {
            return this.stale && this.staleHeaders;
        }
        return this.stale;
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
