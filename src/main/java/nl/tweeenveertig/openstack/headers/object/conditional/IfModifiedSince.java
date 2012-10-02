package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.apache.http.HttpStatus;
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
    public void sinceAgainst(Date modificationDate) {
        if (getDate().compareTo(modificationDate) >= 0) {
            throw new CommandException(HttpStatus.SC_NOT_MODIFIED, CommandExceptionError.CONTENT_NOT_MODIFIED);
        }
    }

    @Override
    public String getHeaderName() {
        return IF_MODIFIED_SINCE;
    }
}
