package org.javaswift.joss.util;

import java.io.File;
import java.util.List;

public class FileReference {

    private final File file;

    private final List<String> pathAndFile;

    public FileReference(final File file, final List<String> pathAndFile) {
        this.file = file;
        this.pathAndFile = pathAndFile;
    }

    public String getFirstPart() {
        return pathAndFile.get(0);
    }

    public String getPath() {
        return getPath(0);
    }

    public String getPath(int startFrom) {
        StringBuilder path = new StringBuilder();
        int count = 0;
        for (String currentPart : pathAndFile) {
            if (count >= startFrom) {
                if (count - startFrom > 0) {
                    path.append('/');
                }
                path.append(currentPart);
            }
            count++;
        }
        return path.toString();
    }

    public File getFile() {
        return this.file;
    }

}
