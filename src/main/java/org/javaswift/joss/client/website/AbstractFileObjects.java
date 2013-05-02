package org.javaswift.joss.client.website;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractFileObjects implements FileObjects {

    private final Map<String, FileObject> fileObjects = new TreeMap<String, FileObject>();

    protected void add(String path, FileObject fileObject) {
        this.fileObjects.put(path, fileObject);
    }

    public Collection<String> keys() {
        return this.fileObjects.keySet();
    }

    public FileObject get(String path) {
        return this.fileObjects.get(path);
    }

}
