package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.apache.http.HttpStatus;

public class IfMatch extends AbstractIfMatch {

    public static final String IF_MATCH = "If-Match";

    public IfMatch(String value) {
        super(value);
    }

    @Override
    public void matchAgainst(String matchValue) {
        if (!getHeaderValue().equals(matchValue)) {
            throw new CommandException(HttpStatus.SC_PRECONDITION_FAILED, CommandExceptionError.CONTENT_DIFFERENT);
        }
    }

    @Override
    public String getHeaderName() {
        return IF_MATCH;
    }
}
