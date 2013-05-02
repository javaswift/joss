package org.javaswift.joss.client.website;

public interface FileObject<M extends FileObject> {

    void delete();

    void save(M sourceFile);

    String getMd5();

}
