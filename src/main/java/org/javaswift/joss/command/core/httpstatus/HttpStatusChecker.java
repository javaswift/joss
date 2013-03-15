package org.javaswift.joss.command.core.httpstatus;

import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpStatusChecker {

    public static final Logger LOG = LoggerFactory.getLogger(HttpStatusChecker.class);

    public static final HttpStatusChecker AUTHORIZATION_MATCHER =
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_UNAUTHORIZED));
    public  static final HttpStatusChecker FORBIDDEN_MATCHER =
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_FORBIDDEN));

    private HttpStatusMatcher matcher;

    public HttpStatusChecker(final HttpStatusMatcher matcher) {
        this.matcher = matcher;
    }

    public abstract boolean isError();

    public boolean isOk(int httpStatusCode) {
        if (matcher.matches(httpStatusCode)) {
            if (isError()) {
                HttpStatusExceptionUtil.throwException(httpStatusCode);
            }
            return true; // The OK signal
        }
        return false;
    }

    public static void verifyCode(HttpStatusChecker[] checkers, int httpStatusCode) {
        AUTHORIZATION_MATCHER.isOk(httpStatusCode);
        FORBIDDEN_MATCHER.isOk(httpStatusCode);
        for (HttpStatusChecker checker : checkers) {
            if (checker.isOk(httpStatusCode)) {
                return;
            }
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }
}
