package org.javaswift.joss.headers;

/**
 * The security token supplied by the OpenStack Identity component (Keystone). This token is added
 * as a header to all secure command calls.
 */
public class Token extends SimpleHeader {

    public static String X_AUTH_TOKEN = "X-Auth-Token";

    public Token(final String token) {
        super(token);
    }

    @Override
    public String getHeaderName() {
        return X_AUTH_TOKEN;
    }
}
