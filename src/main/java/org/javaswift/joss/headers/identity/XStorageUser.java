package org.javaswift.joss.headers.identity;

import org.javaswift.joss.headers.SimpleHeader;

/**
 * User as part of a TEMPAUTH authentication. Contains the username. Passed in the headers as X-Storage-User
 */
public class XStorageUser extends SimpleHeader {

    public static String X_STORAGE_USER = "X-Storage-User";

    public XStorageUser(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_STORAGE_USER;
    }

}
