package nl.t42.openstack.command.identity.access;

import nl.t42.openstack.util.ClasspathTemplateResource;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class AccessTest {
    
    @Test
    public void testUnmarshalling() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
        Access access = mapper.readValue(jsonString, Access.class);
        assertEquals("a376b74fbdb64a4986cd3234647ff6f8", access.getToken());
        assertEquals("https://og.cloudvps.com:443/v1/AUTH_bfo000024", access.getInternalURL());
        assertEquals("http://bfo000024.og.cloudvps.com:80", access.getPublicURL());
    }
}
