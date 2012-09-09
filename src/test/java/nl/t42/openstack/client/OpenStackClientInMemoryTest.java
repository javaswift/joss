package nl.t42.openstack.client;

import nl.t42.openstack.mock.MockUserStore;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.ContainerInformation;
import nl.t42.openstack.model.StoreObject;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;

public class OpenStackClientInMemoryTest {

    private OpenStackClientInMemory client;

    private Container containerName;

    private StoreObject objectName;

    @Before
    public void setup() {
        client = new OpenStackClientInMemory();
        MockUserStore users = new MockUserStore();
        users.addUser("testuser", "testpassword");
        client.setUsers(users);
        containerName = new Container("somecontainer");
        objectName = new StoreObject("somefile.json");
        client.authenticate("testuser", "testpassword", "someurl");
    }

    @Test
    public void createContainer() {
        client.createContainer(containerName);
        ContainerInformation info = client.getContainerInformation(containerName);
        assertFalse(info.isPublicContainer());
    }

}
