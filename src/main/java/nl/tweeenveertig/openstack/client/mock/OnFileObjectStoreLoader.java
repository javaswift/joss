package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoredObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

public class OnFileObjectStoreLoader {

    private ClassLoader classLoader = OnFileObjectStoreLoader.class.getClassLoader();

    public void createContainers(Account account, String onFileObjectStore) throws IOException, URISyntaxException {
        Enumeration<URL> urls = classLoader.getResources(onFileObjectStore);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            for (File containerFile : listFiles(url)) {
                Container container = account.getContainer(containerFile.getName());
                container.create();
                storeObjects(container, onFileObjectStore + "/" + containerFile.getName());
            }
        }
    }

    public void storeObjects(Container container, String containerLocation) throws IOException, URISyntaxException {
        Enumeration<URL> containerUrls = classLoader.getResources(containerLocation);
        while (containerUrls.hasMoreElements()) {
            URL containerURL = containerUrls.nextElement();
            for (File objectFile : listFiles(containerURL)) {
                StoredObject object = container.getObject(objectFile.getName());
                object.uploadObject(objectFile);
            }
        }
    }

    public File[] listFiles(URL url) throws URISyntaxException {
        if (url != null && url.getProtocol().equals("file")) {
            File file = new File(url.toURI());
            if (file.isDirectory()) {
                return new File(url.toURI()).listFiles();
            }
        }
        return new File[] {};
    }

}
