package org.javaswift.joss.command.shared.identity.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="auth")
public class Authentication {
    private PasswordCredentials passwordCredentials;
    private String tenantName;
    private String tenantId;

    public Authentication(final String tenantName, String tenantId, final String username, final String password) {
        this.passwordCredentials = new PasswordCredentials(username, password);
        this.tenantName = tenantName;
        this.tenantId = tenantId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getTenantId() {
        return this.tenantId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getTenantName() {
        return this.tenantName;
    }

    public PasswordCredentials getPasswordCredentials() {
        return this.passwordCredentials;
    }
}
