package org.javaswift.joss.client.factory;

import mockit.Cascading;
import mockit.Expectations;
import mockit.NonStrict;
import org.javaswift.joss.client.impl.AccountImpl;
import org.javaswift.joss.client.impl.ClientImpl;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class AccountFactoryTest {

    @NonStrict @Cascading ClientImpl client;

    @NonStrict @Cascading AccountImpl account;

    @Test
    public void constructMock() {
        AccountConfig config = new AccountConfig();
        config.setMock(true);
        AccountFactory factory = new AccountFactory(config);
        assertNotNull(factory.createAccount());
    }

    @Test
    public void getPublicUrl() {
        AccountConfig config = new AccountConfig();
        config.setMock(true);
        config.setPublicHost("http://find.me");
        AccountFactory factory = new AccountFactory(config);
        assertEquals("http://find.me", factory.createAccount().getPublicURL());
    }

    @Test
    public void constructImpl() throws IOException {
        AccountConfig config = new AccountConfig();
        AccountFactory factory = new AccountFactory(config);

        new Expectations() {{
            client.authenticate();
            result = account;
        }};

        factory.createAccount();
    }

    @Test
    public void useFluentInterface() {
        new AccountFactory()
                .setAllowCaching(true)
                .setAllowReauthenticate(true)
                .setAllowContainerCaching(false)
                .setAuthUrl(null)
                .setHttpClient(null)
                .setMock(true)
                .setMockAllowEveryone(true)
                .setMockAllowObjectDeleter(true)
                .setMockMillisDelay(0)
                .setMockOnFileObjectStore(null)
                .setPassword(null)
                .setPrivateHost(null)
                .setPublicHost(null)
                .setSocketTimeout(0)
                .setTenantName(null)
                .setTenantId(null)
                .setUsername(null);
    }

}
