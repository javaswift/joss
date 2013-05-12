package org.javaswift.joss.command.shared.identity.tenant;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TenantsTest {

    @Test
    public void testMarshalling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Tenant tenant = new Tenant();
        tenant.enabled = true;
        tenant.id = "tenant-id";
        tenant.name = "tenant-name";
        Tenants tenants = new Tenants();
        tenants.tenants.add(tenant);
        assertEquals("{\"tenants\":[{\"id\":\"tenant-id\",\"name\":\"tenant-name\",\"enabled\":true}]}", mapper.writeValueAsString(tenants));
    }

}
