package nl.tweeenveertig.openstack.command.core;

import org.apache.http.HttpStatus;

public class HttpStatusChecker {

    private HttpStatusMatcher matcher;

    private CommandExceptionError error;

    private static final HttpStatusChecker authorizationMatcher =
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_UNAUTHORIZED), CommandExceptionError.UNAUTHORIZED);
    private static final HttpStatusChecker forbiddenMatcher =
            new HttpStatusChecker(new HttpStatusMatch(HttpStatus.SC_FORBIDDEN), CommandExceptionError.ACCESS_FORBIDDEN);

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

    public static void verifyCode(HttpStatusChecker[] checkers, int httpStatusCode) {
        authorizationMatcher.isOk(httpStatusCode);
        forbiddenMatcher.isOk(httpStatusCode);
        for (HttpStatusChecker checker : checkers) {
            if (checker.isOk(httpStatusCode)) {
                return;
            }
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }
}
