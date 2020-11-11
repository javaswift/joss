package org.javaswift.joss.command.shared.identity.tenant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tenant {

    public String id;

    public String name;

    public boolean enabled;

}
