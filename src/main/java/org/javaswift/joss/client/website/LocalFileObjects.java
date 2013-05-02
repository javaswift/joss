package org.javaswift.joss.client.website;

import org.javaswift.joss.util.FileAction;
import org.javaswift.joss.util.FileReference;

import java.io.File;

public class LocalFileObjects extends AbstractFileObjects {

    private final File rootDirectory;

    public LocalFileObjects(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        for (FileReference file : FileAction.listFiles(this.rootDirectory)) {
            add(file.getPath(), new LocalFileObject(file));
        }
    }

    @Override
    public LocalFileObject create(String path) {
        return new LocalFileObject(new FileReference(new File(rootDirectory.getPath() + "/" + path), null));
    }

}
