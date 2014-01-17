package org.javaswift.joss.headers.identity;

import org.javaswift.joss.headers.SimpleHeader;

/**
 * User as part of a BASIC authentication. Contains the password/key. Passed in the headers as X-Auth-Key
 */
public class XAuthKey extends SimpleHeader {

    public static String X_AUTH_KEY = "X-Auth-Key";

    public XAuthKey(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_AUTH_KEY;
    }

}
