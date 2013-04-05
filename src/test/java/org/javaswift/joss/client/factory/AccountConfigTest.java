package org.javaswift.joss.client.factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AccountConfigTest {

    @Test
    public void construct() {
        AccountConfig config = new AccountConfig()
            .setAuthUrl("auth")
            .setPassword("pwd")
            .setTenant("tenant")
            .setUsername("user")
            .setMock(true)
            .setMockMillisDelay(10)
            .setAllowCaching(true)
            .setAllowReauthenticate(true)
            .setMockAllowEveryone(true)
            .setMockAllowObjectDeleter(true)
            .setMockOnFileObjectStore("/some/path")
            .setHost("http://localhost:8080/mock");
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
        assertEquals("http://localhost:8080/mock", config.getHost());
    }
}
