package org.javaswift.joss.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class FileActionTest {

    @Test
    public void md5() throws IOException {
        new FileAction();
        assertNotNull(FileAction.getMd5(new File("pom.xml")));
    }

    @Test
    public void listFilesNotADirectory() throws IOException, URISyntaxException {
        assertEquals(0, FileAction.listFiles(FileAction.getFile("object-store/container1/checkmark.png")).size());
    }

    @Test
    public void getFileDoesNotExist() throws IOException, URISyntaxException {
        assertNull(FileAction.getFile("does-not-exist"));
    }

//    @Test
//    public void noFileProtocol(@Mocked(methods={ "getResources"}) final ClassLoader classLoader) throws IOException, URISyntaxException {
//        new NonStrictExpectations() {
//            Enumeration<URL> urls;
//            URL url; {
//            classLoader.getResources("");
//            result = urls;
//            urls.hasMoreElements();
//            returns(true, false);
//            urls.nextElement();
//            result = url;
//            url.getProtocol();
//            result = "not-file";
//        }};
//        assertNull(FileAction.getFile(""));
//    }

}
