package org.javaswift.joss.model;

public class Directory implements DirectoryOrObject {

    final private String path;

    public Directory(String directory) {
        this.path = directory;
    }

    public String getPath() {
        return this.path;
    }

    @Override public boolean isObject() { return false; }
    @Override public boolean isDirectory() { return true; }
    @Override public Directory getAsDirectory() {
        return this;
    }
    @Override public StoredObject getAsObject() {
        throw new UnsupportedOperationException("A Directory cannot be cast to a StoredObject");
    }
    @Override public String getName() { return path; }
    @Override public void metadataSetFromHeaders() { /* ignore */ }
}
