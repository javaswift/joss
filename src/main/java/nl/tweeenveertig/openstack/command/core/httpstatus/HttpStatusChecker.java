package nl.tweeenveertig.openstack.command.core.httpstatus;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.exception.HttpStatusToExceptionMapper;

import static nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusFailCondition.AUTHORIZATION_MATCHER;
import static nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusFailCondition.FORBIDDEN_MATCHER;

public abstract class HttpStatusChecker {

    private HttpStatusMatcher matcher;

    public HttpStatusChecker(final HttpStatusMatcher matcher) {
        this.matcher = matcher;
    }

    public abstract boolean isError();

    public boolean isOk(int httpStatusCode) {
        if (matcher.matches(httpStatusCode)) {
            if (isError()) {
                HttpStatusToExceptionMapper.throwException(httpStatusCode);
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
