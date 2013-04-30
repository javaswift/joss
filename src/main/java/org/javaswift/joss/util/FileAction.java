package org.javaswift.joss.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileAction {

    public static void handleEntity(File targetFile, HttpEntity entity) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(targetFile);
            IOUtils.copy(entity.getContent(), output);
        } finally {
            close(output);
        }
    }

    public static String getMd5(File file) throws IOException {
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            return DigestUtils.md5Hex(input);
        } finally {
            close(input);
        }
    }

    protected static void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

    public static List<FileReference> listFiles(URL url) throws URISyntaxException {
        if (url != null && url.getProtocol().equals("file")) {
            File file = new File(url.toURI());
            return listFiles(file);
        }
        return new ArrayList<FileReference>();
    }

    public static List<FileReference> listFiles(File root) {
        List<FileReference> files = new ArrayList<FileReference>();
        List<String> path = new ArrayList<String>();
        if (root.isDirectory()) {
            listFiles(files, path, root);
        }
        return files;
    }

    @SuppressWarnings("ConstantConditions")
    protected static void listFiles(List<FileReference> files, List<String> path, File directoryFile) {
        for (File currentFile : directoryFile.listFiles()) {
            List<String> currentPath = getPath(path, currentFile.getName());
            if (currentFile.isDirectory()) {
                listFiles(files, currentPath, currentFile);
            } else {
                files.add(new FileReference(currentFile, currentPath));
            }
        }
    }

    protected static List<String> getPath(List<String> currentPath, String extension) {
        List<String> path = new ArrayList<String>();
        path.addAll(currentPath);
        path.add(extension);
        return path;
    }

}
