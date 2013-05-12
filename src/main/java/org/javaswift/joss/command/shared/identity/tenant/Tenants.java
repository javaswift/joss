package org.javaswift.joss.command.shared.identity.tenant;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tenants {

    public List<Tenant> tenants = new ArrayList<Tenant>();

    @JsonIgnore
    public Tenant getTenant() {
        return this.tenants.get(0);
    }

}
