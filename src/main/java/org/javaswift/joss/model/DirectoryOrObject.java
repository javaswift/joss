package org.javaswift.joss.model;

public interface DirectoryOrObject extends ListSubject {

    boolean isObject();
    boolean isDirectory();
    Directory getAsDirectory();
    StoredObject getAsObject();
}
