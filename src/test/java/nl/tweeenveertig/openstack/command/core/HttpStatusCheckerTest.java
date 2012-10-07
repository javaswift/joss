package nl.tweeenveertig.openstack.command.core;

import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.exception.NotModifiedException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class HttpStatusCheckerTest {

    private HttpStatusChecker[] checkers;

    @Before
    public void setUpCheckers() {
        List<HttpStatusChecker> tempCheckers = new ArrayList<HttpStatusChecker>();
        tempCheckers.add(new HttpStatusChecker(new HttpStatusRange(200, 299), null));
        tempCheckers.add(new HttpStatusChecker(new HttpStatusMatch(404), CommandExceptionError.ENTITY_DOES_NOT_EXIST));
        tempCheckers.add(new HttpStatusChecker(new HttpStatusMatch(304), CommandExceptionError.CONTENT_NOT_MODIFIED, NotModifiedException.class));
        tempCheckers.add(new HttpStatusChecker(new HttpStatusMatch(418), CommandExceptionError.UNKNOWN, RuntimeException.class));
        this.checkers = tempCheckers.toArray(new HttpStatusChecker[tempCheckers.size()]);
    }

    @Test
    public void valueInRange() {
        HttpStatusChecker.verifyCode(checkers, 204);
    }

    @Test
    public void notFoundError() {
        try {
            HttpStatusChecker.verifyCode(checkers, 404);
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.ENTITY_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void unauthorizedError() {
        try {
            HttpStatusChecker.verifyCode(checkers, 401);
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.UNAUTHORIZED, err.getError());
        }
    }

    @Test
    public void accessForbiddenError() {
        try {
            HttpStatusChecker.verifyCode(checkers, 403);
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.ACCESS_FORBIDDEN, err.getError());
        }
    }

    @Test
    public void unknownError() {
        try {
            HttpStatusChecker.verifyCode(checkers, 500);
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.UNKNOWN, err.getError());
        }
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
