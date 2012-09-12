package nl.tweeenveertig.openstack.command.core;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractInformation {

    private Map<String, Object> metadata = new TreeMap<String, Object>();

    public void addMetadata(String name, Object value) {
        metadata.put(name, value);
    }

    public Map<String, Object> getMetadata() {
        return this.metadata;
    }

}
