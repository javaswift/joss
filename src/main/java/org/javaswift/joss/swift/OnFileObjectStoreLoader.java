package org.javaswift.joss.swift;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.util.FileAction;
import org.javaswift.joss.util.FileReference;

public class OnFileObjectStoreLoader {

    public Map<String, SwiftContainer> createContainers(Class classpathProvider,
                                                        String onFileObjectStore,
                                                        boolean absolutePath) throws IOException, URISyntaxException {
        Map<String, SwiftContainer> containers = new TreeMap<String, SwiftContainer>();
        final File file;
        if (absolutePath) {
            file = new File(onFileObjectStore);
        } else if (classpathProvider != null) {
            file = FileAction.getFile(classpathProvider, onFileObjectStore);
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
