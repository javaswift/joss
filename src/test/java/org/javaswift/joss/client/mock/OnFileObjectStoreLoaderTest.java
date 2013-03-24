package org.javaswift.joss.client.mock;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Client;
import org.javaswift.joss.model.StoredObject;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import static junit.framework.Assert.assertEquals;

public class OnFileObjectStoreLoaderTest {

    @Test
    public void loadFromFile() throws IOException, URISyntaxException {

        Client client = new ClientMock()
                .setAllowEveryone(true)
                .setOnFileObjectStore("object-store");
        Account account = client.authenticate(null, null, null, null);
        assertEquals(2, account.getContainer("container1").list().size());
        assertEquals(5, account.getContainer("container2").list().size());
        StoredObject object = account.getContainer("container2").getObject("logo.png");
        assertEquals(4670, object.getContentLength());
        assertEquals("image/png", object.getContentType());
    }

    @Test
    public void noDirectory() throws IOException, URISyntaxException {
        OnFileObjectStoreLoader loader = new OnFileObjectStoreLoader();
        ClassLoader classLoader = OnFileObjectStoreLoader.class.getClassLoader();
        Enumeration<URL> urls = classLoader.getResources("object-store/container1/checkmark.png");
        assertEquals(0, loader.listFiles(urls.nextElement()).length);
    }

    @Test
    public void noUrl() throws URISyntaxException {
        testUrl(null);
    }

    @Test
    public void urlOfNoneFileType() throws URISyntaxException, MalformedURLException {
        testUrl(new URL("http://java.sun.com/index.html"));
    }

    protected void testUrl(URL url) throws URISyntaxException {
        OnFileObjectStoreLoader loader = new OnFileObjectStoreLoader();
        assertEquals(0, loader.listFiles(url).length);
    }

}
