package nl.tweeenveertig.openstack.model;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractInformation {

    private Map<String, String> metadata = new TreeMap<String, String>();

    public void addMetadata(String name, String value) {
        metadata.put(name, value);
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

}
