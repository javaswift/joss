package org.javaswift.joss.command.shared.identity.access;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {

    public String expires;

    public String id;

    public Tenant tenant;
}
