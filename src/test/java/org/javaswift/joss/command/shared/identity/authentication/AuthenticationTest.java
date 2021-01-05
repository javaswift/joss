package org.javaswift.joss.command.shared.identity.authentication;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;

public class AuthenticationTest {

    @Test
    public void testMarshalling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        Authentication auth = new Authentication("testtenant", "testtenantid", "testuser", "testpassword");
        assertEquals("{\"auth\":{\"passwordCredentials\":{\"username\":\"testuser\",\"password\":\"testpassword\"},\"tenantName\":\"testtenant\",\"tenantId\":\"testtenantid\"}}", mapper.writeValueAsString(auth));
    }

}
