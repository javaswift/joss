package org.javaswift.joss.command.shared.identity.tenant;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.javaswift.joss.exception.CommandException;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tenants {

    public List<Tenant> tenants = new ArrayList<Tenant>();

    @JsonIgnore
    public Tenant getTenant() {
        List<Tenant> enabledTenants = getEnabledTenants();
        if (enabledTenants.size() < 1) {
            throw new CommandException("No enabled tenant found during auto-discovery");
        }
        if (enabledTenants.size() > 1) {
            throw new CommandException("Multiple enabled tenants found. Please configure one to use: "+getTenantString(enabledTenants));
        }
        return enabledTenants.get(0);
    }

    @JsonIgnore
    public String getTenantString(List<Tenant> tenants) {
        StringBuilder returnString = new StringBuilder();
        boolean first = true;
        for (Tenant tenant : tenants) {
            if (!first) {
                returnString.append(", ");
            }
            returnString
                    .append("[ID:")
                    .append(tenant.id)
                    .append("/Name:")
                    .append(tenant.name)
                    .append("]");
            first = false;
        }
        return returnString.toString();
    }

    @JsonIgnore
    public List<Tenant> getEnabledTenants() {
        List<Tenant> enabledTenants = new ArrayList<Tenant>();
        for (Tenant tenant : tenants) {
            if (tenant.enabled) {
                enabledTenants.add(tenant);
            }
        }
        return enabledTenants;
    }

}
