package org.javaswift.joss.command.shared.identity.access;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tenant {

    public boolean enabled;

    public String id;

    public String name;

    public String handle;

    public String description;
}
