package org.javaswift.joss.headers.object.conditional;

import org.apache.http.HttpStatus;
import org.apache.http.impl.cookie.DateParseException;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;

import java.util.Date;

public class IfUnmodifiedSince extends AbstractIfSince {

    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    public IfUnmodifiedSince(Long milliseconds) {
        super(milliseconds);
    }

    public IfUnmodifiedSince(String sinceDate) throws DateParseException {
        super(sinceDate);
    }

    public IfUnmodifiedSince(Date date) {
        super(date);
    }

    @Override
    public void sinceAgainst(Date modificationDate) {
        // Milliseconds are not supplied by the browser
        if (getDate().getTime() / 1000 < modificationDate.getTime() / 1000) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_PRECONDITION_FAILED);
        }
    }

    @Override
    public String getHeaderName() {
        return IF_UNMODIFIED_SINCE;
    }
}
