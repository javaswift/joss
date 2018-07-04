package org.javaswift.joss.command.shared.identity.authentication;

import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="auth")
public class KeystoneV3Authentication {

    private KeystoneV3Identity identity;
    private KeystoneV3Scope scope;

    public void setScope(KeystoneV3Scope scope) { this.scope = scope; }

    public KeystoneV3Scope getScope() { return scope; }

    public void setIdentity(KeystoneV3Identity identity) { this.identity = identity; }

    public KeystoneV3Identity getIdentity() {
        return identity;
    }
}