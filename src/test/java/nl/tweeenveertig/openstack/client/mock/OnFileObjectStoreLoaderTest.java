package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Client;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;

public class OnFileObjectStoreLoaderTest {

    @Test
    public void loadFromFile() throws IOException, URISyntaxException {

        Client client = new ClientMock()
                .setAllowEveryone(true)
                .setOnFileObjectStore("object-store");
        Account account = client.authenticate(null, null, null, null);
        assertEquals(2, account.getContainer("container1").listObjects().size());
        assertEquals(5, account.getContainer("container2").listObjects().size());
        StoredObject object = account.getContainer("container2").getObject("logo.png");
        assertEquals(4670, object.getContentLength());
        assertEquals("image/png", object.getContentType());

//        for (Container container : account.listContainers()) {
//            System.out.println("* " + container.getName());
//            for (StoredObject object : container.listObjects()) {
//                System.out.println("  - "+object.getName());
//                System.out.println("    "+object.getContentType());
//                System.out.println("    "+object.getEtag());
//                System.out.println("    "+object.getLastModified());
//                System.out.println("    "+object.getContentLength());
//            }
//        }
    }

}
