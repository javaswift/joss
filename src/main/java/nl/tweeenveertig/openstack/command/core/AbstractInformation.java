package nl.tweeenveertig.openstack.command.core;

import nl.tweeenveertig.openstack.headers.Metadata;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractInformation {

    private Map<String, Metadata> metadataHeaders = new TreeMap<String, Metadata>();

    public void addMetadata(Metadata metadata) {
        metadataHeaders.put(metadata.getName(), metadata);
    }

    public String getMetadata(String name) {
        return this.metadataHeaders.get(name) != null ? this.metadataHeaders.get(name).getHeaderValue() : null;
    }

    public void setMetadata(Map<String, Metadata> metadataHeaders) {
        this.metadataHeaders = metadataHeaders;
    }

}
