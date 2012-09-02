package nl.t42.openstack.command.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class HttpStatusCheckerTest {

    private List<HttpStatusChecker> checkers;

    @Before
    public void setUpCheckers() {
        this.checkers = new ArrayList<HttpStatusChecker>();
        this.checkers.add(new HttpStatusChecker(new HttpStatusRange(200, 299), null));
        this.checkers.add(new HttpStatusChecker(new HttpStatusMatch(404), CommandExceptionError.CONTAINER_DOES_NOT_EXIST));
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
            assertEquals(CommandExceptionError.CONTAINER_DOES_NOT_EXIST, err.getError());
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
}
