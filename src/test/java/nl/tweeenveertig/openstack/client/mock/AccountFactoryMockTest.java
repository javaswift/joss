package nl.tweeenveertig.openstack.client.mock;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class AccountFactoryMockTest {

    @Test
    public void construct() {
        AccountFactoryMock factory = new AccountFactoryMock();
        factory.setAuthUrl(null);
        factory.setPassword(null);
        factory.setTenant(null);
        factory.setUsername(null);
        assertNotNull(factory.getAccount());
    }
}
