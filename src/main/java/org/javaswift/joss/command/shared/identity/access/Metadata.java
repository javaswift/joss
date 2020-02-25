package org.javaswift.joss.command.shared.identity.access;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {

    public boolean is_admin;

    public List<String> roles;

}
