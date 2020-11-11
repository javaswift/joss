package org.javaswift.joss.command.shared.identity.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {

    public String id;

    public String name;
}
