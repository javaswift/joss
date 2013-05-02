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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public LocalFileObject create(String path) {
        FileReference fileReference = new FileReference(new File(rootDirectory.getPath() + "/" + path), null);
        if (path.lastIndexOf("/") > -1) {
            String pathExcludingFile = path.substring(0, path.lastIndexOf("/"));
            new File(rootDirectory.getPath() + "/" + pathExcludingFile).mkdirs();
        }
        return new LocalFileObject(fileReference);
    }

}
