package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Account;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class ClientMockTest {

    @Test
    public void authenticate() {
        MockUserStore users = new MockUserStore();
        users.addUser("richard", "test123");
        ClientMock client = new ClientMock();
        client.setUsers(users);
        client.getUsers(); // ignore
        Account account = client.authenticate("", "richard", "test123", "");
        assertNotNull(account);
    }

    @Test
    public void authenticateWithAllowEveryone() {
        ClientMock client = new ClientMock().allowEveryone();
        assertNotNull(client.authenticate(null, null, null, null));
    }

    @Test
    public void noObjectDeleter() {
        Account account = new ClientMock().disallowObjectDeleter().allowEveryone().authenticate(null, null, null, null);
        assertNull(((AccountMock) account).getObjectDeleter());
    }
}
