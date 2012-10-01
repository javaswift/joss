package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class IfNoneMatchTest extends HeaderTest{

    @Test
    public void addHeader() {
        testHeader(new IfNoneMatch("cafebabe"));
    }

    @Test
    public void contentMustBeDifferent() {
        new IfNoneMatch("cafebabe").matchAgainst("ebabefac");
    }

    @Test
    public void sameContentIsError() {
        try {
            new IfNoneMatch("cafebabe").matchAgainst("cafebabe");
            fail("should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTENT_NOT_MODIFIED, err.getError());
        }
    }

}
