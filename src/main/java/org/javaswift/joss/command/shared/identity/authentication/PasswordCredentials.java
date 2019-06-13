package org.javaswift.joss.command.shared.identity.authentication;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class PasswordCredentials {

    private String username;

    private String password;

    @JsonCreator
    public PasswordCredentials(@JsonProperty final String username, @JsonProperty final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
