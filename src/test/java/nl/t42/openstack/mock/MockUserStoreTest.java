package nl.t42.openstack.mock;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class MockUserStoreTest {

    private MockUserStore users = new MockUserStore();

    @Before
    public void prepare() {
        users.addUser("richard", "test123");
    }

    @Test
    public void authenticateSuccess() {
        try {
            users.authenticate("richard", "test123");
        } catch (Exception err) {
            fail("Should not have thrown an exception");
        }
    }

    @Test
    public void authenticateFail() {
        try {
            users.authenticate("charlie", "123test");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.UNAUTHORIZED, err.getError());
        }
    }

}
