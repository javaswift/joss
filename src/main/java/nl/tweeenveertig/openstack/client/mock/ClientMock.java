package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Client;
import nl.tweeenveertig.openstack.client.mock.scheduled.ObjectDeleter;

public class ClientMock implements Client<AccountMock> {

    private MockUserStore users = new MockUserStore();

    private boolean allowEveryone = false;

    private boolean allowObjectDeleter = true;

    private String onFileObjectStore = null;

    public AccountMock authenticate(String tenant, String username, String password, String authUrl) {
        return authenticate(tenant, username, password, authUrl, null);
    }

    public AccountMock authenticate(String tenant, String username, String password, String authUrl, String preferredRegion) {
        if (!allowEveryone) {
            users.authenticate(username, password);
        }
        return new AccountMock()
                .setObjectDeleter(allowObjectDeleter ? new ObjectDeleter(10, 10) : null)
                .setOnFileObjectStore(onFileObjectStore);
    }

    public ClientMock setAllowObjectDeleter(boolean allowObjectDeleter) {
        this.allowObjectDeleter = allowObjectDeleter;
        return this;
    }

    public ClientMock setAllowEveryone(boolean allowEveryone) {
        this.allowEveryone = allowEveryone;
        return this;
    }

    public ClientMock setOnFileObjectStore(String onFileObjectStore) {
        this.onFileObjectStore = onFileObjectStore;
        return this;
    }

    public MockUserStore getUsers() { return this.users; }
    public void setUsers(MockUserStore users) { this.users = users; }
}
