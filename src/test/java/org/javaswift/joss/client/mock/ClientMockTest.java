package org.javaswift.joss.client.mock;

import org.javaswift.joss.command.mock.factory.AccountCommandFactoryMock;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.swift.MockUserStore;
import org.javaswift.joss.swift.Swift;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class ClientMockTest {

    @Test
    public void authenticate() {
        MockUserStore users = new MockUserStore();
        users.addUser("richard", "test123");
        ClientMock client = new ClientMock().setAllowEveryone(false);
        client.setUsers(users);
        client.getUsers(); // ignore
        Account account = client.authenticate("", "richard", "test123", "");
        assertNotNull(account);
    }

    @Test
    public void authenticateWithAllowEveryone() {
        ClientMock client = new ClientMock().setAllowEveryone(true);
        assertNotNull(client.authenticate(null, null, null, null));
    }

    @Test
    public void noObjectDeleter() {
        AccountMock account = new ClientMock()
                .setAllowObjectDeleter(false)
                .setAllowEveryone(true)
                .authenticate(null, null, null, null);
        Swift swift = ((AccountCommandFactoryMock)account.getFactory()).getSwift();
        assertNull(swift.getObjectDeleter());
    }

}
