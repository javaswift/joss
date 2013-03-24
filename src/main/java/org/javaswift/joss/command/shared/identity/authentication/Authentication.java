package org.javaswift.joss.command.shared.identity.authentication;

import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="auth")
public class Authentication {
    private PasswordCredentials passwordCredentials;
    private String tenantName;

    public Authentication(final String tenantName, final String username, final String password) {
        this.passwordCredentials = new PasswordCredentials(username, password);
        this.tenantName = tenantName;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public PasswordCredentials getPasswordCredentials() {
        return this.passwordCredentials;
    }
}
