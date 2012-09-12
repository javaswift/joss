package nl.tweeenveertig.openstack.client.core;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractObjectStoreEntity implements ObjectStoreEntity {

    private Map<String, String> metadata = new TreeMap<String, String>();

    private boolean infoRetrieved = false;

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = new TreeMap<String, String>();
        for (String key : metadata.keySet()) {
            this.metadata.put(key, metadata.get(key).toString());
        }
    }

    public Map<String, String> getMetadata() {
        checkForInfo();
        return this.metadata;
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
