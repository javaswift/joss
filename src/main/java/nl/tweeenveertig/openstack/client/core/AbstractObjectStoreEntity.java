package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.command.core.AbstractInformation;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.Metadata;

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
        if (!infoRetrieved) {
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
        } catch (CommandException err) {
            if (    CommandExceptionError.CONTAINER_DOES_NOT_EXIST.equals(err.getError()) ||
                    CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST.equals(err.getError())) {
                return false;
            }
            throw err;
        }
        return true;
    }

    public void invalidate() {
        this.infoRetrieved = false;
    }

    protected abstract void getInfo();
}
