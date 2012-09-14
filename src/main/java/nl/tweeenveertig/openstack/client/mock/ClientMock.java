package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Client;

public class ClientMock implements Client {

    private MockUserStore users = new MockUserStore();

    public Account authenticate(String tenant, String username, String password, String authUrl) {
        return authenticate(tenant, username, password, authUrl, null);
    }

    public Account authenticate(String tenant, String username, String password, String authUrl, String preferredRegion) {
        users.authenticate(username, password);
        return new AccountMock();
    }

    public MockUserStore getUsers() { return this.users; }
    public void setUsers(MockUserStore users) { this.users = users; }
}
