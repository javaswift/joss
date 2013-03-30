package org.javaswift.joss.headers.object;

import org.javaswift.joss.headers.Header;

public class DeleteAfter extends Header {

    public static String X_DELETE_AFTER = "X-Delete-After";

    private long expireAfterSeconds;

    public DeleteAfter(long expireAfterSeconds) {
        this.expireAfterSeconds = expireAfterSeconds;
    }

    public long getExpireAfterSeconds() {
        return this.expireAfterSeconds;
    }

    @Override
    public String getHeaderValue() {
        return Long.toString(expireAfterSeconds);
    }

    @Override
    public String getHeaderName() {
        return X_DELETE_AFTER;
    }
}
