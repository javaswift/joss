package org.javaswift.joss.client.mock;

import org.javaswift.joss.model.Client;
import org.javaswift.joss.client.mock.scheduled.ObjectDeleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMock implements Client<AccountMock> {

    public static final Logger LOG = LoggerFactory.getLogger(ClientMock.class);

    private MockUserStore users = new MockUserStore();

    private boolean allowEveryone = false;

    private boolean allowObjectDeleter = true;

    private String onFileObjectStore = null;

    private String publicUrl = null;

    public AccountMock authenticate(String tenant, String username, String password, String authUrl) {
        return authenticate(tenant, username, password, authUrl, null);
    }

    public AccountMock authenticate(String tenant, String username, String password, String authUrl, String preferredRegion) {
        LOG.info("JOSS / MOCK mode");
        if (!allowEveryone) {
            LOG.info("JOSS / Attempting authentication with tenant: "+tenant+", username: "+username+", Auth URL: "+authUrl);
            users.authenticate(username, password);
        }
        LOG.info("JOSS / Creating mock account instance");
        LOG.info("JOSS / * Allow objectdeleter: "+allowObjectDeleter);
        LOG.info("JOSS / * Use onFileObjectStore: "+onFileObjectStore);
        LOG.info("JOSS / * Use public URL: "+publicUrl);
        return new AccountMock()
                .setObjectDeleter(allowObjectDeleter ? new ObjectDeleter(10, 10) : null)
                .setOnFileObjectStore(onFileObjectStore)
                .setPublicUrl(publicUrl);
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

    public ClientMock setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
        return this;
    }

    public MockUserStore getUsers() { return this.users; }
    public void setUsers(MockUserStore users) { this.users = users; }
}
