package org.javaswift.joss.command.impl.core.httpstatus;

import org.javaswift.joss.exception.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HttpStatusCheckerTest {

    private HttpStatusChecker[] checkers;

    @Before
    public void setUpCheckers() {
        List<HttpStatusChecker> tempCheckers = new ArrayList<HttpStatusChecker>();
        tempCheckers.add(new HttpStatusSuccessCondition(new HttpStatusRange(200, 299)));
        tempCheckers.add(new HttpStatusFailCondition(new HttpStatusMatch(404)));
        tempCheckers.add(new HttpStatusFailCondition(new HttpStatusMatch(304)));
        tempCheckers.add(new HttpStatusFailCondition(new HttpStatusMatch(418)));
        this.checkers = tempCheckers.toArray(new HttpStatusChecker[tempCheckers.size()]);
    }

    @Test
    public void valueInRange() {
        HttpStatusChecker.verifyCode(checkers, 204);
    }

    @Test (expected = NotFoundException.class)
    public void notFoundError() {
        HttpStatusChecker.verifyCode(checkers, 404);
    }

    @Test (expected = UnauthorizedException.class)
    public void unauthorizedError() {
        HttpStatusChecker.verifyCode(checkers, 401);
    }

    @Test (expected = ForbiddenException.class)
    public void accessForbiddenError() {
        HttpStatusChecker.verifyCode(checkers, 403);
    }

    @Test (expected = CommandException.class)
    public void unknownError() {
        HttpStatusChecker.verifyCode(checkers, 500);
    }

    @Test(expected = NotModifiedException.class)
    public void throwNotModifiedException() {
        HttpStatusChecker.verifyCode(checkers, 304);
    }

    @Test(expected = CommandException.class)
    public void unableToCastException() {
        HttpStatusChecker.verifyCode(checkers, 418);
    }

}
