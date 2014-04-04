package org.javaswift.joss.model;

public class Directory implements Comparable<DirectoryOrObject>, DirectoryOrObject {

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

    @Override
	public int hashCode() {
		return ((getName() == null) ? 31 : getName().hashCode()) ;
	}

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean equals(Object o) {
        return o instanceof DirectoryOrObject && compareTo((DirectoryOrObject) o) == 0;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public int compareTo(DirectoryOrObject o) {
        return getName().compareTo(o.getName());
    }

}
