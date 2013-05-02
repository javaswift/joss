package org.javaswift.joss.client.website;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.util.FileAction;
import org.javaswift.joss.util.FileReference;

import java.io.File;
import java.io.IOException;

public class LocalFileObject extends AbstractFileObject<ObjectStoreFileObject> {

    FileReference file;

    String md5;

    public LocalFileObject(FileReference file) {
        this.file = file;
        try {
            this.md5 = FileAction.getMd5(getFile());
        } catch (IOException e) {
            throw new CommandException("Unable to determine the MD5 of file "+file.getPath(), e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void delete() {
        getFile().delete();
    }

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
