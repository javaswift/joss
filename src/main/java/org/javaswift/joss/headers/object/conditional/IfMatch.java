package org.javaswift.joss.headers.object.conditional;

import org.apache.http.HttpStatus;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;

public class IfMatch extends AbstractIfMatch {

    public static final String IF_MATCH = "If-Match";

    public IfMatch(String value) {
        super(value);
    }

    @Override
    public void matchAgainst(String matchValue) {
        if (!getHeaderValue().equals(matchValue)) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_PRECONDITION_FAILED);
        }
    }

    @Override
    public String getHeaderName() {
        return IF_MATCH;
    }
}
