package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.exception.HttpStatusExceptionUtil;
import nl.tweeenveertig.openstack.exception.ModifiedException;
import org.apache.http.HttpStatus;

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
