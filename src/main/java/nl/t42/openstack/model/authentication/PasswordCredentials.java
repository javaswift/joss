package nl.t42.openstack.model.authentication;

public class PasswordCredentials {

    private String username;

    private String password;

    public PasswordCredentials(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
