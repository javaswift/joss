package org.javaswift.joss.command.shared.identity.authentication;

public class KeystoneV3Domain {

    private final String name;

    public KeystoneV3Domain(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
