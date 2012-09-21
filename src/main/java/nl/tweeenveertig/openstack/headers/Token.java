package nl.tweeenveertig.openstack.headers;

/**
 * The security token supplied by the OpenStack Identity component (Keystone). This token is added
 * as a header to all secure command calls.
 */
public class Token extends Header {

    public static String X_AUTH_TOKEN = "X-Auth-Token";

    private String token;

    public Token(final String token) {
        this.token = token;
    }

    @Override
    public String getHeaderValue() {
        return token;
    }

    @Override
    public String getHeaderName() {
        return X_AUTH_TOKEN;
    }
}
