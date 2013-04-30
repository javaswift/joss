package org.javaswift.joss.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class FileActionTest {

    @Test
    public void testMd5() throws IOException {
        new FileAction();
        assertNotNull(FileAction.getMd5(new File("pom.xml")));
    }

        @Test
    public void noUrl() throws URISyntaxException {
        testUrl(null);
    }

    @Test
    public void urlOfNoneFileType() throws URISyntaxException, MalformedURLException {
        testUrl(new URL("http://java.sun.com/index.html"));
    }

    @Test
    public void noDirectory() throws IOException, URISyntaxException {
        ClassLoader classLoader = FileAction.class.getClassLoader();
        Enumeration<URL> urls = classLoader.getResources("object-store/container1/checkmark.png");
        assertEquals(0, FileAction.listFiles(urls.nextElement()).size());
    }

    protected void testUrl(URL url) throws URISyntaxException {
        assertEquals(0, FileAction.listFiles(url).size());
    }

}
