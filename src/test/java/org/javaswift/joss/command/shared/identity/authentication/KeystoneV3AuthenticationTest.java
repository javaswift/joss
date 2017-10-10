package org.javaswift.joss.command.shared.identity.authentication;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class KeystoneV3AuthenticationTest {

    @Test
    public void testMarshalling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);

        KeystoneV3Authentication authentication = new KeystoneV3Authentication(
            "username",
            "password",
            "domainName"
        );

        JsonNode expectedContent = mapper.readTree(
            getClass().getResourceAsStream("/sample-v3-auth-request.json")
        );

        JsonNode actualContent = mapper.valueToTree(authentication);

        assertEquals(expectedContent, actualContent);
    }

}
