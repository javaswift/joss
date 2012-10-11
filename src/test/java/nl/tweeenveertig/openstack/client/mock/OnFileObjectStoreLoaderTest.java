package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Client;
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
    }

}
