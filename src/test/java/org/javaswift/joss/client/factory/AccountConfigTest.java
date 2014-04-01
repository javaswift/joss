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
        config.setTenantName("tenant");
        config.setTenantId("tenantId");
        config.setUsername("user");
        config.setHashPassword("somepwd");
        config.setTempUrlHashPrefixSource("PUBLIC_URL_PATH");
        config.setAuthenticationMethod("BASIC");
        config.setMock(true);
        config.setMockMillisDelay(10);
        config.setAllowCaching(true);
        config.setAllowReauthenticate(true);
        config.setMockAllowEveryone(true);
        config.setMockAllowObjectDeleter(true);
        config.setMockOnFileObjectStore("/some/path");
        config.setMockOnFileObjectStoreIsAbsolutePath(true);
        config.setPublicHost("http://localhost:8080/mock");
        config.setPrivateHost("api");
        config.setSocketTimeout(1000);
        config.setAllowContainerCaching(false);
        config.setPreferredRegion("AMS-01");
        config.setDisableSslValidation(true);
        config.setDelimiter('\\');
        assertEquals("auth", config.getAuthUrl());
        assertEquals("pwd", config.getPassword());
        assertEquals("tenant", config.getTenantName());
        assertEquals("tenantId", config.getTenantId());
        assertEquals("user", config.getUsername());
        assertEquals("/some/path", config.getMockOnFileObjectStore());
        assertTrue(config.isMockOnFileObjectStoreIsAbsolutePath());
        assertEquals(10, config.getMockMillisDelay());
        assertTrue(config.isMock());
        assertTrue(config.isAllowCaching());
        assertTrue(config.isAllowReauthenticate());
        assertTrue(config.isMockAllowEveryone());
        assertTrue(config.isMockAllowObjectDeleter());
        assertEquals("somepwd", config.getHashPassword());
        assertEquals(TempUrlHashPrefixSource.PUBLIC_URL_PATH, config.getTempUrlHashPrefixSource());
        assertEquals(AuthenticationMethod.BASIC, config.getAuthenticationMethod());
        assertEquals("http://localhost:8080/mock", config.getPublicHost());
        assertEquals("api", config.getPrivateHost());
        assertEquals(1000, config.getSocketTimeout());
        assertEquals(false, config.isAllowContainerCaching());
        assertEquals("AMS-01", config.getPreferredRegion());
        assertEquals(true, config.isDisableSslValidation());
        assertEquals((Character)'\\', config.getDelimiter());
    }
}
