package org.javaswift.joss.command.shared.identity.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class KeystoneV3PasswordIdentity {

    private final KeystoneV3User user;

    public KeystoneV3PasswordIdentity(String username, String password, String domain) {
        this.user = new KeystoneV3User(username, password, domain);
    }

    @JsonIgnore
    public String getMethodName() {
        return "password";
    }

    public KeystoneV3User getUser() {
        return user;
    }
}
