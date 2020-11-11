package org.javaswift.joss.command.shared.identity.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {

    public String expires;

    public String id;

    public Tenant tenant;
}
