package nl.t42.openstack.model;

import java.util.Map;
import java.util.TreeMap;

public class ContainerInformation {

    private Map<String, String> metadata = new TreeMap<String, String>();

    private int objectCount;

    private long bytesUsed;

    private boolean publicContainer;

    public void addMetadata(String name, String value) {
        metadata.put(name, value);
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }

    public long getBytesUsed() {
        return bytesUsed;
    }

    public void setBytesUsed(long bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    public boolean isPublicContainer() {
        return publicContainer;
    }

    public void setPublicContainer(boolean publicContainer) {
        this.publicContainer = publicContainer;
    }
}
