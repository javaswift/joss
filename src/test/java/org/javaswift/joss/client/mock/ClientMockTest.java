package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.mock.factory.AccountCommandFactoryMock;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.swift.MockUserStore;
import org.javaswift.joss.swift.Swift;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;

public class ClientMockTest {

    @Test
    public void authenticate() {
        MockUserStore users = new MockUserStore();
        users.addUser("richard", "test123");
        AccountConfig config = new AccountConfig();
        config.setMockAllowEveryone(false);
        ClientMock client = new ClientMock(config);
        client.setUsers(users);
        client.getUsers(); // ignore
        Account account = client.authenticate("", "", "richard", "test123", "");
        assertNotNull(account);
    }

    @Test
    public void authenticateWithAllowEveryone() {
        AccountConfig config = new AccountConfig();
        config.setMockAllowEveryone(true);
        ClientMock client = new ClientMock(config);
        assertNotNull(client.authenticate(null, null, null, null, null));
    }

    @Test
    public void noObjectDeleter() {
        AccountConfig config = new AccountConfig();
        config.setMockAllowObjectDeleter(false);
        config.setMockAllowEveryone(true);
        AccountMock account = new ClientMock(config)
                .authenticate(null, null, null, null, null);
        Swift swift = ((AccountCommandFactoryMock)account.getFactory()).getSwift();
        assertNull(swift.getCurrentObjectDeleter());
    }

    @Test
    public void createSwift() {
        ClientMock mock = new ClientMock(new AccountConfig());
        assertSame(mock.getOrCreateSwift(), mock.getOrCreateSwift());
    }

}
