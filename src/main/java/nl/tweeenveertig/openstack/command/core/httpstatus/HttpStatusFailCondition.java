package nl.tweeenveertig.openstack.command.core.httpstatus;

import org.apache.http.HttpStatus;

public class HttpStatusFailCondition extends HttpStatusChecker {

    public HttpStatusFailCondition(final HttpStatusMatcher matcher) {
        super(matcher);
    }

    public boolean isError() {
        return true;
    }
}
