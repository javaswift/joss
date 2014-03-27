package org.javaswift.joss.model;

public class Directory implements DirectoryOrObject {

    final private String path;

    final private String bareName;

    public Directory(String path, Character delimiter) {
        this.path = path;
        this.bareName = bareName(path, delimiter);
    }

    public static String bareName(String name, Character delimiter) {
        if (name == null) {
            return null;
        }
        int lastSlash = name.lastIndexOf(delimiter);
        if (lastSlash != -1 && lastSlash == name.length() - 1) {
            name = name.substring(0, name.length()-1);
        }
        lastSlash = name.lastIndexOf(delimiter);
        if (lastSlash != -1) {
            name = name.substring(lastSlash+1, name.length());
        }
        return name;
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

    @Override
    public String getBareName() {
        return bareName;
    }

}
