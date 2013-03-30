package org.javaswift.joss.swift;

import org.javaswift.joss.instructions.UploadInstructions;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

public class OnFileObjectStoreLoader {

    private ClassLoader classLoader = OnFileObjectStoreLoader.class.getClassLoader();

    public Map<String, SwiftContainer> createContainers(String onFileObjectStore) throws IOException, URISyntaxException {
        Map<String, SwiftContainer> containers = new TreeMap<String, SwiftContainer>();
        Enumeration<URL> urls = classLoader.getResources(onFileObjectStore);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            for (File containerFile : listFiles(url)) {
                SwiftContainer container = new SwiftContainer(containerFile.getName());
                containers.put(container.getName(), container);
                storeObjects(container, onFileObjectStore + "/" + containerFile.getName());
            }
        }
        return containers;
    }

    public void storeObjects(SwiftContainer container, String containerLocation) throws IOException, URISyntaxException {
        Enumeration<URL> containerUrls = classLoader.getResources(containerLocation);
        while (containerUrls.hasMoreElements()) {
            URL containerURL = containerUrls.nextElement();
            for (File objectFile : listFiles(containerURL)) {
                SwiftStoredObject object = container.createObject(objectFile.getName());
                object.uploadObject(new UploadInstructions(objectFile));
            }
        }
    }

    public File[] listFiles(URL url) throws URISyntaxException {
        if (url != null && url.getProtocol().equals("file")) {
            File file = new File(url.toURI());
            if (file.isDirectory()) {
                return new File(url.toURI()).listFiles();
            }
        }
        return new File[] {};
    }

}
