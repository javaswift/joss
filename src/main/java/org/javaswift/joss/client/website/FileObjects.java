package org.javaswift.joss.client.website;

import java.util.Collection;

public interface FileObjects {

    Collection<String> keys();

    FileObject get(String path);

    FileObject create(String path);

    void cleanup();

}
