package org.javaswift.joss.headers.identity;

import org.javaswift.joss.headers.SimpleHeader;

/**
 * User as part of a TEMPAUTH authentication. Contains the password. Passed in the headers as X-Storage-Pass
 */
public class XStoragePass extends SimpleHeader {

    public static String X_STORAGE_PASS = "X-Storage-Pass";

    public XStoragePass(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_STORAGE_PASS;
    }

}
