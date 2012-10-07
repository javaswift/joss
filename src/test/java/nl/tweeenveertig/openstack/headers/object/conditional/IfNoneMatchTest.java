package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.NotModifiedException;
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

    @Test(expected = NotModifiedException.class)
    public void sameContentIsError() {
        new IfNoneMatch("cafebabe").matchAgainst("cafebabe");
    }

}
