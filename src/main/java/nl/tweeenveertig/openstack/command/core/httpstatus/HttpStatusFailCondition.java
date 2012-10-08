package nl.tweeenveertig.openstack.command.core.httpstatus;

import org.apache.http.HttpStatus;

public class HttpStatusFailCondition extends HttpStatusChecker {

    public static final HttpStatusChecker AUTHORIZATION_MATCHER =
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_UNAUTHORIZED));
    public  static final HttpStatusChecker FORBIDDEN_MATCHER =
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_FORBIDDEN));

    public HttpStatusFailCondition(final HttpStatusMatcher matcher) {
        super(matcher);
    }

    public boolean isError() {
        return true;
    }
}
