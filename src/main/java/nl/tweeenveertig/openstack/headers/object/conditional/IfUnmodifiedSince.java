package nl.tweeenveertig.openstack.headers.object.conditional;

import org.apache.http.impl.cookie.DateParseException;

import java.util.Date;

public class IfUnmodifiedSince extends IfSince {

    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    public IfUnmodifiedSince(String sinceDate) throws DateParseException {
        super(sinceDate);
    }

    public IfUnmodifiedSince(Date date) {
        super(date);
    }

    @Override
    public String getHeaderName() {
        return IF_UNMODIFIED_SINCE;
    }
}
