package org.javaswift.joss.information;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.javaswift.joss.headers.Metadata;

public abstract class AbstractInformation {

    private Map<String, Metadata> metadataHeaders = new TreeMap<String, Metadata>();

    public void clear() {
        metadataHeaders.clear();
    }

    public void addMetadata(Metadata metadata) {
        metadataHeaders.put(metadata.getName(), metadata);
    }

    public String getMetadata(String name) {
        final String lowerCaseName = Metadata.capitalize(name);
        return this.metadataHeaders.get(lowerCaseName) != null ? this.metadataHeaders.get(lowerCaseName).getHeaderValue() : null;
    }

    public Collection<Metadata> getMetadata() {
        return this.metadataHeaders.values();
    }

    public void setMetadata(Map<String, Metadata> metadataHeaders) {
        this.metadataHeaders = metadataHeaders;
    }

}
