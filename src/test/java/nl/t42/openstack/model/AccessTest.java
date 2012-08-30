package nl.t42.openstack.model;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class AccessTest {
    
    @Test
    public void testUnmarshalling() throws IOException {
        String jsonString =
                "{" +
                "    \"access\": {" +
                "        \"token\": {" +
                "            \"expires\": \"2012-08-31T06:01:48Z\"," +
                "            \"id\": \"a376b74fbdb64a4986cd32347f64f8f6\"," +
                "            \"tenant\": {" +
                "                \"enabled\": true," +
                "                \"id\": \"bfo000042\"," +
                "                \"name\": \"bfo000042\"" +
                "            }" +
                "        }," +
                "        \"serviceCatalog\": [{" +
                "            \"endpoints\": [{" +
                "                \"adminURL\": \"https://og.cloudvps.com/\"," +
                "                \"region\": \"AMS-01\"," +
                "                \"internalURL\": \"https://og.cloudvps.com:443/v1/AUTH_bfo000042\"," +
                "                \"publicURL\": \"http://bfo000042.og.cloudvps.com:80\"" +
                "            }]," +
                "            \"endpoints_links\": []," +
                "            \"type\": \"object-store\"," +
                "            \"name\": \"swift\"" +
                "        }]," +
                "        \"user\": {" +
                "            \"username\": \"Robert Bor\"," +
                "            \"roles_links\": []," +
                "            \"id\": \"DRB000010\"," +
                "            \"roles\": [{" +
                "                \"id\": \"swiftoperator\"," +
                "                \"name\": \"swiftoperator\"" +
                "            }]," +
                "            \"name\": \"Robert Bor\"" +
                "        }" +
                "    }" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
        Access access = mapper.readValue(jsonString, Access.class);
        assertEquals("a376b74fbdb64a4986cd32347f64f8f6", access.token.id);
        assertEquals("https://og.cloudvps.com:443/v1/AUTH_bfo000042", access.serviceCatalog.get(0).endpoints.get(0).internalURL);
    }
}
