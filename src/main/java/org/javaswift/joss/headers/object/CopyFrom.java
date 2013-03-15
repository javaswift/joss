package org.javaswift.joss.headers.object;

import org.javaswift.joss.headers.SimpleHeader;

/**
 * Determines the source object in a copy action
 */
public class CopyFrom extends SimpleHeader {

    public static final String X_COPY_FROM      = "X-Copy-From";

    public CopyFrom(String copyFrom) {
        super(copyFrom);
    }

    @Override
    public String getHeaderName() {
        return X_COPY_FROM;
    }
}
