package org.javaswift.joss.client.website;

import java.io.File;
import java.io.IOException;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.util.FileAction;
import org.javaswift.joss.util.FileReference;

public class LocalFileObject extends AbstractFileObject<ObjectStoreFileObject> {

    FileReference file;

    String md5;

    public LocalFileObject(FileReference file) {
        this.file = file;
        try {
            if (file.hasPath()) {
                this.md5 = FileAction.getMd5(getFile());
            }
        } catch (IOException e) {
            throw new CommandException("Unable to determine the MD5 of file "+file.getPath(), e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void delete() {
        getFile().delete();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void save(ObjectStoreFileObject sourceFile) {
        sourceFile.getObject().downloadObject(getFile());
    }

    @Override
    public String getMd5() {
        return this.md5;
    }

    public File getFile() {
        return this.file.getFile();
    }

}
