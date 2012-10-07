package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.command.core.ModifiedException;
import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class IfMatchTest extends HeaderTest{

    @Test
    public void addHeader() {
        testHeader(new IfMatch("cafebabe"));
    }

    @Test
    public void contentMustBeTheSame() {
        new IfMatch("cafebabe").matchAgainst("cafebabe");
    }

    @Test(expected = ModifiedException.class)
    public void differentContentIsError() {
        new IfMatch("cafebabe").matchAgainst("ebacefac");
    }

}
