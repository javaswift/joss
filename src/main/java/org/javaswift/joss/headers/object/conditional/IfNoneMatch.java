package org.javaswift.joss.headers.object.conditional;

import org.javaswift.joss.exception.HttpStatusExceptionUtil;
import org.apache.http.HttpStatus;

public class IfNoneMatch extends AbstractIfMatch {

    public static final String IF_NONE_MATCH = "If-None-Match";

    public IfNoneMatch(String value) {
        super(value);
    }

    @Override
    public void matchAgainst(String matchValue) {
        if (getHeaderValue().equals(matchValue)) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_MODIFIED);
        }
    }

    @Override
    public String getHeaderName() {
        return IF_NONE_MATCH;
    }
}
