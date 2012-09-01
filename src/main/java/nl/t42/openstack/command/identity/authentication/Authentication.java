package nl.t42.openstack.command.identity.authentication;

import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName(value="auth")
public class Authentication {
    private PasswordCredentials passwordCredentials;

    public Authentication(final String username, final String password) {
        this.passwordCredentials = new PasswordCredentials(username, password);
    }

    public PasswordCredentials getPasswordCredentials() {
        return this.passwordCredentials;
    }
}
