package org.javaswift.joss.command.shared.identity.authentication;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonRootName(value="auth")
public class Authentication {
    private PasswordCredentials passwordCredentials;
    private String tenantName;
    private String tenantId;

    public Authentication(final String tenantName, String tenantId, final String username, final String password) {
        this(tenantName, tenantId, new PasswordCredentials(username, password));
    }

    @JsonCreator
    public Authentication(@JsonProperty final String tenantName, @JsonProperty String tenantId, @JsonProperty PasswordCredentials passwordCredentials) {
        this.passwordCredentials = passwordCredentials;
        this.tenantName = tenantName;
        this.tenantId = tenantId;
    }

    @JsonSerialize(include=Inclusion.NON_NULL)
    public String getTenantId() {
        return this.tenantId;
    }

    @JsonSerialize(include=Inclusion.NON_NULL)
    public String getTenantName() {
        return this.tenantName;
    }

    public PasswordCredentials getPasswordCredentials() {
        return this.passwordCredentials;
    }
}
