package org.javaswift.joss.client.website;

import org.javaswift.joss.util.FileAction;
import org.javaswift.joss.util.FileReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalFileObjects extends AbstractFileObjects {

    private final File rootDirectory;

    public LocalFileObjects(File rootDirectory, final String[] ignoreFilters) {
        super(ignoreFilters);
        this.rootDirectory = rootDirectory;
        for (FileReference file : FileAction.listFiles(this.rootDirectory, ignoreFilters)) {
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

    @Override
    public void cleanup() {
        deleteEmptyDirectories(rootDirectory);
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    protected void deleteEmptyDirectories(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                deleteEmptyDirectories(file);
                file.delete();
            }
        }
    }

}
