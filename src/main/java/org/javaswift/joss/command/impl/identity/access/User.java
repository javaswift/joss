package org.javaswift.joss.command.impl.identity.access;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public String username;

//    @JsonProperty(value = "roles_links")
//    public List rolesLinks = new ArrayList<String>();

    public String id;

    public List<Role> roles = new ArrayList<Role>();

    public String name;
}
