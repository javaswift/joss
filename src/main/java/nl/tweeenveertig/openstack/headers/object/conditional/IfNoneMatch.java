package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.apache.http.HttpStatus;

public class IfNoneMatch extends AbstractIfMatch {

    public static final String IF_NONE_MATCH = "If-None-Match";

    public IfNoneMatch(String value) {
        super(value);
    }

    @Override
    public void matchAgainst(String matchValue) {
        if (getHeaderValue().equals(matchValue)) {
            throw new CommandException(HttpStatus.SC_NOT_MODIFIED, CommandExceptionError.CONTENT_NOT_MODIFIED);
        }
    }

    @Override
    public String getHeaderName() {
        return IF_NONE_MATCH;
    }
}
