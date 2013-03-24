package org.javaswift.joss.command.impl.core.httpstatus;

public class HttpStatusFailCondition extends HttpStatusChecker {

    public HttpStatusFailCondition(final HttpStatusMatcher matcher) {
        super(matcher);
    }

    public boolean isError() {
        return true;
    }
}
