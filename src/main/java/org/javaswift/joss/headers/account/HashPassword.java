package org.javaswift.joss.headers.account;

public class HashPassword extends AccountMetadata {

    public static final String X_ACCOUNT_TEMP_URL_KEY = "Temp-Url-Key";

    public HashPassword(String value) {
        super(X_ACCOUNT_TEMP_URL_KEY, value);
    }

}
