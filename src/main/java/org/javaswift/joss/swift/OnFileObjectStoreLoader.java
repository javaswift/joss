package org.javaswift.joss.swift;

import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.util.FileAction;
import org.javaswift.joss.util.FileReference;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OnFileObjectStoreLoader {

    public Map<String, SwiftContainer> createContainers(String onFileObjectStore, boolean absolutePath) throws IOException, URISyntaxException {
        Map<String, SwiftContainer> containers = new TreeMap<String, SwiftContainer>();
        final File file;
        if (absolutePath) {
            file = new File(onFileObjectStore);
        } else {
            file = FileAction.getFile(onFileObjectStore);
        }
        List<FileReference> files = FileAction.listFiles(file);
        for (FileReference fileReference : files) {
            // Create container
            String containerName = fileReference.getFirstPart();
            SwiftContainer container = containers.get(containerName);
            if (container == null) {
                container = new SwiftContainer(containerName);
                containers.put(container.getName(), container);
            }
            // Create object
            String objectName = fileReference.getPath(1);
            SwiftStoredObject object = container.createObject(objectName);
            object.uploadObject(new UploadInstructions(fileReference.getFile()));
        }
        return containers;
    }

}
