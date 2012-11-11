package nl.tweeenveertig.openstack.command.core.httpstatus;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.exception.HttpStatusExceptionUtil;
import org.apache.http.HttpStatus;

public abstract class HttpStatusChecker {

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
