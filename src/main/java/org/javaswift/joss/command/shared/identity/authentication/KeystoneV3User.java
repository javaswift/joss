package org.javaswift.joss.command.shared.identity.authentication;

public class KeystoneV3User {

    private final String name;
    private final String password;
    private final KeystoneV3Domain domain;

    public KeystoneV3User(String name, String password, String domainName) {
        this.name = name;
        this.password = password;
        this.domain = new KeystoneV3Domain(domainName);
    }

    public KeystoneV3Domain getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
