package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.exception.NotFoundException;
import nl.tweeenveertig.openstack.information.AbstractInformation;
import nl.tweeenveertig.openstack.headers.Metadata;
import nl.tweeenveertig.openstack.model.ObjectStoreEntity;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractObjectStoreEntity<I extends AbstractInformation> implements ObjectStoreEntity {

    protected I info;

    private boolean infoRetrieved = false;

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
        if (!isInfoRetrieved()) {
            getInfo();
            setInfoRetrieved();
        }
    }

    protected void setInfoRetrieved() {
        this.infoRetrieved = true;
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
        this.infoRetrieved = false;
    }

    public boolean isInfoRetrieved() {
        return this.infoRetrieved;
    }

    protected abstract void getInfo();
}
