package org.javaswift.joss.client.factory;

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
        config.setMockMillisDelay(10);
        config.setAllowCaching(true);
        config.setAllowReauthenticate(true);
        config.setMockAllowEveryone(true);
        config.setMockAllowObjectDeleter(true);
        config.setMockOnFileObjectStore("/some/path");
        config.setPublicHost("http://localhost:8080/mock");
        config.setPrivateHost("api");
        config.setSocketTimeout(1000);
        config.setAllowContainerCaching(false);
        assertEquals("auth", config.getAuthUrl());
        assertEquals("pwd", config.getPassword());
        assertEquals("tenant", config.getTenant());
        assertEquals("user", config.getUsername());
        assertEquals("/some/path", config.getMockOnFileObjectStore());
        assertEquals(10, config.getMockMillisDelay());
        assertTrue(config.isMock());
        assertTrue(config.isAllowCaching());
        assertTrue(config.isAllowReauthenticate());
        assertTrue(config.isMockAllowEveryone());
        assertTrue(config.isMockAllowObjectDeleter());
        assertEquals("http://localhost:8080/mock", config.getPublicHost());
        assertEquals("api", config.getPrivateHost());
        assertEquals(1000, config.getSocketTimeout());
        assertEquals(false, config.isAllowContainerCaching());
    }
}
