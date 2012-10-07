package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.apache.http.HttpStatus;
import org.apache.http.impl.cookie.DateParseException;

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
            throw new CommandException(HttpStatus.SC_PRECONDITION_FAILED, CommandExceptionError.CONTENT_DIFFERENT);
        }
    }

    @Override
    public String getHeaderName() {
        return IF_UNMODIFIED_SINCE;
    }
}
