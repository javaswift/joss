package org.javaswift.joss.command.impl.identity.authentication;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AuthenticationTest {

    @Test
    public void testMarshalling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        Authentication auth = new Authentication("testtenant", "testuser", "testpassword");
        assertEquals("{\"auth\":{\"passwordCredentials\":{\"username\":\"testuser\",\"password\":\"testpassword\"},\"tenantName\":\"testtenant\"}}", mapper.writeValueAsString(auth));
    }

}
