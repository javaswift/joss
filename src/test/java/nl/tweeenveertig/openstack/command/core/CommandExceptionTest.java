package nl.tweeenveertig.openstack.command.core;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CommandExceptionTest {

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @Test
    public void emptyException() {
        CommandException ex = new CommandException("some error");
        assertEquals(0, ex.getHttpStatusCode());
        assertTrue(ex.toString().contains("some error"));
    }
}
