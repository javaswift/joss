package org.javaswift.joss.command.shared.identity.authentication;

import java.util.Collection;
import java.util.Collections;

public class KeystoneV3Identity {

    private final Collection<String> methods;
    private final KeystoneV3PasswordIdentity passwordIdentity;

    public KeystoneV3Identity(String username, String password, String domain) {
        this.passwordIdentity = new KeystoneV3PasswordIdentity(username, password, domain);
        this.methods = Collections.singletonList(passwordIdentity.getMethodName());
    }

    public Collection<String> getMethods() {
        return methods;
    }

    public KeystoneV3PasswordIdentity getPassword() {
        return passwordIdentity;
    }
}
