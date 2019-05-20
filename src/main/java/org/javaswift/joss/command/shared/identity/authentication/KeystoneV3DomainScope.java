package org.javaswift.joss.command.shared.identity.authentication;

public class KeystoneV3DomainScope implements KeystoneV3Scope {

    private final KeystoneV3Domain domain;

    public KeystoneV3DomainScope(KeystoneV3Domain domain) {
        this.domain = domain;
    }

    public KeystoneV3Domain getDomain() {
        return domain;
    }
}