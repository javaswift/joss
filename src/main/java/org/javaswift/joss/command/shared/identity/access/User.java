package org.javaswift.joss.command.shared.identity.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
