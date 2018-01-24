package org.javaswift.joss.command.shared.identity.authentication;

import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="auth")
public class KeystoneV3Authentication {

    private KeystoneV3Identity identity;

    public KeystoneV3Authentication(String username, String password, String domain) {
        this.identity = new KeystoneV3Identity(username, password, domain);
    }

    public KeystoneV3Identity getIdentity() {
        return identity;
    }
}
