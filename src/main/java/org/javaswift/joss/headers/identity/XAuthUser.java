package org.javaswift.joss.headers.identity;

import org.javaswift.joss.headers.SimpleHeader;

/**
 * User as part of a BASIC authentication. Contains the username. Passed in the headers as X-Auth-User
 */
public class XAuthUser extends SimpleHeader {

    public static String X_AUTH_USER = "X-Auth-User";

    public XAuthUser(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_AUTH_USER;
    }

}
