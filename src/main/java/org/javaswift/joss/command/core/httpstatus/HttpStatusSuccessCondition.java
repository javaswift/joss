package org.javaswift.joss.command.core.httpstatus;

public class HttpStatusSuccessCondition extends HttpStatusChecker {

    public HttpStatusSuccessCondition(final HttpStatusMatcher matcher) {
        super(matcher);
    }

    public boolean isError() {
        return false;
    }
}
