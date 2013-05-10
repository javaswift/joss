package org.javaswift.joss.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
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

    public static List<FileReference> listFiles(File root) {
        return listFiles(root, new String[] {});
    }

    public static List<FileReference> listFiles(File root, String[] ignoreFilters) {
        List<FileReference> files = new ArrayList<FileReference>();
        List<String> path = new ArrayList<String>();
        if (root.isDirectory()) {
            listFiles(files, path, root, ignoreFilters);
        }
        return files;
    }

    @SuppressWarnings("ConstantConditions")
    protected static void listFiles(List<FileReference> files, List<String> path, File directoryFile, String[] ignoreFilters) {
        for (File currentFile : directoryFile.listFiles()) {
            if (ignore(path, ignoreFilters)) {
                continue;
            }
            List<String> currentPath = getPath(path, currentFile.getName());
            if (currentFile.isDirectory()) {
                listFiles(files, currentPath, currentFile, ignoreFilters);
            } else {
                files.add(new FileReference(currentFile, currentPath));
            }
        }
    }

    protected static boolean ignore(List<String> pathAndFile, String[] ignoreFilters) {
        String path = FileReference.getPath(0, pathAndFile);
        for (String ignoreFilter : ignoreFilters) {
            if (path.equals(ignoreFilter)) {
                return true;
            }
        }
        return false;
    }

    protected static List<String> getPath(List<String> currentPath, String extension) {
        List<String> path = new ArrayList<String>();
        path.addAll(currentPath);
        path.add(extension);
        return path;
    }

    public static File getFile(String resource) throws IOException, URISyntaxException {
        ClassLoader classLoader = FileAction.class.getClassLoader();
        Enumeration<URL> urls = classLoader.getResources(resource);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url.getProtocol().equals("file")) {
                return new File(url.toURI());
            }
        }
        return null;
    }

}
