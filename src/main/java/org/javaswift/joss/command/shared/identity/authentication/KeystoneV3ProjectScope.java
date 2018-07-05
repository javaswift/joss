package org.javaswift.joss.command.shared.identity.authentication;

public class KeystoneV3ProjectScope implements KeystoneV3Scope {

    private KeystoneV3ProjectWrapped project;

    public static class KeystoneV3ProjectWrapped {
        private final String name;
        private final KeystoneV3Domain domain;

        private KeystoneV3ProjectWrapped(String name, KeystoneV3Domain domain) {
            this.name = name;
            this.domain = domain;
        }

        public String getName() {
            return name;
        }

        public KeystoneV3Domain getDomain() {
            return domain;
        }
    }

    public KeystoneV3ProjectScope(String name, KeystoneV3Domain domain) {
        this.project = new KeystoneV3ProjectWrapped(name, domain);
    }

    public KeystoneV3ProjectWrapped getProject() {
        return project;
    }
}