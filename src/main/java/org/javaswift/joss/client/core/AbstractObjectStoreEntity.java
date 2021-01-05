package org.javaswift.joss.client.core;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.headers.Metadata;
import org.javaswift.joss.information.AbstractInformation;
import org.javaswift.joss.model.ObjectStoreEntity;

public abstract class AbstractObjectStoreEntity<I extends AbstractInformation> implements ObjectStoreEntity {

    protected static final boolean ALLOW_CACHING = true;

    protected I info;

    private boolean allowCaching = ALLOW_CACHING;

    private boolean stale = true;

    protected boolean staleHeaders = true;

    public AbstractObjectStoreEntity(boolean allowCaching) {
        this.allowCaching = allowCaching;
    }

    @Override
    public void setMetadata(Map<String, Object> metadata) {
        info.clear();
        for (String key : metadata.keySet()) {
            setAndDoNotSaveMetadata(key, metadata.get(key));
        }
        saveMetadata();
    }

    @Override
    public void setAndSaveMetadata(String key, Object value) {
        setAndDoNotSaveMetadata(key, value);
        saveMetadata();
    }

    @Override
    public void setAndDoNotSaveMetadata(String key, Object value) {
        info.addMetadata(createMetadataEntry(key, value.toString()));
    }

    @Override
    public void removeAndSaveMetadata(String key) {
        setAndSaveMetadata(key, "");
    }

    @Override
    public void removeAndDoNotSaveMetadata(String key) {
        setAndDoNotSaveMetadata(key, "");
    }

    protected abstract Metadata createMetadataEntry(String name, String value);

    @Override
    public void saveMetadata() {
        saveSpecificMetadata();
        invalidate();
    }

    protected abstract void saveSpecificMetadata();

    @Override
    public Object getMetadata(String key) {
        return getMetadata().get(Metadata.capitalize(key));
    }

    @Override
    public Map<String, Object> getMetadata() {
        checkForInfo();
        Map<String, Object> metadataValues = new TreeMap<String, Object>();
        for (Metadata metadata : this.info.getMetadata()) {
            metadataValues.put(metadata.getName(), metadata.getHeaderValue());
        }
        return metadataValues;
    }

    protected void checkForInfoAndAllowHeaderSet() {
        checkForInfo(true, true);
    }

    protected void checkForInfo() {
        checkForInfo(false, true);
    }

    protected void checkForInfoDisallowErrorLog() {
        checkForInfo(false, false);
    }

    protected void checkForInfo(boolean readHeader, boolean allowErrorLog) {
        if (isStale(readHeader)) {
            getInfo(allowErrorLog);
            setInfoRetrieved();
        }
    }

    protected void setInfoRetrieved() {
        this.stale = false;
    }

    public boolean exists() {
        try {
            invalidate();
            checkForInfoDisallowErrorLog();
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

    @Override
    public String getPath() {
        try {
            return getPathForEntity();
        } catch (Exception e) {
            throw new CommandException("Unable to encode the object path");
        }
    }

    public abstract String getPathForEntity() throws UnsupportedEncodingException;

    protected abstract void getInfo(boolean allowErrorLog);
}
