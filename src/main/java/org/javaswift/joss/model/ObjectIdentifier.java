package org.javaswift.joss.model;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.javaswift.joss.util.SpaceURLEncoder;

public class ObjectIdentifier {

    private String containerName;
    private String objectName;

    public ObjectIdentifier(String containerName, String objectName) {
        this.containerName = containerName;
        this.objectName = objectName;
    }

    public ObjectIdentifier(String containerName) {
        this(containerName, null);
    }

    public String getUrlEncodedIdentifier() throws UnsupportedEncodingException {
        if (objectName == null) {
            return SpaceURLEncoder.encode(containerName);
        } else {
            return SpaceURLEncoder.encode(containerName) + "/" + SpaceURLEncoder.encode(objectName);
        }
    }
    
    public String getContainerName() {
        return containerName;
    }
    
    public Optional<String> getObjectName() {
        return Optional.ofNullable(objectName);
    }

}
