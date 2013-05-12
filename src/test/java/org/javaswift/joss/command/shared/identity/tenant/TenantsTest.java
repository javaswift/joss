package org.javaswift.joss.command.shared.identity.tenant;

import org.codehaus.jackson.map.ObjectMapper;
import org.javaswift.joss.exception.CommandException;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TenantsTest {

    @Test
    public void marshalling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Tenant tenant = new Tenant();
        tenant.enabled = true;
        tenant.id = "tenant-id";
        tenant.name = "tenant-name";
        Tenants tenants = new Tenants();
        tenants.tenants.add(tenant);
        assertEquals("{\"tenants\":[{\"id\":\"tenant-id\",\"name\":\"tenant-name\",\"enabled\":true}]}", mapper.writeValueAsString(tenants));
    }

    @Test(expected = CommandException.class)
    public void noTenants() {
        Tenants tenants = new Tenants();
        tenants.getTenant();
    }

    @Test
    public void tooManyTenants() {
        Tenants tenants = new Tenants();
        tenants.tenants.add(createTenant("#1", "Alice", true));
        tenants.tenants.add(createTenant("#2", "Bob", true));
        try {
            tenants.getTenant();
            fail("Should have thrown an exception");
        } catch (CommandException err) {
            assertTrue(err.getMessage().contains("[ID:#1/Name:Alice]"));
            assertTrue(err.getMessage().contains("[ID:#2/Name:Bob]"));
        }
    }

    @Test
    public void onlyOneEnabledTenant() {
        Tenants tenants = new Tenants();
        tenants.tenants.add(createTenant("#1", "Alice", false));
        tenants.tenants.add(createTenant("#2", "Bob", true));
        tenants.tenants.add(createTenant("#3", "Carol", false));
        Tenant tenant = tenants.getTenant();
        assertEquals("Bob", tenant.name);
    }

    protected Tenant createTenant(String id, String name, boolean enabled) {
        Tenant tenant = new Tenant();
        tenant.id = id;
        tenant.name = name;
        tenant.enabled = enabled;
        return tenant;
    }

}
