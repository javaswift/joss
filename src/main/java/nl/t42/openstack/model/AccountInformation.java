package nl.t42.openstack.model;

import java.util.Map;
import java.util.TreeMap;

public class AccountInformation {

    private Map<String, String> metadata = new TreeMap<String, String>();

    private int containerCount;

    private int objectCount;

    private long bytesUsed;

    public void addMetadata(String name, String value) {
        metadata.put(name, value);
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public int getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(int containerCount) {
        this.containerCount = containerCount;
    }

    public long getBytesUsed() {
        return bytesUsed;
    }

    public void setBytesUsed(long bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }
}
