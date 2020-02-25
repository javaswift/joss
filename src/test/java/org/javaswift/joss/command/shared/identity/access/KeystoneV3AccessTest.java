package org.javaswift.joss.command.shared.identity.access;

import static org.junit.Assert.assertEquals;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class KeystoneV3AccessTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void tokenPassedThrough() throws Exception {
        KeystoneV3Access access = read("token", "/sample-v3-auth-response.json");

        assertEquals("token", access.getToken());
    }

    @Test
    public void defaultRegion() throws Exception {
        KeystoneV3Access access = read("token", "/sample-v3-auth-response.json");

        // First region listed should be the default
        assertEquals("http://region-obj-100.storage.mycompany.com/swift/v1/public", access.getPublicURL());
        assertEquals("http://region-obj-100.storage.mycompany.com/swift/v1/internal", access.getInternalURL());

    }

    @Test
    public void preferredRegion() throws Exception {
        KeystoneV3Access access = read("token", "/sample-v3-auth-response.json");
        access.setPreferredRegion("region-ssdobj-005");

        assertEquals("http://region-ssdobj-005.storage.mycompany.com/swift/v1/public", access.getPublicURL());
        assertEquals("http://region-ssdobj-005.storage.mycompany.com/swift/v1/internal", access.getInternalURL());

    }

    private KeystoneV3Access read(String tokenValue, String resourceName) throws Exception {
        JsonNode root = mapper.readTree(
            getClass().getResourceAsStream(resourceName)
        );

        return new KeystoneV3Access(tokenValue, root);
    }
}
