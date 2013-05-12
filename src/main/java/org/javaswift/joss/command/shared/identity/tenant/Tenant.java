package org.javaswift.joss.command.shared.identity.tenant;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tenant {

    public String id;

    public String name;

    public boolean enabled;

}
