package org.javaswift.joss.client.website;

import java.util.*;

public abstract class AbstractFileObjects implements FileObjects {

    private final Map<String, FileObject> fileObjects = new TreeMap<String, FileObject>();

    private final String[] ignoreFilters;

    public AbstractFileObjects() {
        this(new String[]{});
    }

    public AbstractFileObjects(final String[] ignoreFilters) {
        this.ignoreFilters = ignoreFilters;
    }

    protected void add(String path, FileObject fileObject) {
        this.fileObjects.put(path, fileObject);
    }

    @Override
    public Collection<String> keys() {
        return this.fileObjects.keySet();
    }

    @Override
    public FileObject get(String path) {
        return this.fileObjects.get(path);
    }

    @Override
    public boolean ignore(String path) {
        for (String ignoreFilter : ignoreFilters) {
            if (path.equals(ignoreFilter)) {
                return true;
            }
        }
        return false;
    }

}
