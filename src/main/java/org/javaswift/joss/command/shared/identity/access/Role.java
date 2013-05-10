package org.javaswift.joss.command.shared.identity.access;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {

    public String id;

    public String name;
}
