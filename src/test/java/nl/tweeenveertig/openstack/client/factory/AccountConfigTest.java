package nl.tweeenveertig.openstack.client.factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AccountConfigTest {

    @Test
    public void construct() {
        AccountConfig config = new AccountConfig();
        config.setAuthUrl("auth");
        config.setPassword("pwd");
        config.setTenant("tenant");
        config.setUsername("user");
        config.setMock(true);
        assertEquals("auth", config.getAuthUrl());
        assertEquals("pwd", config.getPassword());
        assertEquals("tenant", config.getTenant());
        assertEquals("user", config.getUsername());
        assertTrue(config.isMock());
    }
}
