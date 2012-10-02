package nl.tweeenveertig.openstack.headers.object.conditional;

import org.apache.http.impl.cookie.DateParseException;

import java.util.Date;

public class IfModifiedSince extends AbstractIfSince {

    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

    public IfModifiedSince(String sinceDate) throws DateParseException {
        super(sinceDate);
    }

    public IfModifiedSince(Date date) {
        super(date);
    }

    @Override
    public String getHeaderName() {
        return IF_MODIFIED_SINCE;
    }
}
