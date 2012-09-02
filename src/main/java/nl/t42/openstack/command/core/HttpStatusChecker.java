package nl.t42.openstack.command.core;

import java.util.List;

public class HttpStatusChecker {

    private HttpStatusMatcher matcher;

    private CommandExceptionError error;

    public HttpStatusChecker(final HttpStatusMatcher matcher, final CommandExceptionError error) {
        this.matcher = matcher;
        this.error = error;
    }

    public boolean isOk(int httpStatusCode) {
        if (matcher.matches(httpStatusCode)) {
            if (error == null) {
                return true; // The OK signal
            } else {
                throw new CommandException(httpStatusCode, error);
            }
        }
        return false;
    }

    public static void verifyCode(List<HttpStatusChecker> checkers, int httpStatusCode) {
        for (HttpStatusChecker checker : checkers) {
            if (checker.isOk(httpStatusCode)) {
                return;
            }
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }
}
